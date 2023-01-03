package exporter;

import helper.MQTTTopicPublisher;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.util.UUID;

public class KafkaProxyAgentExporter extends Exporter {

    public KafkaProxyAgentExporter(Exporter exporter) {
        super(exporter);
    }

    public void publishAttributes(String token, JSONObject json) {

    }

    public void publishTelemetry(String name, String type, String token, LocalDateTime localDateTime, double power) {

    }

    public void publishPowerMetric(String token, JSONObject json) {
        
    }
}
