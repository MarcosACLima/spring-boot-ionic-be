package com.marcoscl.sbibe;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SbibeApplication implements CommandLineRunner {
	
	public static void main(String[] args) {
		SpringApplication.run(SbibeApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
	}

}
