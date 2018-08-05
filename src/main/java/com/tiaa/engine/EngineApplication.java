package com.tiaa.engine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * This is the main/ start of the Application.
 * 
 * @author vijay
 *
 */
@SpringBootApplication
@EnableScheduling
public class EngineApplication {

	private static final Logger log = LoggerFactory.getLogger(EngineApplication.class);

	@Autowired
	CronScheduler cronScheduler;

	public static void main(String[] args) {
		SpringApplication.run(EngineApplication.class, args);
	}

}
