                                                package blog.ex.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import blog.ex.model.entity.UserEntity;
import blog.ex.service.UserService;
import jakarta.servlet.http.HttpSession;

//@RequestMapping("/user")

@Controller
public class UserRegisterController {
	
	@Autowired
	private UserService userService;
	@Autowired
	private HttpSession session;
	
	@GetMapping("/login")
	public String getLoginRegisterPage() {
		return "loginregister.html";
	}
	
	@GetMapping("/authorhome")
	public String getAuthorHomePage(Model model) {
		UserEntity userList = (UserEntity) session.getAttribute("user");
		
		/**
		 * userListから現在ログインしている人のユーザー名を取得**/
		String userName = userList.getUserName();
		model.addAttribute("userName", userName);
		return "authorhome.html";
	}
	
	@PostMapping("/register/registerprocess")
	public String register(@RequestParam String register_user_name, @RequestParam String register_user_email, @RequestParam String register_user_password) {
		userService.createAccount(register_user_name, register_user_email, register_user_password);
		return "redirect:/register";
	}
	
	@PostMapping("/register/loginprocess")
	public String login(@RequestParam String login_email, @RequestParam String login_user_password) {
		UserEntity user = userService.login(login_email, login_user_password);
		
		if (userService.login(login_email, login_user_password) == null) {
			return "redirect:/register";
		} else {
			session.setAttribute("user", user);
			return "redirect:/authorhome/list";
		}
	}
	
	@GetMapping("/logout")
	public String Logout() {
		
		session.invalidate();
		return "redirect:/register";
	}
}                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     