package scheduler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import CommandReceiver.command.MQTTCommand;

public class MQTTCommandJob implements Job {
    private static final Logger LOGGER = LogManager.getLogger(MQTTCommandJob.class);

    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();

        String topic = dataMap.getString("topic");
        String message = dataMap.getString("message");

        LOGGER.info("execute job " + "topic: " + topic + "message: " + message);

        JSONObject json = new JSONObject();
        json.put("topic", topic);
        json.put("message", message);

        MQTTCommand command = new MQTTCommand();
        command.execute(json);
    }
}