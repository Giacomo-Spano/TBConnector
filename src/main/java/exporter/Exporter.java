package exporter;

import java.time.LocalDateTime;

public class Exporter {
    private String name;
    private String exporter;

    private String host;
    private String user;
    private String password;

    private String port;
    private String DBname;
    private String topic;

    public Exporter() {

    }

    public Exporter(Exporter exporter) {
        this.name = exporter.name;
        this.exporter = exporter.exporter;
        this.host = exporter.host;
        this.user = exporter.user;
        this.password = exporter.password;

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


    public  void init() {
    }

    public void publishPowerMetric(String name, String type, String token, LocalDateTime localDateTime, double power) {

    }

    public void publishStatusMetric(String name, String type, LocalDateTime localDateTime, double status) {
    }
}
