package akka.example.echo;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.io.Tcp;
import akka.io.TcpMessage;
import akka.util.ByteString;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

public class Client extends AbstractActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    private final InetSocketAddress remote;

    public static void main(String[] args) {
        InetSocketAddress address = new InetSocketAddress("127.0.0.1", 2020);
        Config config = ConfigFactory.parseString("akka { loglevel=\"DEBUG\" }");
        ActorSystem system = ActorSystem.create("echo", config);
        system.actorOf(Client.props(address), "client");

    }

    public static Props props(InetSocketAddress remote) {
        return Props.create(Client.class, remote);
    }

    public Client(InetSocketAddress remote) {
        this.remote = remote;

        final ActorRef tcp = Tcp.get(getContext().getSystem()).manager();

        // TCP 매너저에게 연결 요청
        tcp.tell(TcpMessage.connect(remote), getSelf());
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
               .match( // 연결 등 실패 메시지
                        Tcp.CommandFailed.class,
                        msg -> {
                            log.info("Connection failed");
                            getContext().stop(getSelf());
                        })
                .match( // 연결 성공
                        Tcp.Connected.class,
                        msg -> {
                            log.info("Connected: {}", remote);
                            getSender().tell(TcpMessage.register(getSelf()), getSelf());
                            getContext().become(connected(getSender()));
                            getSelf().tell("hello", ActorRef.noSender());
                        })
                .build();
    }

    private Receive connected(final ActorRef connection) {
        return receiveBuilder()
                .match( // 서버로 메시지 전송
                        String.class,
                        msg -> {
                            log.info("SEND: {}", msg);
                            connection.tell(TcpMessage.write(ByteString.fromArray(msg.getBytes())), getSelf());
                        }
                )
                .match( // 소켓 오류 발생 처리
                        Tcp.CommandFailed.class,
                        failed -> {
                            log.info("Tcp command failed: {}", failed.causedByString());
                        }
                )
                .match( // 서버 Echo 메세지 수신
                        Tcp.Received.class,
                        msg -> {
                            log.info("RECV: {}", msg.data().decodeString(Charset.defaultCharset()));

                            // 수신 후 소켓을 닫는다. 계속 송신하려면 아래를 처리하지 않으면 된다.
//                            connection.tell(TcpMessage.close(), getSelf());
                        })
                .matchEquals( // close 요청이 들어오면 소켓을 닫는다
                        "close",
                        msg -> {
                            connection.tell(TcpMessage.close(), getSelf());
                        })
                .match( // 소켓을 닫히면 액터를 종료한다.
                        Tcp.ConnectionClosed.class,
                        msg -> {
                            log.info("Connection closed: {}", msg.toString());
                            getContext().stop(getSelf());
                        })
                .build();
    }
}
