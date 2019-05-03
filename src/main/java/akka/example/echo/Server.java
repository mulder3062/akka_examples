package akka.example.echo;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.io.Tcp;
import akka.io.TcpMessage;

import java.net.InetSocketAddress;

public class Server extends AbstractActor {

    final ActorRef manager;

    public Server(ActorRef manager) {
        this.manager = manager;
    }

    public static Props props(ActorRef manager) {
        return Props.create(Server.class, manager);
    }

    @Override
    public void preStart() throws Exception {
        final ActorRef tcp = Tcp.get(getContext().getSystem()).manager();
        tcp.tell(TcpMessage.bind(getSelf(), new InetSocketAddress("localhost", 2020), 100), getSelf());
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(
                        Tcp.Bound.class,
                        msg -> {
                            manager.tell(msg, getSelf());
                        })
                .match(
                        Tcp.CommandFailed.class,
                        msg -> {
                            getContext().stop(getSelf());
                        })
                .match(
                        Tcp.Connected.class,
                        conn -> {
                            manager.tell(conn, getSelf());
                            final ActorRef handler =
                                    getContext().actorOf(Props.create(SimplisticHandler.class));
                            getSender().tell(TcpMessage.register(handler), getSelf());
                        })
                .build();
    }
}