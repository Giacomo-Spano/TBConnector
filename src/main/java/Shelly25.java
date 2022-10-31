public class Shelly25 extends Device  {

    public Shelly25(Device device) {
        super(device);
    }
    public void receiveMessage(String topic, String message) {
        try {
            String publishTopic = Configuration.getThingsboardMQTTPublishTopic();//"v1/devices/me/telemetry";
            String publishMsg = "{\"power\":\"" + message + "\"}";

            TopicPublisher publisher = new TopicPublisher();
            publisher.createConnection(Configuration.getThingsboardMQTThost(), getToken(),"");
            publisher.publishMessage(publishTopic, publishMsg);
            publisher.closeConnection();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
