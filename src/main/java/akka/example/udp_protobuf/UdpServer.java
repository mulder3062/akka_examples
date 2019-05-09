package akka.example.udp_protobuf;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.example.udp_protobuf.proto.Message;
import akka.io.Udp;
import akka.io.UdpMessage;
import akka.util.ByteString;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.net.InetSocketAddress;

/**
 * Akka Udp Server
 *
 * 클라이언트측 코드와 거의 동일하다.
 * 수신된 메시지를 대문자로 변경해서 다시 보낸다.
 */
public class UdpServer extends AbstractActor {
    private final LoggingAdapter log = Logging.getLogger(context().system(), this);
    private final InetSocketAddress localAddr;

    public static Props props(InetSocketAddress localAddr) {
        return Props.create(UdpServer.class, localAddr);
    }

    public UdpServer(InetSocketAddress localAddr) {
        this.localAddr = localAddr;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .matchEquals( // bind 메시지를 받으면 UDP 소켓 bind
                        "bind",
                        s -> {
                            log.info("Binding {} ... ", localAddr);
                            final ActorRef udp = Udp.get(getContext().system()).getManager();
                            udp.tell(UdpMessage.bind(getSelf(), localAddr), getSelf());

                        }
                )
                .match( // bind 성공하면 echo 서비스를 할 수 있도록 Recevive 교체
                        Udp.Bound.class,
                        bound -> {
                            log.info("Socket open");
                            getContext().become(ready(getSender()));
                        })
                .matchAny( // Bound 메시지가 아니면 실패로 간주하고 종료
                        obj -> {
                            log.info("Bind failed: {}", obj);
                            getContext().stop(self());
                        }
                )
                .build();
    }

    private Receive ready(ActorRef sender) {
        return receiveBuilder()
                .match( // Client 에서 수신받은 메시지 echo 처리
                        Udp.Received.class,
                        r -> {
                            Message message = Message.parseFrom(r.data().toArray());
                            log.info("RECV: {}", message);
                            log.info("SEND: {}", message);
                            ByteString byteString = ByteString.fromArray(message.toByteArray());
                            sender.tell(UdpMessage.send(byteString, r.sender()), getSelf());
                        })
                .matchEquals(
                        UdpMessage.unbind(),
                        message -> {
                            log.info("Socket close");
                        })
                .match(
                        Udp.Unbound.class,
                        message -> {
                            System.out.println("Actor stop");
                            getContext().stop(self());
                        })
                .build();
    }

    public static void main(String[] args) {
        final String configStr =
                "akka {\n" +
                "   loglevel = \"DEBUG\"\n" +
                "   actor {\n" +
                "       allow-java-serialization=off\n"+
                "       serializers {\n" +
                "           java = \"akka.serialization.JavaSerializer\"\n" +
                "           proto = \"akka.remote.serialization.ProtobufSerializer\"\n" +
                "       }\n" +
                "       serialization-bindings {\n" +
                "           \"java.lang.String\" = proto\n" +
                "       }\n" +
                "   }\n" +
                "}";
        Config config = ConfigFactory.parseString(configStr);

        ActorSystem system = ActorSystem.create("serverSystem", config);
        InetSocketAddress local = new InetSocketAddress("127.0.0.1", 20020);
        ActorRef server = system.actorOf(UdpServer.props(local), "server");
        server.tell("bind", ActorRef.noSender());
    }
}
