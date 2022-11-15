package exporter;

import org.json.JSONObject;

import java.time.LocalDateTime;

public class Shelly4PMPRO extends Device {
    public Shelly4PMPRO(Device device) {
        super(device);
    }
    public void receiveMessage(LocalDateTime localDateTime, String topic, String message) {
        //https://shelly-api-docs.shelly.cloud/gen2/ComponentsAndServices/Switch/

        JSONObject jo = new JSONObject(message);
        //Last measured instantaneous active power (in Watts) delivered to the attached load
        // (shown if applicable)Last measured instantaneous active power (in Watts) delivered
        // to the attached load (shown if applicable)
        String strpower = jo.get("apower").toString();
        double power = Double.valueOf(strpower);

        //Last measured voltage in Volts (shown if applicable)
        String voltage = jo.get("voltage").toString();

        // Last measured current in Amperes (shown if applicable)
        String current = jo.get("current").toString();

        //PostgresPublisher postgresPublisher = new PostgresPublisher();
        //postgresPublisher.publish(getId(),getName(),Float.valueOf(power));
        publishPowerMessage(localDateTime, power);


        /*try {
            String publishTopic = Configuration.getThingsboardMQTTPublishTopic();//"v1/devices/me/telemetry";
            String publishMsg = "{\"power\":\"" + power + "\"}";

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
