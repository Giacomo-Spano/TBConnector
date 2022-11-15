package exporter;

import io.prometheus.client.exporter.HTTPServer;

import java.io.IOException;

public class Main {

    static PrometheusPublisher prometheusPublisher;

    public static void main(String[] args) {
        //Counter counter = Counter.build().namespace("java").name("my_counter").help("This is my counter").register();
        //Gauge gauge = Gauge.build().namespace("java").name("my_gauge").help("This is my gauge").register();
        //Histogram histogram = Histogram.build().namespace("java").name("my_histogram").help("This is my histogram").register();
        //Summary summary = Summary.build().namespace("java").name("my_summary").help("This is my summary").register();

        /*Thread bgThread = new Thread(() -> {
            while (true) {
                try {
                    counter.inc(rand(0, 5));
                    gauge.set(rand(-5, 10));
                    histogram.observe(rand(0, 5));
                    summary.observe(rand(0, 5));


                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        bgThread.start();*/

        PrometheusPublisher.init();

        TopicSubscriber topicSubscriber = new TopicSubscriber();
        topicSubscriber.init();
    }
}