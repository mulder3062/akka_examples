package akka.example.logging;

import akka.actor.AbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class DummyActor extends AbstractActor {
    // Scala 예와 다르게 LoggingAdapter를 이용해서 로그를 남긴다.
    // ref: https://doc.akka.io/docs/akka/current/logging.html#dependency
    private final LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    @Override
    public void preStart() throws Exception {
        log.info("Actor start");
    }

    @Override
    public void postStop() throws Exception {
        log.info("Actor stop");
    }

    // 아무 일도 하지 않는다.
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(
                        String.class,
                        msg -> log.info("recv: {}", msg)
                )
                .build();
    }
}
