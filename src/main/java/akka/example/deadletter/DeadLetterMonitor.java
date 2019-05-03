package akka.example.deadletter;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.DeadLetter;
import akka.actor.Props;

public class DeadLetterMonitor extends AbstractActor {
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match( // DeadLetter 메시지가 도착하면 액터를 살리고 메시지를 재전송한다.
                    DeadLetter.class,
                    deadLetter -> {
                        System.out.println("전송 실패: " + deadLetter);
                        System.out.println("다시 Actor 재생성 및 재전송");
                        ActorRef ref = getContext().system().actorOf(Props.create(DummyActor.class));
                        ref.tell(deadLetter.message(), deadLetter.sender());
                    }
                )
                .build();
    }
}
