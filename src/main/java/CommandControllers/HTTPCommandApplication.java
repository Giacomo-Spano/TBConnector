package CommandControllers;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class HTTPCommandApplication {

	public static void main(String[] args) {
		SpringApplication.run(HTTPCommandApplication.class, args);
	}

}
