package akka.example.logging;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

public class Main {
    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("logging");
        ActorRef dummyActor = system.actorOf(Props.create(DummyActor.class), "dummyActor");
        dummyActor.tell("hello world", ActorRef.noSender());
    }
}
