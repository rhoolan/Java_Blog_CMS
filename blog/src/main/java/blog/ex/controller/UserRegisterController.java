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



@Controller
public class UserRegisterController {
	
	@Autowired
	private UserService userService;
	@Autowired
	private HttpSession session;
	
	//Register画面の表示
		@GetMapping("/register")
		public String getLoginRegisterPage() {
			return "loginregister.html";
		}

    // Register処理
	//PostMappingで/register/processのPOST情報を受取る
	// @RequestParamでHTMLのFORMAのINPUTを受け取る
	@PostMapping("/register/process")
	public String register(@RequestParam String register_user_name, @RequestParam String register_user_email, @RequestParam String register_user_password) {
		// 受け取って情報をuserService.createAccountに渡して
		if (register_user_name == "" || register_user_email == "" || register_user_password == "") {
			return "redirect:/register";
		}
		userService.createAccount(register_user_name, register_user_email, register_user_password);
		return "redirect:/login";
	}
	

	//ログアウト処理
	@GetMapping("/logout")
	public String Logout() {
		
		// セッションを切ってログインページに移動する
		session.invalidate();
		return "redirect:/login";
	}
}
