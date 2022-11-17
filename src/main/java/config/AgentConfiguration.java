package config;

public class AgentConfiguration {


    private String name;
    private String mqtthost;
    private String mqttuser;
    private String mqttpassword;

    public AgentConfiguration() {

    }

    public AgentConfiguration(AgentConfiguration agentConfiguration) {
        this.name = agentConfiguration.name;
        this.mqtthost = agentConfiguration.mqtthost;
        this.mqttuser = agentConfiguration.mqttuser;
        this.mqttpassword = agentConfiguration.mqttpassword;
    }

    public AgentConfiguration(String name, String mqtthost, String mqttuser, String mqttpassword) {
        super();
        this.name = name;
        this.mqtthost = mqtthost;
        this.mqttuser = mqttuser;
        this.mqttpassword = mqttpassword;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMqtthost() {
        return mqtthost;
    }

    public void setMqtthost(String mqtthost) {
        this.mqtthost = mqtthost;
    }

    public String getMqttuser() {
        return mqttuser;
    }

    public void setMqttuser(String mqttuser) {
        this.mqttuser = mqttuser;
    }

    public String getMqttpassword() {
        return mqttpassword;
    }

    public void setMqttpassword(String mqttpassword) {
        this.mqttpassword = mqttpassword;
    }

}
