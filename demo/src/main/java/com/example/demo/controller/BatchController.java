package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.batch.Scheduler;

@RestController
@RequestMapping("/batchjob")
public class BatchController {
	
	@Autowired
	Scheduler scheduler;
	
	@RequestMapping("/start")
	public String run() {
		return scheduler.on();
	}
	
	@RequestMapping("/end")
	public String stop() {
		return scheduler.off();
	}
	
}
