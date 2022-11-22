package scheduler;

import agent.LaneAgentTopicSubscriber;
import helper.MQTTTopicPublisher;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class LaneCommandJob implements Job {
    private static final Logger LOGGER = LogManager.getLogger(LaneCommandJob.class);

    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();

        String topic = dataMap.getString("topic");
        String jobtype = dataMap.getString("jobtype");
        String command = dataMap.getString("command");

        LOGGER.info("execute job " + "topic: " + topic + "jobtype: " + jobtype + "command: " + command);

        try {
            String publishMsg = command;
            MQTTTopicPublisher publisher = new MQTTTopicPublisher();
            publisher.createConnection(Schedule.getMQTThost(), Schedule.getMQTTuser(),Schedule.getMQTTpassword());
            if (topic == null || publishMsg == null) {
                LOGGER.error("topic null");
                return;
            }
            if (publishMsg == null) {
                LOGGER.error("publishMsg null");
                return;
            }
            publisher.publishMessage(topic, publishMsg);
            publisher.closeConnection();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}