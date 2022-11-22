package agent;


import importer.Importer;

public class Agent {
    private String name;
    private String type;
    private String host;
    private String user;
    private String password;
    private String port;
    private String topic;
    private String publishtopic;

    public Agent() {

    }
    public Agent(Agent agent) {
        this.name = agent.name;
        this.host = agent.host;
        this.user = agent.user;
        this.password = agent.password;
        this.topic = agent.topic;
        this.publishtopic = agent.publishtopic;
    }

    public String gethost() {
        return host;
    }

    public void setThost(String host) {
        this.host = host;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String password) {
        this.port = port;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getPublishtopic() {
        return publishtopic;
    }

    public void setPublishtopic(String publishtopic) {
        this.publishtopic = publishtopic;
    }

    public String getName() {
        return name;
    }

    public void setTypr(String name) {
        this.name = name;
    }
    public String geType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void init() {
    }

}
