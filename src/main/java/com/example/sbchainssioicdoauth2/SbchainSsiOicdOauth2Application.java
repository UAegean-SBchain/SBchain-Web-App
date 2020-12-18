package com.example.sbchainssioicdoauth2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SbchainSsiOicdOauth2Application {

	public static void main(String[] args) {
		SpringApplication.run(SbchainSsiOicdOauth2Application.class, args);
	}

}
