package com.capstone.fertility;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class FertilityApplication {

	public static void main(String[] args) {
		SpringApplication.run(FertilityApplication.class, args);
	}

}
