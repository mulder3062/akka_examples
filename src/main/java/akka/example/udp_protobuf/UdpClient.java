package akka.example.udp_protobuf;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.example.udp_protobuf.proto.Content;
import akka.example.udp_protobuf.proto.Message;
import akka.io.Udp;
import akka.io.UdpMessage;
import akka.util.ByteString;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

/**
 * Akka Udp Client
 *
 * 서버측 코드와 거의 동일하다.
 * 서버로 텍스트를 보내고 메시지가 전달되면 로그에 출력한다.
 */
public class UdpClient extends AbstractActor {
    private final LoggingAdapter log = Logging.getLogger(context().system(), this);
    private final InetSocketAddress localAddr;
    private final InetSocketAddress remoteAddr;

    public static Props props(InetSocketAddress localAddr, InetSocketAddress remoteAddr) {
        return Props.create(UdpClient.class, localAddr, remoteAddr);
    }

    public UdpClient(InetSocketAddress localAddr, InetSocketAddress remoteAddr) {
        this.localAddr = localAddr;
        this.remoteAddr = remoteAddr;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .matchEquals( // bind 메시지를 받으면 UDP 소켓을 bind
                        "bind",
                        message -> {
                            log.info("Binding {} ...", localAddr);
                            final ActorRef udp = Udp.get(getContext().system()).getManager();
                            udp.tell(UdpMessage.bind(getSelf(), localAddr), getSelf());
                        })
                .match( // bind 성공하면 echo 서비스를 할 수 있도록 Recevive 교체
                        Udp.Bound.class,
                        bound -> {
                            log.info("Socket open");
                            getContext().become(ready(getSender()));
                            Message message = Message.newBuilder()
                                    .setTitle("Title")
                                    .addContents(Content.newBuilder().setBody("content1").build())
                                    .addContents(Content.newBuilder().setBody("content2").build())
                                    .addContents(Content.newBuilder().setBody("content3").build())
                                    .setContentSize(3)
                                    .build();
                            self().tell(message, self());
                        })
                .build();
    }

    private Receive ready(ActorRef sender) {
        return receiveBuilder()
                .match( // Actor가 텍스트 메시지를 받으면 UDP로 메시지를 서버에 전달
                        Message.class,
                        message -> {
                            log.info("SEND: {}", message);
                            ByteString byteString = ByteString.fromArray(message.toByteArray());
                            sender.tell(UdpMessage.send(byteString, remoteAddr), getSelf());
                        })
                .match( // 서버에서 수신받은 메시지 처리
                        Udp.Received.class,
                        received -> {
                            Message message = Message.parseFrom(received.data().toArray());
                            log.info("RECV: {}", message);
                        })
                .matchEquals(
                        UdpMessage.unbind(),
                        message -> {
                            log.info("Socket close");
                        })
                .match(
                        Udp.Unbound.class,
                        message -> {
                            log.info("Actor stop");
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

        ActorSystem system = ActorSystem.create("clientSystem", config);
        InetSocketAddress local = new InetSocketAddress("127.0.0.1", 20010);
        InetSocketAddress remote = new InetSocketAddress("127.0.0.1", 20020);
        ActorRef client = system.actorOf(UdpClient.props(local, remote), "client");
        client.tell("bind", ActorRef.noSender());



    }
}
