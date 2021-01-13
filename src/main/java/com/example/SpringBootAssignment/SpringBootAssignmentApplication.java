package com.example.SpringBootAssignment;

import java.time.Clock;

import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableJpaRepositories
@EnableScheduling
@EnableSwagger2
public class SpringBootAssignmentApplication {
	
	
	public static Logger logger= LoggerFactory.getLogger(SpringBootAssignmentApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(SpringBootAssignmentApplication.class, args);
		
		
		
	}
	
	 
	@Bean
	Clock clock() {
		return Clock.systemUTC();
	}

	@Component
	class DemoCommandLineRunner implements CommandLineRunner {

		@Override
		public void run(String... args) throws Exception {
			// TODO Auto-generated method stub

		}
	}

	
}
