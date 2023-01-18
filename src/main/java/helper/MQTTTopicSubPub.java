package helper;//  aaa

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.*;

import java.util.UUID;

/**
 * A Mqtt topic subscriber
 *
 */
public class MQTTTopicSubPub implements Runnable {
	private static final Logger LOGGER = LogManager.getLogger(MQTTTopicSubPub.class);
	private  String host;
	private  String clientId;
	private  String username;
	private  String password;
	private String topicToSubscribe;
	private MqttCallback callback;
	private boolean started = false;
	MqttClient mqttClient;
	public boolean getStarted() {
		synchronized (this) {
			return started;
		}
	}
	public void setStarted() {
		synchronized (this) {
			started = true;
		}
	}
	public MQTTTopicSubPub(String host, String clientId, String username, String password, String topic, MqttCallback callback) {
		LOGGER.info("MQTTTopicSubscriber constructor");
		this.host = host;
		this.username = username;
		this.password = password;
		this.topicToSubscribe = topic;
		this.callback = callback;
		this.clientId = clientId;

		LOGGER.info("host:" + this.host);
		LOGGER.info("username:" + this.username);
		LOGGER.info("password:" + this.password);
		LOGGER.info("topicToSubscribe:" + this.topicToSubscribe);
		LOGGER.info("clientId:" + this.clientId);
	}
	@Override
	public void run() {
		String host =  this.host;
		String username = this.username;
		String password = this.password;
		String clientId = this.clientId + "_" +  UUID.randomUUID().toString().substring(0,8);;

		LOGGER.info("TopicSubscriber run...");
		LOGGER.info("host:" + host);
		LOGGER.info("username:" + username);
		LOGGER.info("password:" + password);
		LOGGER.info("topicToSubscribe:" + this.topicToSubscribe);
		LOGGER.info("clientId:" + clientId);

		try {
			// Create an Mqtt client
			mqttClient = new MqttClient(host,clientId );
			MqttConnectOptions connOpts = new MqttConnectOptions();
			connOpts.setCleanSession(true);
			connOpts.setUserName(username);
			connOpts.setPassword(password.toCharArray());
			connOpts.setAutomaticReconnect(true);
			// Connect the client
			LOGGER.info("Connecting to " + host + ", " + clientId + ", " + username + ", " + password);
			try{
				mqttClient.connect(connOpts);
			} catch(MqttSecurityException e) {
				LOGGER.error("connection error" + e.toString());
				LOGGER.error("Exception:   " + e);
				LOGGER.error("Reason Code: " + e.getReasonCode());
				LOGGER.error("Message:     " + e.getMessage());
				if (e.getCause() != null)
					LOGGER.error("Cause:       " + e.getCause());
				e.printStackTrace();
				throw new RuntimeException(e);
			}
			catch(MqttException e) {
				LOGGER.error("connection error" + e.toString());
				LOGGER.error("Exception:   " + e);
				LOGGER.error("Reason Code: " + e.getReasonCode());
				LOGGER.error("Message:     " + e.getMessage());
				if (e.getCause() != null)
					LOGGER.error("Cause:       " + e.getCause());
				e.printStackTrace();
				throw new RuntimeException(e);
			};
			LOGGER.info("Connected to " + host + ", " + clientId + ", " + username + ", " + password);
			setStarted();

			mqttClient.setCallback(callback);
			subscribe(topicToSubscribe, 0);

		} catch (MqttException me) {
			LOGGER.error("Exception:   " + me);
			LOGGER.error("Reason Code: " + me.getReasonCode());
			LOGGER.error("Message:     " + me.getMessage());
			if (me.getCause() != null)
				LOGGER.error("Cause:       " + me.getCause());
			me.printStackTrace();
		}
	}
	public  void subscribe(String topicToSubscribe, int qos)  {
		LOGGER.info("Subscribing to topic: " + topicToSubscribe);
		try{
			if (mqttClient != null) {
				mqttClient.subscribe(topicToSubscribe, 0);
				mqttClient.subscribe(topicToSubscribe, 0);
			} else {
				LOGGER.info("Subscribed to topic: " + topicToSubscribe + ", " + host + ", " + clientId + ", " + username + ", " + password);
			}
		} catch (MqttException me) {
			LOGGER.error("Exception:   " + me);
			LOGGER.error("Reason Code: " + me.getReasonCode());
			LOGGER.error("Message:     " + me.getMessage());
			if (me.getCause() != null)
				LOGGER.error("Cause:       " + me.getCause());
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
}