package blog.ex.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import blog.ex.model.entity.PostEntity;
import blog.ex.model.entity.UserEntity;
import blog.ex.service.PostService;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/author/home")
public class PostListController {
	@Autowired
	private PostService postService;
	
	@Autowired
	private HttpSession session;
	
	@GetMapping("/list")
	public String getIndexPostList(Model model) {
		UserEntity userList = (UserEntity) session.getAttribute("user");
		Long userId = userList.getUserId();
		String userName = userList.getUserName();
	
		List<PostEntity>postList = postService.findAllBlogPostByPostAuthorOrderByPostDateDesc(userId);
		model.addAttribute("userName", userName);
		model.addAttribute("postList", postList);
		return "authorhome.html";
	}
	
}
