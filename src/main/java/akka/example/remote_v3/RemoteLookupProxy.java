package akka.example.remote_v3;

import akka.actor.*;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class RemoteLookupProxy extends AbstractActor {
    final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    private final String path;

    private Receive identify;

    public RemoteLookupProxy(String path) {
        this.path = path;
        this.identify = receiveBuilder()
                .match(
                        ActorIdentity.class,
                        id -> !id.getActorRef().isPresent(),
                        id -> {
                            log.error("Remote actor with path {} is not available.", path);
                        }
                )
                .match(
                        ActorIdentity.class,
                        id -> id.getActorRef().isPresent(),
                        id -> {
                            log.error("Received actor identity");
                            ActorRef ref = id.getActorRef().get();

                            // become을 통해 Receive를 변경한다.
                            getContext().become(active(ref));
                            getContext().watch(ref);
                        }
                )
                .match(
                        ReceiveTimeout.class,
                        t -> {
                            log.error("request timeout");
                            sendIdentifyRequest();
                            TimeUnit.SECONDS.sleep(10);
                        }
                )
                .matchAny(
                        obj -> {
                            log.error("Ignoring message {}, not ready yet.", obj);
                        }
                )
                .build();
    }

    @Override
    public void preStart() throws Exception {
        getContext().setReceiveTimeout(Duration.ofSeconds(13));
        sendIdentifyRequest();
    }



    private void sendIdentifyRequest() {
        ActorSelection selection = getContext().actorSelection(path);
        selection.tell(new Identify(path), self());
    }

    private Receive active(ActorRef ref) {
        return receiveBuilder()
                .match(
                        Terminated.class,
                        t -> {
                            if (t.actor().equals(ref)) {
                                log.info("Actor {} terminated", t.actor());

                                // Backend가 중지되었으므로 become을 통해 식별상태로 변경한다.
                                getContext().become(identify);
                                log.info("switching to identify state");
                                getContext().setReceiveTimeout(Duration.ofSeconds(3));
                                sendIdentifyRequest();
                            }
                        }
                )
//                .match(
//                        ReceiveTimeout.class,
//                        t -> {
//                            log.error("request timeout");
//                            sendIdentifyRequest();
//                        }
//                )
                .match(
                        String.class,
                        msg -> {
                            ref.forward(msg, getContext());
                        }
                )
                .matchAny(
                        msg -> {
                            log.info("Receive any: {}", msg);
                            //ref.forward(msg, getContext());

                        }
                )
                .build();
    }

    @Override
    public Receive createReceive() {
        return identify;
    }
}
