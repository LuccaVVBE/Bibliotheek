package com.springBoot.bibliotheek;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import perform.PerformRest;
import service.BibService;
import service.BibServiceImpl;
import validators.BoekValidator;


@SpringBootApplication
@EnableJpaRepositories("repository")
@EntityScan("domain")
public class BibliotheekApplication implements WebMvcConfigurer{

	public static void main(String[] args) {
		SpringApplication.run(BibliotheekApplication.class, args);
		
		try {
			new PerformRest();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
    public void addViewControllers(ViewControllerRegistry registry) {
	   registry.addRedirectViewController("/", "/bib");
    }
	
	@Bean
	BoekValidator boekValidator() {
		return new BoekValidator();
	}
	
	@Bean
	BibService bibService() {
		return new BibServiceImpl();
	}
}
