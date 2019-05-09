package akka.example.cluster_wordcounter;

import akka.actor.*;
import akka.cluster.routing.ClusterRouterPool;
import akka.cluster.routing.ClusterRouterPoolSettings;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.routing.BroadcastPool;
import scala.concurrent.duration.Duration;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.TimeUnit;

class StartJob implements Serializable {
    private String name;
    private List<String> text;

    public StartJob(final String name, final List<String> text) {
        this.name = name;
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public List<String> getText() {
        return text;
    }
}

class Enlist implements Serializable {
    private final ActorRef worker;

    public Enlist(final ActorRef worker) {
        this.worker = worker;
    }

    public ActorRef getWorker() {
        return worker;
    }
}

class NextTask implements Serializable { }

class TaskResult implements Serializable {
    private Map<String, Integer> map;

    public TaskResult(Map<String, Integer> map) {
        this.map = map;
    }

    public Map<String, Integer> getMap() {
        return map;
    }
}

class MergeResult implements Serializable { }

class Start implements Serializable {}



/*
 작업 마스터는 상태를 관리한다.

 become을 통해 상태를 제어한다. (상태: 아이들링 / 작업스케줄링 / 종료중)
  */
public class JobMaster extends AbstractActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    private List<List<String>> textParts = new ArrayList<>();
    private List<Map<String, Integer>> intermediateResult = new ArrayList<>();
    private int workGiven = 0;
    private int workReceived = 0;
    private Set<ActorRef> workers = new HashSet<>();
    private ActorRef router = createWorkerRouter();
    private Receive idle = receiveBuilder()
            .match( // Router를 사용해 Work 메시지를 Broadcast한다.
                    StartJob.class,
                    job -> {
                        // 10개씩 그룹으로 묶는다.
                        List<String> subLst = new ArrayList<>();
                        for (int i=0; i<job.getText().size(); i++) {
                            if (i != 0 && i % 10 == 0) {
                                textParts.add(subLst);
                                subLst = new ArrayList<>();
                            } else if (i+1 == job.getText().size()) {
                                textParts.add(subLst);
                            }
                            subLst.add(job.getText().get(i));
                        }

                        Cancellable cancellable = getContext().system().scheduler().schedule(
                                Duration.apply(0, TimeUnit.MILLISECONDS), // 초기 지연 시간
                                Duration.apply(1000, TimeUnit.MILLISECONDS), // 간격
                                router, // Receiver
                                new Work(job.getName(), self()), // 메시지
                                getContext().system().dispatcher(), // Executor
                                null // sender
                        );
                        getContext().setReceiveTimeout(Duration.apply(60, TimeUnit.SECONDS));
                        getContext().become(working(job.getName(), sender(), cancellable));
                    }
            )
            .build();


    public static Props props() {
        return Props.create(JobMaster.class);
    }

    private Receive working(String jobName, ActorRef receptionist, Cancellable cancellable) {
        return receiveBuilder()
                .match( // 참여한 작업자를 감시하며 목록에 있는 작업자를 추적한다.
                        Enlist.class,
                        enlist -> {
                            getContext().watch(enlist.getWorker());
                            workers.add(enlist.getWorker());
                        }
                )
                .match( // NextTask 요청을 받으면 Task 메시지를 보내준다.
                        NextTask.class,
                        task -> {
                            if (textParts.isEmpty()) {
                                sender().tell(new WorkLoadDepleted(), self());
                            } else {
                                List<String> head = textParts.get(0);
                                sender().tell(new Task(head, self()), self());
                                workGiven++;
                                textParts.remove(0);
                            }
                        }

                )
                .match(
                        TaskResult.class,
                        t -> {
                            intermediateResult.add(t.getMap());
                            workReceived++;

                            if (textParts.isEmpty() && workGiven == workReceived) {
                                cancellable.cancel();
                                getContext().become(finishing(jobName, receptionist, workers));
                                self().tell(new MergeResult(), self());
                            }

                        }
                )
                .match(
                        ReceiveTimeout.class,
                        receiveTimeout -> {
                            if (workers.isEmpty()) {
                                log.info("No workers responded in time. Cancelling job {}", jobName);
                                getContext().stop(self());
                            } else {
                                getContext().setReceiveTimeout(Duration.Undefined());
                            }
                        }
                )
                .match(
                        Terminated.class,
                        terminated -> {
                            log.info("Worker {} got terminated. Cancelling {}.", terminated.actor(), jobName);
                            getContext().stop(self());
                        }
                )
                .build();
    }

    private Receive finishing(String jobName, ActorRef receptionist, Set<ActorRef> workers) {
        return receiveBuilder()
                .match(
                        MergeResult.class,
                        result -> {
                            Map<String, Integer> mergedMap = merge();
                            workers.forEach(worker -> {
                                getContext().stop(worker);
                            });

                            receptionist.tell(new WordCount(jobName, mergedMap), self());
                        }
                )
                .match(
                        Terminated.class,
                        terminated -> {
                            ActorRef worker = terminated.actor();
                            log.info("Job {} is finishing. Worker {} is stopped",
                                    jobName, worker.path().name());
                        }
                )
                .build();
    }

    // 결과 머지
    private Map<String, Integer> merge() {
        Map<String, Integer> mergeMap = new HashMap<>();

        intermediateResult.forEach(map -> {

        });


        return mergeMap;
    }

    @Override
    public SupervisorStrategy supervisorStrategy() {
        return SupervisorStrategy.stoppingStrategy();
    }

    @Override
    public Receive createReceive() {
        return idle;
    }

    // 작업자를 위한 라우터를 생성한다.
    private ActorRef createWorkerRouter() {
        Set<String> useRole = new LinkedHashSet<>();
        useRole.add("worker");

        ClusterRouterPoolSettings settings = new ClusterRouterPoolSettings(
                100, // 클러스터 안에 있는 작업자의 최대 갯수
                20, // 노드별 최대 작업자 수
                false, // 로컬 라우터를 만들지 않고 다른 노드에만 Worker를 만든다.
                useRole // 이 역할을 하는 노드로만 메시지를 전달한다.
        );

        ClusterRouterPool clusterRouterPool = new ClusterRouterPool(new BroadcastPool(10), settings);
        Props workerRouter = clusterRouterPool.props(Props.create(JobWorker.class));
        return getContext().actorOf(workerRouter, "worker-router");
    }
}
