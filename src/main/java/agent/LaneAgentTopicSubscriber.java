package agent;//  aaa

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.*;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A Mqtt topic subscriber
 *
 */
public class LaneAgentTopicSubscriber implements Runnable {
	private static final Logger LOGGER = LogManager.getLogger(LaneAgentTopicSubscriber.class);

	private Thread worker;
	private final static AtomicBoolean running = new AtomicBoolean(false);
	private int interval;

	private static String _name;
	private static String _host;
	private static String _username;
	private static String _password;

	private static String _topic;

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

		String host =  "tcp://" + _host;
		String username = _username;
		String password = _password;
		String topic = _topic + "/" + _name;

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
					LOGGER.info(
							"\nReceived a Message!"  + "\n\tTopic:   " + topic + "\n\tMessage: "
									+ new String(message.getPayload()) + "\n\tQoS:     " + message.getQos() + "\n");

					String strMessage = new String(message.getPayload());

					if (strMessage.equals("sleep")) {
						LOGGER.info("received command sleep");
						Runtime r=Runtime.getRuntime();
						Runtime.getRuntime().exec("Rundll32.exe powrprof.dll,SetSuspendState Sleep");

					} else if (message.equals("shutdown")) {

					} else if (message.equals("start")) {

					} else if (message.equals("restart")) {

					} else {
						LOGGER.error("received unknown command");
					}
					// Create Runtime object
					//Runtime r=Runtime.getRuntime();

					// Shutdown system
					//r.exec("shutdown -h");
					//Runtime.getRuntime().exec("Shutdown.exe -s -t 00");
					//Runtime.getRuntime().exec("Rundll32.exe powrprof.dll,SetSuspendState Sleep");
					//Runtime.getRuntime().exec("Rundll32.exe user32.dll,LockWorkStation");

				}

				public void connectionLost(Throwable cause) {
					LOGGER.error("Connection to Solace messaging lost!" + cause.getMessage());
					//running.set(false);
				}

				public void deliveryComplete(IMqttDeliveryToken token) {
				}

			});


			LOGGER.info("Subscribing client to topic: " + topic);
			mqttClient.subscribe(topic, 0);

			LOGGER.info("Lane agent Subscribed. Wait for the message to be received");

			// Wait for the message to be received
			running.set(true);
			while (running.get()) {
				try {
					Thread.sleep(interval);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					LOGGER.info("Thread was interrupted, Failed to complete operation");
				}
				// do something here
			}

			// Disconnect the client
			mqttClient.disconnect();
			//System.out.println("Exiting--");
			

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
	public  void init(String name, String host, String username, String password, String topic)  {

		this._name = name;
		this._host = host;
		this._username = username;
		this._password = password;
		this._topic = topic;

		LaneAgentTopicSubscriber ts = new LaneAgentTopicSubscriber();
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