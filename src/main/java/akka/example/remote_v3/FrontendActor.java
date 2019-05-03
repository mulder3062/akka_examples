package akka.example.remote_v3;

import akka.actor.*;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.util.Scanner;

/**
 * RemoteActor에게 'hello world' 메시지를 보내면 대문자로 변환된 'HELLO WORLD' 메시지를 받는다.
 */
public class FrontendActor extends AbstractActor {
    final static String config =
            "akka {\n" +
//            "  loglevel = \"INFO\"\n" +
            "  actor {\n" +
            "    provider = \"akka.remote.RemoteActorRefProvider\"\n" +
            "  }\n" +
            "  remote {\n" +
            "    enabled-transports = [\"akka.remote.netty.tcp\"]\n" +
            "    netty.tcp {\n" +
            "      hostname = \"127.0.0.1\"\n" +
            "      port = 0\n" +
            "    }\n" +
            "    log-sent-messages = on\n" +
            "    log-received-messages = on\n" +
            "  }\n" +
            "}";

    final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    public static void main(String[] args) {
        Config config = ConfigFactory.parseString(FrontendActor.config);
        ActorSystem localSystem = ActorSystem.create("FrontendSystem", config);
        ActorRef frontend = localSystem.actorOf(Props.create(FrontendActor.class), "frontend");
        ActorRef lookup = localSystem.actorOf(Props.create(RemoteLookupProxy.class, "akka.tcp://BackendSystem@127.0.0.1:5150/user/backend"), "lookup");

        try {
            System.out.println(">>> exit to exit <<<");
            Scanner scanner = new Scanner(System.in);

            while(true) {
                String line = scanner.next();
                if ("exit".equals(line)) {
                    break;
                } else {
                    lookup.tell(line, frontend);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            localSystem.terminate();
        }
    }

    @Override
    public void preStart() throws Exception {
        /*
            로컬 액터는 "akka://my-sys/user/service-a/worker1" 형태의 경로를 갖는다.
            원격 액터는 "akka.tcp://my-sys@host.example.com:5678/user/service-b"와 같이 전형적인 URI 형식을 따른다.
                [형식] {프로토콜}://{액터시스템}@{서버주소}:{포트}/{감독자}/{액터명}
                - 프로토콜: akka.tcp, akka.udp 등이 될 수 있다.
                - 감독자: 사용자가 생성한 액터는 /user (감독자)의 자식 액터로 등록된다. /system은 akka에서 사용하는 내장된 액터이다.
         */

//        ActorRef remoteActor = getContext().actorOf(Props.create(BackendActor.class), "remote");
//        log.info("이것은 원격액터: {}", remoteActor);
//        remoteActor.tell("hello world", self());
        ActorSelection proxy = getContext().actorSelection("/user/lookup");
//        proxy.tell("hello world", self());
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(
                        String.class,
                        msg -> log.info("원격지에서 메시지를 수신:{}", msg)
                )
                .build();
    }
}
