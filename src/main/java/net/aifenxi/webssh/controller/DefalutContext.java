package net.aifenxi.webssh.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DefalutContext {
	
	public DefalutContext() {
		System.out.println("init...");
	}
	
	@RequestMapping("/")
	public String getRoot() {
		return "remote";
	}
}
