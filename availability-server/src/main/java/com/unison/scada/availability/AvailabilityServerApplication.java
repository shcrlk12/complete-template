package com.unison.scada.availability;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class AvailabilityServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(AvailabilityServerApplication.class, args);
	}
}
