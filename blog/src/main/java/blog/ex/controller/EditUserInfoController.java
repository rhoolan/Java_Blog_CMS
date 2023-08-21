package blog.ex.controller;

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

@RequestMapping("/author/home")

@Controller
public class EditUserInfoController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private HttpSession session;
	
	@GetMapping("/edituserinfo")
	public String getEditUserInfoPage(Model model) {
		UserEntity user = (UserEntity) session.getAttribute("user");
		model.addAttribute("user", user);
		return "edituserinfo.html";
	}
	
	@PostMapping("/updateuserinfo")
	public String updateUserInfo(@RequestParam String currentUserEmail, @RequestParam String newUserName,
			@RequestParam String newUserEmail, @RequestParam String newUserPassword,
			Model model) {
		
		if (userService.updateUser(currentUserEmail, newUserName, newUserEmail, newUserPassword)) {
			session.invalidate();
			UserEntity user = userService.login(newUserEmail, newUserPassword);
			session.setAttribute("user", user);
			model.addAttribute("user", user);
			return "edituserinfo.html";
		} else {
			model.addAttribute("updateMessage", "update failed");
			return "edituserinfo.html";
		}
	}
}
