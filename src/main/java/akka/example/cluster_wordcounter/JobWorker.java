package akka.example.cluster_wordcounter;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ReceiveTimeout;
import akka.actor.Terminated;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import scala.concurrent.duration.Duration;

//import java.time.Duration;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

class WorkLoadDepleted implements Serializable {}

class Task implements Serializable {
    private List<String> input;
    private ActorRef master;

    public Task(final List<String> input, final ActorRef master) {
        this.input = input;
        this.master = master;
    }

    public List<String> getInput() {
        return input;
    }

    public ActorRef getMaster() {
        return master;
    }
}

class Work implements Serializable {
    private String jobName;
    private ActorRef master;

    public Work(final String jobName, final ActorRef master) {
        this.jobName = jobName;
        this.master = master;
    }

    public String getJobName() {
        return jobName;
    }

    public ActorRef getMaster() {
        return master;
    }
}

public class JobWorker extends AbstractActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().system(), this);
    private int processed = 0;

    private Receive idle = receiveBuilder()
            .match(
                    Work.class,
                    work -> {
                        String jobName = work.getJobName();
                        ActorRef master = work.getMaster();
                        getContext().become(enlisted(jobName, master));

                        log.info("Enlisted, will start requesting work for job {}", jobName);
                        master.tell(new Enlist(self()), self());
                        master.tell(new NextTask(), self());
                        getContext().watch(master);
                        getContext().setReceiveTimeout(Duration.apply(30, TimeUnit.SECONDS));
                    }
            )
            .build();

    Receive enlisted(String jobName, ActorRef master) {
        return receiveBuilder()
                .match(
                        ReceiveTimeout.class,
                        receiveTimeout -> {
                            master.tell(new NextTask(), self());
                        }
                )
                .match(
                        Task.class,
                        task -> {
                            List<String> textPark = task.getInput();
                            ActorRef master1 = task.getMaster();
                            Map<String, Integer> countMap = processTask(textPark);
                            processed++;

                            master1.tell(new TaskResult(countMap), self());
                            master1.tell(new NextTask(), self());
                        }
                )
                .match(
                        WorkLoadDepleted.class,
                        workLoadDepleted -> {
                            log.info("Work load {} is depleted, retiring...", jobName);
                            getContext().setReceiveTimeout(Duration.Undefined());
                            getContext().become(retired(jobName));
                        }
                )
                .match(
                        Terminated.class,
                        terminated -> {
                            getContext().setReceiveTimeout(Duration.Undefined());
                            log.error("Master terminated that ran Job {}, stopping self.");
                            getContext().stop(self());
                        }
                )
                .build();
    }

    private Receive retired(String jobName) {
        return receiveBuilder()
                .match(
                        Terminated.class,
                        terminated -> {
                            log.error("Master terminated that ran Job {}, stopping self.", jobName);
                            getContext().stop(self());
                        }
                )
                .matchAny(
                        obj -> {
                            log.error("I'm retired.");
                        }
                )
                .build();
    }

    Map<String, Integer> processTask(List<String> textPart) {
        Map<String, Integer> map = new HashMap<>();

        textPart.stream().flatMap(s -> Arrays.stream(s.split("\\W+")))
            .forEach(word -> {
                if (map.containsKey(word)) {
                    if (word.equals("FAIL")) {
                        throw new RuntimeException("SIMULATED FAILURE!");
                    }

                    int cnt = map.get(word);
                    map.put(word, cnt + 1);

                }
            }
        );

        return map;
    }

    @Override
    public Receive createReceive() {
        return idle;
    }




}
