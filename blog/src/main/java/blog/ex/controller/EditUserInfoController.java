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

// ＠GetMappingアノテーションは、HTTPのリクエストに対するマッピングを設定する
@RequestMapping("/author/home")

@Controller
public class EditUserInfoController {
	
	// Autowiredアノテーションで、DIコンテナが自動的にUserServiceインスタンスを注入するために使用されます。EditUserInfoControllerクラスの中にUserServiceクラスのメソッド呼び出すことが出来ます
	@Autowired
	private UserService userService;
	
	// Autowiredアノテーションで、DIコンテナが自動的にHTTPSessionインスタンスを注入するために使用されます。EditUserInfoControllerクラスの中にHTTPSessionクラスのメソッド呼び出すことが出来ます
	@Autowired
	private HttpSession session;
	
	// ＠GetMappingアノテーションは、HTTPのリクエストに対するマッピングを設定する
	// Modelはコントロラーからビューに渡すためのデータを格納するために使用されます。
	@GetMapping("/edituserinfo")
	public String getEditUserInfoPage(Model model) {
		//UserEntityのクラスでSessionの情報から現在のユーザー情報を取得してユーザーのインスタンスを作る
		UserEntity user = (UserEntity) session.getAttribute("user");
		//Modelを使ってユーザーの情報をedituserinfo.htmlのビューに渡す
		model.addAttribute("user", user);
		//edituserinfoへ行く
		return "edituserinfo.html";
	}
	
	// ＠PostMappingはFormのPostメソッドのマッピング
	@PostMapping("/updateuserinfo")
	// @RequestParamでHTMLのFORMAのINPUTを受け取る
	// Modelはコントロラーからビューに渡すためのデータを格納するために使用されます。
	public String updateUserInfo(@RequestParam String currentUserEmail, @RequestParam String newUserName,
			@RequestParam String newUserEmail, @RequestParam String newUserPassword,
			Model model) {
		
//		 もしupdateUserが成功したら
//		１．現在のセッションを切る
//		２．UserEntityのクラスで新しいユーザメールとパスワードを使って再度ログインする
//		３．新しいセッションを作る
//		４．Modelを使ってユーザー情報をもビューに渡す
//		５．edituserinfo.htmlのページへ行く
		if (userService.updateUser(currentUserEmail, newUserName, newUserEmail, newUserPassword)) {
			session.invalidate();
			UserEntity user = userService.login(newUserEmail, newUserPassword);
			session.setAttribute("user", user);
			model.addAttribute("user", user);
			return "edituserinfo.html";
		} else {
//			もし成功しなかったらedituserinfoページへ行く
			return "edituserinfo.html";
		}
	}
}
