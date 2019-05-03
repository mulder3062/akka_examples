package akka.example.watch;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.PoisonPill;

import java.util.concurrent.TimeUnit;

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
