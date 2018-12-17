package com.example.demo.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginController {

	@RequestMapping("/HASElogin")
	public ModelAndView login(@RequestParam(value = "error", required = false) String error , 
			@RequestParam(value = "accessDeny", required = false) String accessDeny , 
			HttpServletRequest request , Exception ex) {

		ModelAndView model = new ModelAndView();
			
		if (error != null) {
			model.addObject("error", "Invalid username and password!");
		}
		
		if (accessDeny != null) {
			model.addObject("error", "Access Deny , you have no permission to access !");
		}

		model.setViewName("loginpage");
		return model;

	}
}
