package scheduler;

import helper.MQTTTopicPublisher;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class MQTTCommandJob implements Job {
    private static final Logger LOGGER = LogManager.getLogger(MQTTCommandJob.class);

    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();

        String topic = dataMap.getString("topic");
        String message = dataMap.getString("message");

        LOGGER.info("execute job " + "topic: " + topic + "message: " + message);

        try {
            MQTTTopicPublisher publisher = new MQTTTopicPublisher();
            publisher.createConnection(Schedule.getMQTThost(), Schedule.getMQTTuser(),Schedule.getMQTTpassword());
            if (topic == null || message == null) {
                LOGGER.error("topic or  message is null");
                return;
            }
            publisher.publishMessage(topic, message);
            publisher.closeConnection();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}