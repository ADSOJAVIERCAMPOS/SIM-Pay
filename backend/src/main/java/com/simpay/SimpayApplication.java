package com.simpay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SimpayApplication {

	public static void main(String[] args) {
		SpringApplication.run(SimpayApplication.class, args);
	}

}