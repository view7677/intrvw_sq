package com.tiaa.engine;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * CronScheduler is to schedule job at a give time.
 * 
 * @author vijay
 *
 */
@Component
public class CronScheduler {

	private static final Logger log = LoggerFactory.getLogger(CronScheduler.class);

	@Autowired
	CalculateEngine calculateEngine;

	@Scheduled(cron = "${cron.expression}")
	public void run() throws Exception {
		log.info("Cron scheduler is running at {} ", new Date());
		calculateEngine.calculateMatchMismatch();

	}
}
