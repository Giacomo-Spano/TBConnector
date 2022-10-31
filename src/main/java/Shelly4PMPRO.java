import org.json.JSONObject;

public class Shelly4PMPRO extends Device {
    public Shelly4PMPRO(Device device) {
        super(device);
    }
    public void receiveMessage(String topic, String message) {
        //https://shelly-api-docs.shelly.cloud/gen2/ComponentsAndServices/Switch/

        JSONObject jo = new JSONObject(message);
        //Last measured instantaneous active power (in Watts) delivered to the attached load
        // (shown if applicable)Last measured instantaneous active power (in Watts) delivered
        // to the attached load (shown if applicable)
        String power = jo.get("apower").toString();

        //Last measured voltage in Volts (shown if applicable)
        String voltage = jo.get("voltage").toString();

        // Last measured current in Amperes (shown if applicable)
        String current = jo.get("current").toString();


        try {
            String publishTopic = Configuration.getThingsboardMQTTPublishTopic();//"v1/devices/me/telemetry";
            String publishMsg = "{\"power\":\"" + power + "\"}";

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
