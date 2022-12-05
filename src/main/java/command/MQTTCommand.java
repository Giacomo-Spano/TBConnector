package command;

import helper.MQTTTopicPublisher;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import scheduler.Schedule;

public class MQTTCommand extends Command {
    private static final Logger LOGGER = LogManager.getLogger(MQTTCommand.class);

    public void execute(JSONObject json)  {

        String topic = json.getString("topic");
        String message = json.getString("message");

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
