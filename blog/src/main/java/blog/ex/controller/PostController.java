package blog.ex.controller;

import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import blog.ex.model.entity.PostEntity;
import blog.ex.model.entity.UserEntity;
import blog.ex.service.PostService;
import jakarta.servlet.http.HttpSession;

@RequestMapping("/authorhome")

@Controller
public class PostController {

	@Autowired
	private PostService postService;
	
	@Autowired 
	private HttpSession session;
	
	@GetMapping("/authorhome/list")
	public String getIndexPostList(Model model) {
		UserEntity userList = (UserEntity) session.getAttribute("user");
		Long userId = userList.getUserId();
		String userName = userList.getUserName();
	
		List<PostEntity>postList = postService.findAllBlogPostByPostAuthor(userId);
		model.addAttribute("userName", userName);
		model.addAttribute("postList", postList);
		return "authorhome.html";
	}
	
	//navigate to new post page 
	@GetMapping("/authorhome/newpost")
	public String getNewPostPage(Model model) {
		UserEntity userList = (UserEntity) session.getAttribute("user");
		
		String userName = userList.getUserName();
		model.addAttribute("userName", userName);
		model.addAttribute("registerMessage","Add new post");
		return "newpost.html";
	}
	
	@PostMapping("/author/newpost/process")
	public String savePost(@RequestParam String postTitle,
			@RequestParam String postContent) {
				
		UserEntity userList = (UserEntity) session.getAttribute("user");
		Long userId = userList.getUserId();
//		String imgFileName = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-").format(new Date()) + postImage.getOriginalFilename();
		LocalDate date = LocalDate.now();
		String postImage = "image";
		if(postService.createBlogPost(postTitle, postImage, date, postContent, userId)) {
			return "redirect:/authorhome/list";
		}else {
			return "redirect:/authorhome/newpost";
		}
		
	}
	
}

