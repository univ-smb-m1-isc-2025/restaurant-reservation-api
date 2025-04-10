package m1.info.reza;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class RezaApplication {

	public static void main(String[] args) {
		SpringApplication.run(RezaApplication.class, args);
	}

}
