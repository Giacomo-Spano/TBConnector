
import java.io.IOException;
import java.util.UUID;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;

/**
 * A Mqtt topic publisher 
 *
 */
public class TopicPublisher {
	
	static boolean isShutdown = false;
	MqttClient mqttClient;
    
    public void createConnection(String host, String user, String password)  {
        System.out.println("TopicPublisher initializing...");

        try {
            // Create an Mqtt client
            mqttClient = new MqttClient("tcp://" + host, "HelloWorldPub_" + UUID.randomUUID().toString().substring(0,8));
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            connOpts.setUserName(user);
            
            // Connect the client
            System.out.println("Connecting to " + host);
            mqttClient.connect(connOpts);
            
        } catch (MqttException me) {
            System.out.println("Exception:   " + me);
            System.out.println("Reason Code: " + me.getReasonCode());
            System.out.println("Message:     " + me.getMessage());
            if (me.getCause() != null) System.out.println("Cause:       " + me.getCause());
            me.printStackTrace();
        }
    }
    
    public void publishMessage(String topic, String content)  {
            
	            // Create a Mqtt message
	            //String content = "Hello world from MQTT!";
	            MqttMessage message = new MqttMessage(content.getBytes());
	            // Set the QoS on the Messages - 
	            // Here we are using QoS of 0 (equivalent to Direct Messaging in Solace)
	            message.setQos(0);
	            
	            System.out.println("Publishing message. topic: " + topic + " Mesessage:" + message);
	            
	            // Publish the message
	            try {
					mqttClient.publish(topic, message);
				} catch (MqttPersistenceException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					System.out.println(e1.toString());
				} catch (MqttException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					System.out.println(e1.toString());
				}	            
    }
    
    public void closeConnection()  {
            // Disconnect the client
            try {
				mqttClient.disconnect();
			} catch (MqttException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
            System.out.println("Messages published.");

    }

}