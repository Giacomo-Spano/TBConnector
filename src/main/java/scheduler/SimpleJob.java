package scheduler;

import exporter.Configuration;
import exporter.TopicPublisher;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class SimpleJob implements Job {
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();

        String topic = dataMap.getString("topic");
        String type = dataMap.getString("type");
        String command = dataMap.getString("command");

        try {
            String publishMsg = command;

            TopicPublisher publisher = new TopicPublisher();
            publisher.createConnection(Schedule.getMQTThost(), Schedule.getMQTTuser(),Schedule.getMQTTpassword());
            publisher.publishMessage(topic, publishMsg);
            publisher.closeConnection();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        //System.out.println("Job says: " + jobSays + ", and val is: " + myFloatValue);
    }
}