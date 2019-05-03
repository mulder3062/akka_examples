package akka.example.cluster;

import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.cluster.Cluster;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class SeedMain {
    public static void main(String[] args) {
        Config seedConfig = ConfigFactory.load("seed"); // seed.conf
        ActorSystem seedSystem = ActorSystem.create("words", seedConfig);
        seedSystem.actorOf(Props.create(ClusterDomainEventListener.class), "ClusterDomainEventListener");

        // leave();
    }

    static void leave(ActorSystem system) {
        Cluster cluster = Cluster.get(system);
        cluster.leave(cluster.selfAddress());
    }

}
