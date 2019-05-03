package akka.example.tcp;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.io.IOException;

public class Main {


    public static void main(String[] args) {
        System.setProperty("java.net.preferIPv4Stack", "true");
        Config config = ConfigFactory.load();
        ActorSystem actorSystem = ActorSystem.create("default", config);
        ActorRef listener = actorSystem.actorOf(Props.create(ServerActor.class));
        listener.tell("start", null);

        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("Press Enter to exit...");
        }
    }
}
