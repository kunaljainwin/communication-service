package com.my.service.comms_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication(scanBasePackages = {
		"com.my.service.comms_service",
		"com.my.utilities.amqp_adapter"
})
public class CommsServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CommsServiceApplication.class, args);
	}

}
