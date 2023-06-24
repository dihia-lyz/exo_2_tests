package com.example.exo_2_update;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

//@EnableJpaRepositories("com.example.exo_2_update.*")
@SpringBootApplication
@EntityScan("com.example.exo_2_update.model")
@ComponentScan(basePackages = {"com.example.exo_2_update.repository", "com.example.exo_2_update.unitTests", "com.example.exo_2_update.controller", "com.example.exo_2_update.dto"})
public class Exo2UpdateApplication {
	public static void main(String[] args) {
		SpringApplication.run(Exo2UpdateApplication.class, args);
	}

}
