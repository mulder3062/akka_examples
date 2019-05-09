package akka.example.cluster_wordcounter;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.cluster.Cluster;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.util.Arrays;
import java.util.List;

public class CWMain {
    public static void main(String[] args) {
        String role = System.getProperty("role", "cl_seed");
        Config config = ConfigFactory.load(role);
        ActorSystem system = ActorSystem.create("words", config);

        System.out.println("Starting node with roles: " + Cluster.get(system).selfRoles());

        // 노드가 마스터 역할일 때만 JobReceptionist를 시작한다.
        if (system.settings().config().getStringList("akka.cluster.roles").contains("master")) {
            // 맴버가 올라오는 경우 실행할 코드 블록을 등록
            Cluster.get(system).registerOnMemberUp(() -> {
                // JobReceptionist는 클러스터가 실행 중이고 최소 두개 이상의 작업자 역할 노드가 있을 떄만 만들어진다.
                // 참조) master.conf의 worker.min-nr-of-members = 2
                ActorRef receptionist = system.actorOf(Props.create(JobReceptionist.class), "receptionist");
                System.out.println("Master node is ready.");

                List<String> text = Arrays.asList(
                        "this is a test",
                        "of some very naive word counting",
                        "but what can you say",
                        "it is what it is");


                // Receptionist는 text를 받아 JobMaster에게 요청한다.
//                receptionist.tell(new JobRequest("test1", text), ActorRef.noSender());
                receptionist.tell(new JobRequest("test1", text), receptionist);

                // 클러스터 맴버의 상태를 로깅한다.
                system.actorOf(Props.create(ClusterDomainEventListener.class), "cluster-listener");
            });
        }
    }
}
