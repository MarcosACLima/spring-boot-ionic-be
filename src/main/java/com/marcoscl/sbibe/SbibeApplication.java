package com.marcoscl.sbibe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.marcoscl.sbibe.services.S3Service;

@SpringBootApplication
public class SbibeApplication implements CommandLineRunner {
	
	@Autowired
	private S3Service s3Service;

	public static void main(String[] args) {
		SpringApplication.run(SbibeApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		s3Service.uploadArquivo("/home/marcos/workspace-sts/fotos/javamascote.png");
	}

}
