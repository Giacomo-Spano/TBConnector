package kafka;

//import util.properties packages

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

//Create java class named “SimpleProducer”
public class SimpleProducer {
    private Producer<String, String> producer;
    public void init() {

        //Assign topicName to string variable
        //String topicName = "provatopic";//args[0].toString();

        // create instance for properties to access producer configs
        Properties props = new Properties();

        //Assign localhost id
        props.put("bootstrap.servers", "giacomocasa.duckdns.org:29092");

        //Set acknowledgements for producer requests.
        props.put("acks", "all");

                //If the request fails, the producer can automatically retry,
                props.put("retries", 0);

        //Specify buffer size in config
        props.put("batch.size", 16384);

        //Reduce the no of requests less than 0
        props.put("linger.ms", 1);

        //The buffer.memory controls the total amount of memory available to the producer for buffering.
        props.put("buffer.memory", 33554432);

        props.put("key.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");

        props.put("value.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");

        producer = new KafkaProducer
                <String, String>(props);

    }

    public void sendMessage(String topic, String message) {
        for(int i = 0; i < 10; i++)
            producer.send(new ProducerRecord<String, String>(topic,
                    Integer.toString(i), "messaggio" + Integer.toString(i)));
        System.out.println("Message sent successfully");
    }

    public void close() {
        producer.close();
    }


}