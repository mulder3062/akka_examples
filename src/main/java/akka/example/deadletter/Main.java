package akka.example.deadletter;

import akka.actor.*;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

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

        // 더미 액터를 중지시킨다.
        dummy.tell(PoisonPill.getInstance(), sender);

        // 메시지를 보내지만 실해하고 DeadLetter 메시지함에 쌓이게 된다.
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
