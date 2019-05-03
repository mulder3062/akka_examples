package akka.example.watch;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.Terminated;

/**
 * WatchActor는 TargetActor를 모니터링하고 중지되면 Terminated 메시지를 받는다
 */

public class WatchActor extends AbstractActor {
    private final ActorRef target;

    public WatchActor(ActorRef target) {
        this.target = target;
    }

    public static Props props(ActorRef target) {
        return Props.create(WatchActor.class, () -> new WatchActor(target));
    }

    @Override
    public void preStart() throws Exception {
        getContext().watch(target);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(
                        Terminated.class,
                        t -> System.out.println("TargetActor terminated: " + t.actor())
                )
                .build();
    }
}
