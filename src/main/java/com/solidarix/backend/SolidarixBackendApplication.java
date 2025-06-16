package com.solidarix.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SolidarixBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(SolidarixBackendApplication.class, args);
	}

}
