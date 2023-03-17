package com.clinic.pets;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.boot.web.server.ErrorPageRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;

@SpringBootApplication
public class PetsApplication {

	public static void main(String[] args) {
		SpringApplication.run(PetsApplication.class, args);
	}
	@Bean
	public ErrorPageRegistrar errorPageRegistrar() {
	    return (ErrorPageRegistry epr) -> {
	        epr.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/index.html"));
	    };
	}

}
