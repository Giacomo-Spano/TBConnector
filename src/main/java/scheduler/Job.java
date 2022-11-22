package scheduler;

public class Job {
    private String name;
    private String jobtype;
    private String command;
    private String crontrigger;
    private String topic;


    private String macaddress;

    private String ipaddress;


    public Job() {

    }


    public Job(Job job) {
        this.name = job.name;
        this.jobtype = job.jobtype;
        this.command = job.command;
        this.crontrigger = job.crontrigger;
        this.topic = job.topic;
        this.macaddress = job.macaddress;
        this.ipaddress = job.ipaddress;
    }

    public Job(String name, String jobtype, String command, String crontrigger, String topic, String MACAddress, String ipaddress) {
        super();
        this.name = name;
        this.jobtype = jobtype;
        this.command = command;
        this.crontrigger = crontrigger;
        this.topic = topic;
        this.macaddress = macaddress;
        this.ipaddress = ipaddress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJobType() {
        return jobtype;
    }

    public void setJobtype(String jobtype) {
        this.jobtype = jobtype;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getCrontrigger() {
        return crontrigger;
    }

    public void setCrontrigger(String crontrigger) {
        this.crontrigger = crontrigger;
    }


    public String getIpaddress() {
        return ipaddress;
    }

    public void setIpaddress(String ipaddress) {
        this.ipaddress = ipaddress;
    }

    public String getJobtype() {
        return jobtype;
    }

    public String getMacaddress() {
        return macaddress;
    }

    public void setMacaddress(String macaddress) {
        this.macaddress = macaddress;
    }
}
