package net.aifenxi.webssh.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class Login {
	
	@RequestMapping("/login")
	public String login(String username ,String password) {
		System.out.println("username:"+username +"    password:" + password);
		if("admin".equals(username) && "abc123!".equals(password)) {
			return "main";
		}
		return "login/login";
	}

}
