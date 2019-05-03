package akka.example.remote_v2;

import akka.actor.AbstractActor;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

/**
 * 5150 포트로 메시지를 기다렸다. 메시지를 대문자로 변환해서 응답한다.
 */
public class RemoteActor extends AbstractActor {
    final static String config =
            "akka {\n" +
            "  loglevel = \"INFO\"\n" +
            "  actor {\n" +
            "    provider = \"akka.remote.RemoteActorRefProvider\"\n" +
            "  }\n" +
            "  remote {\n" +
            "    enabled-transports = [\"akka.remote.netty.tcp\"]\n" +
            "    netty.tcp {\n" +
            "      hostname = \"127.0.0.1\"\n" +
            "      port = 5150\n" +
            "    }\n" +
            "    log-sent-messages = on\n" +
            "    log-received-messages = on\n" +
            "  }\n" +
            "}";
    final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    public static void main(String[] args) {
        Config config = ConfigFactory.parseString(RemoteActor.config);
        ActorSystem system = ActorSystem.create("RemoteSystem", config);
        system.actorOf(Props.create(RemoteActor.class), "remote");
    }

    @Override
    public void preStart() throws Exception {
        log.info("RemoteActor 시작");
    }

    @Override
    public void postStop() throws Exception {
        log.info("RemoteActor 중지");
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(
                        String.class,
                        msg -> {
                            log.info("원격 메시지 수신: {}, 송신액터:{}", msg, sender());
                            sender().tell(msg.toUpperCase(), self());
                        }
                )
                .matchAny(obj -> log.info("알수없는 메시지를 수신: {}", obj)
                )
                .build();
    }
}
