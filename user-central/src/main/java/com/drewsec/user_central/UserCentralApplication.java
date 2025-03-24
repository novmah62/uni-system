package com.drewsec.user_central;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class UserCentralApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserCentralApplication.class, args);
	}

}
