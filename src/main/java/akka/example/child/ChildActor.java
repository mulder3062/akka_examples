package akka.example.child;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class ChildActor extends AbstractActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    public static Props props() {
        return Props.create(ChildActor.class, () -> new ChildActor());
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(String.class, msg -> {
                    log.info("msg: " + msg);
                    sender().tell("Hi", self());
                })
                .match(Number.class, n -> {
                    log.info("number: " + n);
                })
                .build();

    }
}
