package importer;//  aaa
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Iterator;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import config.Configuration;
import device.Device;
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

	private Thread worker;
	private final static AtomicBoolean running = new AtomicBoolean(false);
	private int interval;

	private static String _host;
	private static String _username;
	private static String _password;

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

	@Override
	public void run() {

		System.out.println("TopicSubscriber initializing...");

		String host =  "tcp://" + _host;//"tcp://giacomocasa.duckdns.org:1883";
		String username = _username;//"giacomo";
		String password = _password;//"giacomo";

		try {
			// Create an Mqtt client
			MqttClient mqttClient = new MqttClient(host,
					"TopicSubscriberdSub_" + UUID.randomUUID().toString().substring(0, 8));
			MqttConnectOptions connOpts = new MqttConnectOptions();
			connOpts.setCleanSession(true);
			connOpts.setUserName(username);
			connOpts.setPassword(password.toCharArray());

			// Connect the client
			System.out.println("Connecting to DeviceMQTT messaging at " + host);
			mqttClient.connect(connOpts);
			System.out.println("Connected");

			// Callback - Anonymous inner-class for receiving messages
			mqttClient.setCallback(new MqttCallback() {

				public void messageArrived(String topic, MqttMessage message) throws Exception {
					// Called when a message arrives from the server that
					// matches any subscription made by the client
					LocalDateTime localDateTime = LocalDateTime.now(ZoneId.of("Europe/Rome"));
					System.out.println(localDateTime);

					String time = new Timestamp(System.currentTimeMillis()).toString();
					System.out.println(
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
					System.out.println("Connection to Solace messaging lost!" + cause.getMessage());
					running.set(false);
				}

				public void deliveryComplete(IMqttDeliveryToken token) {
				}

			});

			if (Configuration.getDevices().size() == 0)
				return;

			for (int i = 0; i < Configuration.getDevices().size(); i++) {
				Device device = Configuration.getDevices().get(i);
				String powertopic = device.getPowertopic();

				System.out.println("Subscribing client to topic: " + powertopic);
				mqttClient.subscribe(powertopic, 0);

			}
			System.out.println("Subscribed. Wait for the message to be received");

			// Wait for the message to be received
			running.set(true);
			while (running.get()) {
				try {
					Thread.sleep(interval);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					System.out.println("Thread was interrupted, Failed to complete operation");
				}
				// do something here
			}

			// Disconnect the client
			mqttClient.disconnect();
			//System.out.println("Exiting--");
			

			//System.exit(0);
		} catch (MqttException me) {
			System.out.println("Exception:   " + me);
			System.out.println("Reason Code: " + me.getReasonCode());
			System.out.println("Message:     " + me.getMessage());
			if (me.getCause() != null)
				System.out.println("Cause:       " + me.getCause());
			me.printStackTrace();
		}
		running.set(false);
	}

	//public static void main(String[] args) throws IOException {
	public  void init(String host, String username, String password)  {

		this._host = host;
		this._username = username;
		this._password = password;

		MQTTImporterTopicSubscriber ts = new MQTTImporterTopicSubscriber();
		ts.start();
		
		try {
			while (System.in.available() == 0 && running.get()) {
				// Thread.sleep(1000); // wait 1 second
				;
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}