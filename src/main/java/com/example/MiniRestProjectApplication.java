package com.example;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import java.io.IOException;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class MiniRestProjectApplication implements CommandLineRunner{

	public static void main(String[] args) throws IOException {
		SpringApplication.run(MiniRestProjectApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

	}
}
