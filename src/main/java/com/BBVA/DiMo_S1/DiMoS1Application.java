package com.BBVA.DiMo_S1;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DiMoS1Application {

	public static void main(String[] args) {
		SpringApplication.run(DiMoS1Application.class, args);
	}

	//Agregamos Bean para que nos avise por consola que efectivamente la aplicacion se inicio con éxito.
	@Bean
	public CommandLineRunner commandLineRunner (ApplicationContext ctx) {

		return args -> {
			System.out.printf("Inició la aplicacion con exito");
		};
	}

}
