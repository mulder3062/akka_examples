package akka.example.echo;

import akka.actor.*;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.io.Tcp;
import akka.io.TcpMessage;
import akka.util.ByteString;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

public class Server extends AbstractActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    private final InetSocketAddress address;

    public static class RecvHandler extends AbstractActor {
        private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

        public static Props props() {
            return Props.create(RecvHandler.class);
        }

        @Override
        public Receive createReceive() {
            return receiveBuilder()
                    .match(
                            Tcp.Received.class,
                            msg -> {
                                String request = msg.data().decodeString(Charset.defaultCharset());
                                log.info("RECV: {}", request);

                                // 대문자로 변경하여 응답
                                String response = request.toUpperCase();
                                log.info("SEND: {}", response);
                                ByteString responseMsg = ByteString.fromString(response);
                                getSender().tell(TcpMessage.write(responseMsg), getSelf());
                            })
                    .match(
                            Tcp.ConnectionClosed.class,
                            msg -> {
                                if (msg.isPeerClosed()) {
                                    log.info("Peer closed");
                                } else {
                                    getContext().stop(getSelf());
                                }
                            }
                    )
                    .matchAny(
                            any -> {
                                log.info("Any: " + any);
                            })
                    .build();
        }
    }

    public static void main(String[] args) {
        InetSocketAddress address = new InetSocketAddress("127.0.0.1", 2020);
        Config config = ConfigFactory.parseString("akka { loglevel=\"DEBUG\" }");
        ActorSystem system = ActorSystem.create("echo", config);
        system.actorOf(Server.props(address), "server");
    }

    public Server(InetSocketAddress address) {
        this.address = address;
    }

    public static Props props(InetSocketAddress address) {
        return Props.create(Server.class, address);
    }

    @Override
    public SupervisorStrategy supervisorStrategy() {
        return SupervisorStrategy.stoppingStrategy();
    }

    @Override
    public void preStart() throws Exception {
        final ActorRef tcp = Tcp.get(getContext().getSystem()).manager();
        tcp.tell(TcpMessage.bind(getSelf(), address, 100), getSelf());
    }

    @Override
    public void postRestart(Throwable arg0) throws Exception {
        // do not restart
        getContext().stop(getSelf());
    }

    @Override
    public Receive createReceive() {
        ActorRef connection = getSender();

        return receiveBuilder()
                .match(
                        Tcp.Bound.class,
                        msg -> {
                            log.info("Listening on {}", msg.localAddress());
                        })
                .match(
                        Tcp.CommandFailed.class,
                        failed -> {
                            getContext().stop(getSelf());
                        })
                .match( // Client가 연결되면 Handler에 처리 위임
                        Tcp.Connected.class,
                        conn -> {
                            log.info("Connection from {}", conn.remoteAddress());
                            ActorRef recvHandler = getContext()
                                    .actorOf(Props.create(RecvHandler.class), "recvHandler" + conn.remoteAddress().hashCode());
                            getSender().tell(TcpMessage.register(recvHandler), getSelf());
                        })
                .build();
    }
}