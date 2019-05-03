package akka.example.tcp;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.io.Tcp;
import akka.io.TcpMessage;

import java.net.InetSocketAddress;

public class ServerActor extends AbstractActor {
    private final ActorRef tcpManager = Tcp.get(getContext().system()).getManager();
    private LoggingAdapter log = Logging.getLogger(getContext().system(), this);
    private String host = "127.0.0.1";
    private int port = 2020;

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .matchEquals(
                        "start",
                        message -> {
                            InetSocketAddress endpoint = new InetSocketAddress(host, port);
                            Tcp.Command cmd = TcpMessage.bind(getSelf(), endpoint, 10);
                            tcpManager.tell(cmd, getSelf());
                        })
                .match(
                        Tcp.Bound.class,
                        bound -> {
                            InetSocketAddress addr = bound.localAddress();
                            log.info("Bound to {}:{}", addr.getHostString(), addr.getPort());
                        })
                .match(
                        Tcp.Connected.class,
                        connected -> {
                            InetSocketAddress remote = ((Tcp.Connected) connected)
                                    .remoteAddress();
                            log.info("A new connection from {}:{}", remote.getHostString(),
                                    remote.getPort());
                            ActorRef handlerActor = getContext().actorOf(
                                    Props.create(HandlerActor.class, remote, getSender()));
                            Tcp.Command cmd = TcpMessage.register(handlerActor);
                            getSender().tell(cmd, getSelf());
                        })
                .build();
    }

    public ServerActor() {

    }
}