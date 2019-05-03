package akka.example.cluster;

import akka.actor.AbstractActor;
import akka.cluster.Cluster;
import akka.cluster.ClusterEvent;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class ClusterDomainEventListener extends AbstractActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    public ClusterDomainEventListener() {
        // 액터 생성시 클러스터 도메인 이벤트에 가입한다.
        Cluster.get(getContext().system()).subscribe(self(), ClusterEvent.ClusterDomainEvent.class);
    }

    @Override
    public void postStop() throws Exception {
        Cluster.get(getContext().system()).unsubscribe(self());
        super.postStop();
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(
                        ClusterEvent.MemberUp.class,
                        event -> log.info("Member UP: {}", event.member().address())
                )
                .match(
                        ClusterEvent.MemberExited.class,
                        event -> log.info("Member EXITED: {}", event.member().address())
                )
                .match(
                        ClusterEvent.MemberRemoved.class,
                        event -> log.info("Member REMOVED: {}", event.member().address())
                )
                .match(
                        ClusterEvent.UnreachableMember.class,
                        event -> log.info("Member UNREACHABLE: {}", event.member().address())
                )
                .match(
                        ClusterEvent.ReachableMember.class,
                        event -> log.info("Member REACHABLE: {}", event.member().address())
                )
                .match(
                        ClusterEvent.CurrentClusterState.class,
                        event -> event.getMembers().forEach(member ->
                                log.info("Cluster Member State: member={}, state={}", member.address(), member.status()))
                )
                .build();
    }
}
