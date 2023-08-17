package blog.ex.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import blog.ex.service.UserService;

@RequestMapping("/user")

@Controller
public class UserRegisterController {
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/register")
}
