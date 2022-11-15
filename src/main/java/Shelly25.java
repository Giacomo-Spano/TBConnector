import java.time.LocalDateTime;

public class Shelly25 extends Device  {



    public Shelly25(Device device) {
        super(device);
    }
    public void receiveMessage(LocalDateTime localDateTime, String topic, String message) {

        String power = message;

        //PostgresPublisher postgresPublisher = new PostgresPublisher();
        //postgresPublisher.publish(getId(),getName(),Float.valueOf(message));
        publishPowerMessage(localDateTime, power);


        /*try {
            String publishTopic = Configuration.getThingsboardMQTTPublishTopic();//"v1/devices/me/telemetry";
            String publishMsg = "{\"power\":\"" + message + "\"}";

            TopicPublisher publisher = new TopicPublisher();
            publisher.createConnection(Configuration.getThingsboardMQTThost(), getToken(),"");
            publisher.publishMessage(publishTopic, publishMsg);
            publisher.closeConnection();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }*/
    }
}
