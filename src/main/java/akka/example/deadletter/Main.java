package akka.example.deadletter;

import akka.actor.*;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

/**
 * RemoteActor에게 'hello world' 메시지를 보내면 대문자로 변환된 'HELLO WORLD' 메시지를 받는다.
 */
public class Main extends AbstractActor {
    final static String config =
            "akka {\n" +
            "  loglevel = \"INFO\"\n" +
            "}";

    public static void main(String[] args) {
        Config config = ConfigFactory.parseString(Main.config);
        ActorSystem system = ActorSystem.create("system", config);
        ActorRef deadLetterMonitor = system.actorOf(Props.create(DeadLetterMonitor.class));
        system.eventStream().subscribe(deadLetterMonitor, DeadLetter.class);
        ActorRef sender = system.actorOf(Props.create(Main.class), "local");
        ActorRef dummy = system.actorOf(Props.create(DummyActor.class));

        String msg = "hello world";
        dummy.tell(PoisonPill.getInstance(), sender);
        System.out.println("메시지 전송: " + msg);
        dummy.tell(msg, sender);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(
                        String.class,
                        msg -> System.out.println("메시지 수신: " + msg)
                )
                .build();
    }
}
