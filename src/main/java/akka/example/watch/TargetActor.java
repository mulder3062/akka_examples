package akka.example.watch;

import akka.actor.AbstractActor;
import akka.actor.Props;

public class TargetActor extends AbstractActor {
    public static Props props() {
        return Props.create(TargetActor.class, () -> new TargetActor());
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .matchAny(
                        obj -> System.out.println("Recv: " + obj)
                )
                .build();

    }
}
