package importer;//  aaa
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Iterator;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import config.Configuration;
import device.Device;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * A Mqtt topic subscriber
 *
 */
public class MQTTImporterTopicSubscriber implements Runnable {

	private static final Logger LOGGER = LogManager.getLogger(MQTTImporterTopicSubscriber.class);

	private Thread worker;
	private final static AtomicBoolean running = new AtomicBoolean(false);
	private int interval;

	private  String host;
	private  String username;
	private  String password;

	public void ControlSubThread(int sleepInterval) {
		interval = sleepInterval;
	}

	public void start() {
		worker = new Thread(this);
		worker.start();
	}

	public void stop() {
		running.set(false);
	}

	public MQTTImporterTopicSubscriber(String host, String username, String password) {
		this.host = host;
		this.username = username;
		this.password = password;
	}
	@Override
	public void run() {

		System.out.println("TopicSubscriber initializing...");
		LOGGER.info("_host:" + this.host);
		LOGGER.info("_username:" + this.username);
		LOGGER.info("_password:" + this.host);

		String host =  "tcp://" + this.host;//"tcp://giacomocasa.duckdns.org:1883";
		String username = this.username;//"giacomo";
		String password = this.password;//"giacomo";

		try {
			// Create an Mqtt client
			MqttClient mqttClient = new MqttClient(host,
					"TopicSubscriberdSub_" + UUID.randomUUID().toString().substring(0, 8));
			MqttConnectOptions connOpts = new MqttConnectOptions();
			connOpts.setCleanSession(true);
			connOpts.setUserName(username);
			connOpts.setPassword(password.toCharArray());

			connOpts.setAutomaticReconnect(true);

			// Connect the client
			LOGGER.info("Connecting to DeviceMQTT messaging at " + host);
			mqttClient.connect(connOpts);
			LOGGER.info("Connected");

			// Callback - Anonymous inner-class for receiving messages
			mqttClient.setCallback(new MqttCallback() {

				public void messageArrived(String topic, MqttMessage message) throws Exception {
					// Called when a message arrives from the server that
					// matches any subscription made by the client
					LocalDateTime localDateTime = LocalDateTime.now(ZoneId.of("Europe/Rome"));
					System.out.println(localDateTime);

					String time = new Timestamp(System.currentTimeMillis()).toString();
					LOGGER.info(
							"\nReceived a Message!" + "\n\tTime:    " + localDateTime/*time*/ + "\n\tTopic:   " + topic + "\n\tMessage: "
									+ new String(message.getPayload()) + "\n\tQoS:     " + message.getQos() + "\n");

					Iterator<Device> deviceIterator = Configuration.getDevices().iterator();
					while (deviceIterator.hasNext()) {
						Device device = deviceIterator.next();
						if (topic.equals(device.getPowertopic())) {

							device.receiveMessage(localDateTime, topic,new String(message.getPayload()));
							/*try {
								TopicPublisher publisher = new TopicPublisher();
								publisher.createConnection(configuration.getThingsboardMQTThost(), device.getToken(),"");
								publisher.publishMessage(publishtopic, publishmsg);
								publisher.closeConnection();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}*/
							//break;
						}
					}
				}

				public void connectionLost(Throwable cause) {
					LOGGER.error("Connection to Solace messaging lost!" + cause.getMessage());
					//running.set(false);
				}

				public void deliveryComplete(IMqttDeliveryToken token) {
				}

			});

			if (Configuration.getDevices().size() == 0) {
				LOGGER.info("no devices found");
				return;
			}

			for (int i = 0; i < Configuration.getDevices().size(); i++) {
				Device device = Configuration.getDevices().get(i);
				String powertopic = device.getPowertopic();
				LOGGER.info("device: " + device.getName());
				LOGGER.info("Subscribing client to topic: " + powertopic);
				mqttClient.subscribe(powertopic, 0);

			}
			LOGGER.info("Subscribed. Wait for the message to be received");

			// Wait for the message to be received
			running.set(true);
			while (running.get()) {
				try {
					Thread.sleep(interval);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					LOGGER.error("Thread was interrupted, Failed to complete operation");
				}
				// do something here
			}

			// Disconnect the client
			mqttClient.disconnect();
			LOGGER.info("Exiting--");
			

			//System.exit(0);
		} catch (MqttException me) {
			LOGGER.error("Exception:   " + me);
			LOGGER.error("Reason Code: " + me.getReasonCode());
			LOGGER.error("Message:     " + me.getMessage());
			if (me.getCause() != null)
				LOGGER.error("Cause:       " + me.getCause());
			me.printStackTrace();
		}
		running.set(false);
	}

	//public static void main(String[] args) throws IOException {
	public  void init(String host, String username, String password)  {

		/*this._host = host;
		this._username = username;
		this._password = password;*/

		MQTTImporterTopicSubscriber ts = new MQTTImporterTopicSubscriber(host, username, password);
		ts.start();
		
		try {
			while (System.in.available() == 0 && running.get()) {
				// Thread.sleep(1000); // wait 1 second
				;
			}

		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
	}
}