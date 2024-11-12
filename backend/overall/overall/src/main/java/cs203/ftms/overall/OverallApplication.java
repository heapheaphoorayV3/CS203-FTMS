package cs203.ftms.overall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class OverallApplication {

	public static void main(String[] args) {
		SpringApplication.run(OverallApplication.class, args);
	}

}
