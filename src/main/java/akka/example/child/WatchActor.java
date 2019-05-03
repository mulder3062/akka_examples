package akka.example.child;

import akka.actor.*;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import java.io.IOException;

/**
 * WatchActor가 ChildActor를 감시하다. 재시작/중지/재개를 시킨다.
 *
 * (아직 미완성 코드)
 */

public class WatchActor extends AbstractActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    private ActorRef childActor;

    public static Props props() {
        return Props.create(WatchActor.class, () -> new WatchActor());
    }

    public static void main(String[] args) {
        ActorSystem watchSystem = ActorSystem.create("watch-system");

        try {
            ActorRef watchActor = watchSystem.actorOf(WatchActor.props(), "watchActor");
            watchActor.tell("Hello~", ActorRef.noSender());
            watchActor.tell("kill", ActorRef.noSender());

            System.out.println(">>> Press ENTER to exit <<<");
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            watchSystem.terminate();
        }

    }

    @Override
    public void preStart() throws Exception {
        childActor = getContext().actorOf(ChildActor.props(), "childActor");
        getContext().watch(childActor);
    }

    @Override
    public void postStop() throws Exception {
        //Optional<ActorRef> childActor = getContext().findChild("childActor");
        getContext().unwatch(childActor);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .matchEquals("kill", s -> {
                    childActor.tell(PoisonPill.getInstance(), self());
                })
                .match(String.class, msg -> {
                    log.info("recv: " + msg);
                    if (!sender().equals(childActor)) {
                        log.info("send: " + msg);
                        childActor.tell(msg, self());
                    }
                })
                .build();
    }
}
