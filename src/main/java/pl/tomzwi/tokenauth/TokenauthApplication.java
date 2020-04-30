package pl.tomzwi.tokenauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TokenauthApplication {

	public static void main(String[] args) {
		SpringApplication.run(TokenauthApplication.class, args);
	}

}
