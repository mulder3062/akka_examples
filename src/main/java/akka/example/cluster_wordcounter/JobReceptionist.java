package akka.example.cluster_wordcounter;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Terminated;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import java.io.Serializable;
import java.util.*;

class Job implements Serializable {
    private String name;
    private List<String> text;
    private ActorRef respondTo;
    private ActorRef jobMaster;

    public Job(String name, List<String> text, ActorRef respondTo, ActorRef jobMaster) {
        this.name = name;
        this.text = text;
        this.respondTo = respondTo;
        this.jobMaster = jobMaster;
    }

    public String getName() {
        return name;
    }

    public List<String> getText() {
        return text;
    }

    public ActorRef getRespondTo() {
        return respondTo;
    }

    public ActorRef getJobMaster() {
        return jobMaster;
    }
}

class WordCount implements Serializable {
    private String name;
    private Map<String, Integer> map;

    public WordCount(String name, Map<String, Integer> map) {
        this.name = name;
        this.map = map;
    }

    public String getName() {
        return name;
    }

    public Map<String, Integer> getMap() {
        return map;
    }
}

class JobSuccess implements Serializable {
    private String name;
    private Map<String, Integer> map;

    public JobSuccess(String name, Map<String, Integer> map) {
        this.name = name;
        this.map = map;
    }

    public String getName() {
        return name;
    }

    public Map<String, Integer> getMap() {
        return map;
    }
}

class JobRequest implements Serializable {
    private String name;
    private List<String> text;

    public JobRequest(String name, List<String> text) {
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

public class JobReceptionist extends AbstractActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().system(), this);
    private Set<Job> jobs = new HashSet<>();
    private Map<String, Integer> retries = new HashMap<>();
    private final static int MAX_RETIRES = 3;

    public JobReceptionist() {

    }

    // 작업 마스터를 만든다.
    public ActorRef createMaster(String name) {
        return getContext().actorOf(JobMaster.props(), name);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(
                    JobRequest.class,
                        req -> {
                            String name = req.getName();
                            List<String> text = req.getText();

                            log.info("Received job {}", name);
                            String masterName = "master-" + name;
                            ActorRef jobMaster = createMaster(masterName);
                            Job job = new Job(name, text, sender(), jobMaster);
                            jobs.add(job);

                            jobMaster.tell(new StartJob(name, text), self());
                            getContext().watch(jobMaster);

                        }
                )
                .match(
                    WordCount.class,
                        wordCount -> {
                            String name = wordCount.getName();
                            Map<String, Integer> map = wordCount.getMap();

                            log.info("Job {} complete.", name);
                            log.info("result: {}", map);

                            Set<Job> removeJobs = new HashSet<>();

                            jobs.forEach(job -> {
                                if (job.equals(name)) {
                                    job.getRespondTo().tell(new JobSuccess(name, map), self());
                                    getContext().stop(job.getJobMaster());
                                    removeJobs.add(job);
                                }
                            });

                            removeJobs.forEach(job -> {
                                jobs.remove(job);
                            });
                        }
                )
                .match(
                        JobSuccess.class,
                        jobSuccess -> {
                            log.info(">>>> SUCCESS: job={}, result={}", jobSuccess.getName(), jobSuccess.getMap());
                        }
                )
                .match(
                        Terminated.class,
                        terminated -> {
                            ActorRef master = terminated.actor();

                            jobs.forEach(job -> {
                                if (job.getJobMaster() == master) {
                                    log.error("Job Master {} terminated before finishing job.");
                                    log.error("Job {} failed.", job.getName());
                                    Integer nrOfRetries = retries.getOrDefault(job.getName(), 0);

                                    if (MAX_RETIRES > nrOfRetries) {
                                        if (nrOfRetries == MAX_RETIRES - 1) {
                                            List<String> newText = new ArrayList<>();
                                            for (String s : job.getText()) {
                                                if (s.indexOf("FAIL") == -1) {
                                                    newText.add(s);
                                                }
                                            }

                                            self().tell(new JobRequest(job.getName(), newText), job.getRespondTo());
                                        }

                                        retries.put(job.getName(), retries.getOrDefault(job.getName(), 0) + 1);
                                    }
                                }
                            });
                        }
                )
                .build();
    }
}
