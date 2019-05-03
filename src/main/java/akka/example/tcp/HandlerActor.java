package akka.example.tcp;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.io.Tcp;
import akka.io.TcpMessage;
import akka.util.ByteString;

import java.net.InetSocketAddress;

public class HandlerActor extends AbstractActor {
    InetSocketAddress remote;

    ActorRef connection;

    LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    public HandlerActor(InetSocketAddress remote, ActorRef connection) {
        this.remote = remote;
        this.connection = connection;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(
                        Tcp.Received.class,
                        received -> {
                            ByteString data = received.data();
                            Tcp.Command cmd = TcpMessage.write(data);
                            connection.tell(cmd, getSelf());
                        }
                )
                .match(
                        Tcp.ConnectionClosed.class,
                        connectionClosed -> {
                            log.info("The connection {}:{} is closed.", remote.getHostString(),
                                    remote.getPort());
                            getContext().stop(getSelf());
                        }
                )
                .build();
    }
}