package blog.ex.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import blog.ex.service.UserService;

@RequestMapping("/user")

@Controller
public class UserRegisterController {
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/loginregister")
	public String getLoginRegisterPage() {
		return "loginregister.html";
	}
	
	@PostMapping("/register/registerprocess")
	public String register(@RequestParam String user_name, @RequestParam String user_email, @RequestParam String user_password) {
		userService.createAccount(user_name, user_email, user_password);
		return "redirect:/loginregister";
	}
}
