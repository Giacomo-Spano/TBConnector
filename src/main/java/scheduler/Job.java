package scheduler;

public class Job {
    private String name;
    private String type;
    private String topic;
    private String start;
    private String shutdown;

    public Job() {

    }


    public Job(Job job) {
        this.name = job.name;
        this.start = job.start;
        this.shutdown = job.shutdown;
        this.type = job.type;
        this.topic = job.topic;
    }

    public Job(String name, String start, String shutdown) {
        super();
        this.name = name;
        this.start = start;
        this.shutdown = shutdown;
        this.type = type;
        this.topic = topic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getShutdown() {
        return shutdown;
    }

    public void setShutdown(String shutdown) {
        this.shutdown = shutdown;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
