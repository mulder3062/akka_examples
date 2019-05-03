package akka.example.watch;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.PoisonPill;

import java.util.concurrent.TimeUnit;

/**
 * watch를 통해 액터의 생명주기를 감시할 수 있다.
 * 부모 액터의 경우 자식의 Supervisor가 되므로 자동으로 감시가 된다.
 */
public class Main {
    public static void main(String[] args) {
        ActorSystem sys = ActorSystem.create("system");

        ActorRef target = sys.actorOf(TargetActor.props());
        sys.actorOf(WatchActor.props(target));

        try {
            TimeUnit.SECONDS.sleep(1);
            target.tell(PoisonPill.getInstance(), ActorRef.noSender());
            System.out.println(">>> Press ENTER to exit <<<");
            System.in.read();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sys.terminate();
        }
    }
}
