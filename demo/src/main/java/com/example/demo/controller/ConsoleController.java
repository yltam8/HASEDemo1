package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.batch.Scheduler;

@Controller
public class ConsoleController {
	
	@Autowired
	Scheduler scheduler;

	@RequestMapping("/console")
	public ModelAndView console() {

		ModelAndView model = new ModelAndView();

		model.setViewName("consolepage");
		return model;

	}
	
}
