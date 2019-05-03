package akka.example.deadletter;

import akka.actor.AbstractActor;

public class DummyActor extends AbstractActor {
    @Override
    public void preStart() throws Exception {
        System.out.println("DummyActor 시작");
    }

    @Override
    public void postStop() throws Exception {
        System.out.println("DummyActor 중지");
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(
                        String.class,
                        msg -> {
                            System.out.printf("메시지 수신: %s, sender: %s\n", msg, sender());
                            sender().tell(msg.toUpperCase(), self());
                        }
                )
                .matchAny(obj -> System.out.printf("알수없는 메시지를 수신: %s", obj)
                )
                .build();
    }
}
