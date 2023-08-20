package blog.ex.controller;

import java.nio.file.Files;
import java.nio.file.Path;
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

@RequestMapping("/list")

@Controller
public class PostController {

	@Autowired
	private PostService postService;
	
	@Autowired 
	private HttpSession session;
	
	//navigate to new post page 
	@GetMapping("/newpost")
	public String getNewPostPage(Model model) {
		UserEntity userList = (UserEntity) session.getAttribute("user");
		
		String userName = userList.getUserName();
		model.addAttribute("userName", userName);
		model.addAttribute("registerMessage","Add new post");
		return "newpost.html";
	}
	
	@PostMapping("/author/newpost/process")
	public String savePost(@RequestParam String postTitle,
			@RequestParam MultipartFile postImage,
			@RequestParam String postContent) {
				
		UserEntity userList = (UserEntity) session.getAttribute("user");
		Long userId = userList.getUserId();
		String imgFileName = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-").format(new Date()) + postImage.getOriginalFilename();
		LocalDate date = LocalDate.now();
		
		try {
			Files.copy(postImage.getInputStream(), Path.of("src/main/resources/static/blog-img/" + imgFileName));
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		if (postService.createBlogPost(postTitle, imgFileName, date, postContent, userId)) {
			return "redirect:/author/home/list";
		} else {
			return "redirect:/author/hone/newpost";
		}
		
	}
	
}

