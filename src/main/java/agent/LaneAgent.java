package agent;

import device.LaneAgentDevice;
import helper.MQTTTopicPublisher;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import scheduler.JobScheduler;
import scheduler.Schedule;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class LaneAgent extends Agent { //
    private static final Logger LOGGER = LogManager.getLogger(LaneAgent.class);

    public LaneAgent(Agent agent) {
        super(agent);

    }


    public void init() {
        LaneAgentTopicSubscriber topicSubscriber = new LaneAgentTopicSubscriber();
        topicSubscriber.init(getName(),gethost(),getUser(),getPassword(),getTopic());

        sendPeriodicStatusUpdate();
    }

    public void sendPeriodicStatusUpdate() {
        TimerTask repeatedTask = new TimerTask() {
            public void run() {
                LOGGER.info("Task sendPeriodicStatusUpdate() performed on " + new Date());

                try {
                    String topic = getPublishtopic() + "/" + getName();
                    String publishMsg = "alive";

                    if (topic == null || publishMsg == null) {
                        LOGGER.error("topic null");
                        return;
                    }
                    if (publishMsg == null) {
                        LOGGER.error("publishMsg null");
                        return;
                    }
                    MQTTTopicPublisher publisher = new MQTTTopicPublisher();
                    String clientID = "HelloWorldPub_" + UUID.randomUUID().toString().substring(0,8);
                    if (publisher.createConnection(Schedule.getMQTThost(), clientID, Schedule.getMQTTuser(),Schedule.getMQTTpassword())) {

                    publisher.publishMessage(topic, publishMsg);
                    publisher.closeConnection();
                    LOGGER.info("Message alive sent");
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    LOGGER.error(e.getMessage());
                    e.printStackTrace();
                }
            }
        };
        Timer timer = new Timer("Timer");

        long delay = 1000L;
        long period = 1000L * 30L; // every 10 seconds
        timer.scheduleAtFixedRate(repeatedTask, delay, period);
    }

}
