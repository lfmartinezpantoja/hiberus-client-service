package com.hiberus.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@EntityScan("com.hiberus.commons.model")

public class HiberusClientServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(HiberusClientServiceApplication.class, args);
	}

}
