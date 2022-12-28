package helper;//  aaa

import device.Device;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A Mqtt topic subscriber
 *
 */
public class MQTTTopicSubscriber implements Runnable {

	private static final Logger LOGGER = LogManager.getLogger(MQTTTopicSubscriber.class);

	private  String host;
	private  String clientId;
	private  String username;
	private  String password;

	private String topicToSubscribe;
	private MqttCallback callback;

	/*public void ControlSubThread(int sleepInterval) {
		interval = sleepInterval;
	}*/

	/*public void start() {
		worker = new Thread(this);
		worker.start();
	}*/

	/*public void stop() {
		running.set(false);
	}*/

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

	public MQTTTopicSubscriber(String host, String clientId, String username, String password, String topic, MqttCallback callback) {
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

		String host =  "tcp://" + this.host;
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
			} catch(MqttSecurityException e) {
				throw new RuntimeException(e);
			}
			catch(MqttException e) {
				throw new RuntimeException(e);
			};
			LOGGER.info("Connected");
			setStarted();

			mqttClient.setCallback(callback);

			mqttClient.subscribe(topicToSubscribe, 0);



			/*mqttClient.setCallback(new MqttCallback() {
				@Override
				public void connectionLost(Throwable throwable) {

				}

				@Override
				public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {

				}

				@Override
				public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

				}
			});

			LOGGER.info("subscribeAvailabilityTopic");
			try {
				mqttClient.subscribe(topicToSubscribe, 0);
			} catch (MqttException e) {
				throw new RuntimeException(e);
			}*/


			// Wait for the message to be received
			//running.set(true);
			/*while (running.get()) {
				try {
					Thread.sleep(interval);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					LOGGER.error("Thread was interrupted, Failed to complete operation");
				}
				// do something here
			}*/

			// Disconnect the client
			//mqttClient.disconnect();
			//LOGGER.info("Exiting--");
			

			//System.exit(0);
		} catch (MqttException me) {
			LOGGER.error("Exception:   " + me);
			LOGGER.error("Reason Code: " + me.getReasonCode());
			LOGGER.error("Message:     " + me.getMessage());
			if (me.getCause() != null)
				LOGGER.error("Cause:       " + me.getCause());
			me.printStackTrace();
		}
		//running.set(false);
	}

	public  void init()  {
		//MQTTTopicSubscriber ts = new MQTTTopicSubscriber(host, clientId, username, password, topicToSubscribe, callback);
		//ts.start();
		//this.start();
		
		/*try {
			while (System.in.available() == 0 && running.get()) {
				// Thread.sleep(1000); // wait 1 second
				;
			}

		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			// TODO Auto-generated catch block
			e.printStackTrace();

		}*/
	}
}