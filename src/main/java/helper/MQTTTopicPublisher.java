package helper;

import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;

/**
 * A Mqtt topic publisher 
 *
 */
public class MQTTTopicPublisher {
    private static final Logger LOGGER = LogManager.getLogger(MQTTTopicPublisher.class);
	static boolean isShutdown = false;
	MqttClient mqttClient;
    
    public boolean createConnection(String host, String clientId, String user, String password)  {
        LOGGER.info("TopicPublisher initializing...");

        try {
            // Create an Mqtt client
            mqttClient = new MqttClient(/*"tcp://" + */host, clientId);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            connOpts.setUserName(user);
            connOpts.setPassword(password.toCharArray());
            connOpts.setAutomaticReconnect(true);
            
            // Connect the client
            LOGGER.info("Connecting to " + host);
            mqttClient.connect(connOpts);

        } catch (MqttException me) {
            LOGGER.error("Exception:   " + me);
            LOGGER.error("Reason Code: " + me.getReasonCode());
            LOGGER.error("Message:     " + me.getMessage());
            if (me.getCause() != null) System.out.println("Cause:       " + me.getCause());
            me.printStackTrace();
            return false;
        }
        return true;
    }
    
    public void publishMessage(String topic, String content)  {
            
	            // Create a Mqtt message
	            //String content = "Hello world from MQTT!";
	            MqttMessage message = new MqttMessage(content.getBytes());
	            // Set the QoS on the Messages - 
	            // Here we are using QoS of 0 (equivalent to Direct Messaging in Solace)
	            message.setQos(0);

                LOGGER.info("Publishing message. topic: " + topic + " Message:" + message);
	            
	            // Publish the message
	            try {
					mqttClient.publish(topic, message);
				} catch (MqttPersistenceException e1) {
					// TODO Auto-generated catch block
                    LOGGER.error("Error publishing message: " + topic + ", " + message + "");
					e1.printStackTrace();
                    LOGGER.error(e1.toString());
				} catch (MqttException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
                    LOGGER.error(e1.toString());
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

            LOGGER.info("Messages published.");

    }

}