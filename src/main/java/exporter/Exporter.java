package exporter;

import org.json.JSONObject;

import java.time.LocalDateTime;

public class Exporter {
    private String name;
    private String exporter;

    private String namespace;

    private String host;
    private String user;
    private String password;

    private String port;
    private String DBname;
    private String topic;

    private String prefix;

    public Exporter() {

    }

    public Exporter(Exporter exporter) {
        this.name = exporter.name;
        this.exporter = exporter.exporter;
        this.host = exporter.host;
        this.user = exporter.user;
        this.password = exporter.password;
        this.namespace = exporter.namespace;
        this.topic = exporter.topic;
        this.prefix = exporter.prefix;
    }

    public Exporter(String name, String exporter) {
        super();
        this.name = name;
        this.exporter = exporter;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExporter() {
        return exporter;
    }

    public void setExporter(String exporter) {
        this.exporter = exporter;
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

    public void setPort(String port) {
        this.port = port;
    }
    public String getPrefix() {
        return prefix;
    }
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getDBname() {
        return DBname;
    }

    public void setDBname(String DBname) {
        this.DBname = DBname;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }


    public  void init() {
    }
    public void publishAttributes(String deviceid, JSONObject json) {

    }

    public void publishTelemetry(String name, String type, String deviceid, LocalDateTime localDateTime, double power) {

    }

    public void publishTelemetry(JSONObject json, String deviceId, String type) {

    }


    public void publishStatusMetric(String name, String type, LocalDateTime localDateTime, double status) {
    }
}
