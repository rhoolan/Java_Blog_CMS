package blog.ex.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import blog.ex.service.UserService;

//@RequestMapping("/user")

@Controller
public class UserRegisterController {
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/register")
	public String getLoginRegisterPage() {
		return "loginregister.html";
	}
	
	@PostMapping("/register/registerprocess")
	public String register(@RequestParam String register_user_name, @RequestParam String register_user_email, @RequestParam String register_user_password) {
		userService.createAccount(register_user_name, register_user_email, register_user_password);
		return "redirect:/register";
	}
}
