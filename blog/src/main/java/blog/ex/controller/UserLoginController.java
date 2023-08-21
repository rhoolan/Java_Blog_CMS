package blog.ex.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import blog.ex.model.entity.UserEntity;
import blog.ex.service.UserService;
import jakarta.servlet.http.HttpSession;

@Controller
public class UserLoginController {
	@Autowired
	private UserService userService;
	@Autowired
	private HttpSession session;
	
	//ログイン画面の表示
	@GetMapping("/login")
	public String getLoginRegisterPage() {
		return "loginregister.html";
	}
	
	//ログイン処理
	@PostMapping("/login/process")
	public String login(@RequestParam String login_email, @RequestParam String login_user_password) {
		UserEntity user = userService.login(login_email, login_user_password);
		
		if (userService.login(login_email, login_user_password) == null) {
			return "redirect:/register";
		} else {
			session.setAttribute("user", user);
			return "redirect:/author/home/list";
		}
	}
}
