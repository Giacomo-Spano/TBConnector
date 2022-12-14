package scheduler;//package com.baeldung.jackson.yaml; XXXXXX

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Schedule {

    // schedule plan
    private static String name;
    private static String mqtthost;
    private static String mqttuser;
    private static String mqttpassword;
    // device.Devices settings
    List<Job> jobs;

    public Schedule() {

    }

    public Schedule(String name, String mqtthost, String mqttuser, String mqttpassword, List<Job> jobs) {
        super();
        this.name = name;
        this.jobs = jobs;
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

    public static String getMQTThost() {
        return mqtthost;
    }

    public void setMQTThost(String mqtthost) {
        this.mqtthost = mqtthost;
    }

    public static String getMQTTuser() {
        return mqttuser;
    }

    public void setMQTTuser(String mqttuser) {
        this.mqttuser = mqttuser;
    }

    public static String getMQTTpassword() {
        return mqttpassword;
    }

    public void setMQTTpassword(String MQTTpassword) {
        this.mqttpassword = MQTTpassword;
    }

    public List<Job> getJobs() {
        if (jobs == null) {
            jobs = new ArrayList<>();
        }
        return jobs;
    }

    public void setJobs(List<Job> jobs) {
        if (jobs == null) {
            jobs = new ArrayList<>();
        }

        this.jobs = new ArrayList<>();
        Iterator<Job> jobIterator = jobs.iterator();
        while (jobIterator.hasNext()) {
            Job job = jobIterator.next();
            Job newjob = new Job(job);
            this.jobs.add(newjob);
        }
    }

    @Override
    public String toString() {
        return "name: " + name + ", mqtthost: " + mqtthost + ", mqttuser: " + mqttuser + ", mqttpassword" + mqttpassword;
    }

}