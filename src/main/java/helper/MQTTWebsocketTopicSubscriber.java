package helper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.*;

/**
 * A Mqtt topic subscriber
 *
 */
public class MQTTWebsocketTopicSubscriber implements Runnable {

	private static final Logger LOGGER = LogManager.getLogger(MQTTWebsocketTopicSubscriber.class);

	private  String host;
	private  String clientId;
	private  String username;
	private  String password;
	private String topicToSubscribe;
	private MqttCallback callback;
	private boolean started = false;
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

	public MQTTWebsocketTopicSubscriber(String host, String clientId, String username, String password, String topic, MqttCallback callback) {
		this.host = host;
		this.username = username;
		this.password = password;
		this.topicToSubscribe = topic;
		this.callback = callback;
		this.clientId = clientId;
	}
	@Override
	public void run() {
		System.out.println("TopicSubscriber initializing...");
		LOGGER.info("host:" + this.host);
		LOGGER.info("username:" + this.username);
		LOGGER.info("password:" + this.host);

		String host =  "ws://" + this.host;
		String username = this.username;
		String password = this.password;

		try {
			// Create an Mqtt client
			MqttClient mqttClient = new MqttClient(host,clientId + "_" + username);
			MqttConnectOptions connOpts = new MqttConnectOptions();
			connOpts.setCleanSession(true);
			connOpts.setUserName(username);
			connOpts.setPassword(password.toCharArray());

			connOpts.setAutomaticReconnect(true);

			// Connect the client
			LOGGER.info("Connecting to DeviceMQTT messaging at " + host);
			try{
				mqttClient.connect(connOpts);
			} catch(MqttSecurityException me) {
				LOGGER.error("Exception:   " + me);
				LOGGER.error("Reason Code: " + me.getReasonCode());
				LOGGER.error("Message:     " + me.getMessage());
				if (me.getCause() != null)
					LOGGER.error("Cause:       " + me.getCause());
				me.printStackTrace();
				throw new RuntimeException(me);
			}
			catch(MqttException me) {
				LOGGER.error("Exception:   " + me);
				LOGGER.error("Reason Code: " + me.getReasonCode());
				LOGGER.error("Message:     " + me.getMessage());
				if (me.getCause() != null)
					LOGGER.error("Cause:       " + me.getCause());
				me.printStackTrace();

				throw new RuntimeException(me);
			};
			LOGGER.info("Connected");
			setStarted();

			mqttClient.setCallback(callback);

			mqttClient.subscribe(topicToSubscribe, 0);

		} catch (MqttException me) {
			LOGGER.error("Exception:   " + me);
			LOGGER.error("Reason Code: " + me.getReasonCode());
			LOGGER.error("Message:     " + me.getMessage());
			if (me.getCause() != null)
				LOGGER.error("Cause:       " + me.getCause());
			me.printStackTrace();
		}
	}
	public  void init()  {
	}
}