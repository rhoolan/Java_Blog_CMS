package blog.ex.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import blog.ex.model.entity.CommentEntity;
import blog.ex.model.entity.PostEntity;
import blog.ex.model.entity.UserEntity;
import blog.ex.service.CommentService;
import blog.ex.service.PostService;
import blog.ex.service.UserService;
import jakarta.servlet.http.HttpSession;

//＠GetMappingアノテーションは、HTTPのリクエストに対するマッピングを設定する
@RequestMapping("/author/home")

@Controller
public class PostController {

	// Autowiredアノテーションで、DIコンテナが自動的にUserServiceインスタンスを注入するために使用されます。PostControllerクラスの中にPostServiceクラスのメソッド呼び出すことが出来ます
	@Autowired
	private PostService postService;
	
	// Autowiredアノテーションで、DIコンテナが自動的にUserServiceインスタンスを注入するために使用されます。PostControllerクラスの中にUserServiceクラスのメソッド呼び出すことが出来ます
	@Autowired
	private UserService userService;
	
	// Autowiredアノテーションで、DIコンテナが自動的にCommentServiceインスタンスを注入するために使用されます。PostControllerクラスの中にcommentServiceクラスのメソッド呼び出すことが出来ます
	@Autowired
	private CommentService commentService;
	
	// Autowiredアノテーションで、DIコンテナが自動的にHttpSessionインスタンスを注入するために使用されます。PostControllerクラスの中にHttpSessionクラスのメソッド呼び出すことが出来ます
	@Autowired 
	private HttpSession session;
	
	
	//navigate to new post page 
	//ブログ記事を登録させる画面を表示させる
	@GetMapping("/newpost")
	public String getNewPostPage(Model model) {
		UserEntity userList = (UserEntity) session.getAttribute("user");
		
		String userName = userList.getUserName();
		model.addAttribute("userName", userName);
		model.addAttribute("registerMessage","Add new post");
		return "newpost.html";
	}
	
	//ブログ記事の登録処理を行う
	@PostMapping("/author/newpost/process")
	public String savePost(@RequestParam String postTitle,
			@RequestParam MultipartFile postImage,
			@RequestParam String postContent) {
				
		UserEntity userList = (UserEntity) session.getAttribute("user");
		Long userId = userList.getUserId();
		String imgFileName = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-").format(new Date()) + postImage.getOriginalFilename();
		LocalDateTime date = LocalDateTime.now();
		Long visitorCount = (long) 0;
		
		try {
			Files.copy(postImage.getInputStream(), Path.of("src/main/resources/static/blog-img/" + imgFileName));
		}catch(Exception e) {
			e.printStackTrace();
		}
		if(postService.createBlogPost(postTitle, imgFileName, date, postContent, userId, visitorCount)) {
			return "redirect:/author/home/list";
		}else {
			return "redirect:/author/home/newpost";
		}
		
	}
	
	//edit mapping 
	@GetMapping("/edit/{postId}")
	public String getPostEditPage(@PathVariable Long postId, Model model) {
		UserEntity userList = (UserEntity) session.getAttribute("user");
		String userName = userList.getUserName();
		model.addAttribute("userName", userName);
		PostEntity postList = postService.getPost(postId);
		
		if(postList == null) {
			return "redirect:/author/home/list";
		} else {
			model.addAttribute("postList", postList);
			model.addAttribute("editMessage", "Edit post");
			return "editpost.html";
		}
	}
	
	//update post
//	@PostMapping("/update")
//	public String postUpdate(@RequestParam String postTitle,
//			@RequestParam MultipartFile postImage,
//			@RequestParam String postContent,
//			@RequestParam Long postId,
//			Model model) {
//		
//		UserEntity userList = (UserEntity) session.getAttribute("user");
//		Long userId = userList.getUserId();
//		String imgFileName = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-").format(new Date()) + postImage.getOriginalFilename();
//
//		try {
//			Files.copy(postImage.getInputStream(), Path.of("src/main/resources/static/blog-img/" + imgFileName));
//		}catch(Exception e) {
//			e.printStackTrace();
//		}
//		
//		if (postService.editPost(postTitle, imgFileName ,postContent,postId, userId )) {
//			return "redirect:/author/home/list";
//		} else {
//			model.addAttribute("registerMessage", "Edit failed");
//			return "editpost.html";
//		}
//	}
	
	@PostMapping("/update")
	public String postUpdate(@RequestParam String postTitle,
			@RequestParam String postContent,
			@RequestParam Long postId,
			Model model) {
		
		UserEntity userList = (UserEntity) session.getAttribute("user");
		Long userId = userList.getUserId();
	
		if (postService.editPost(postTitle,postContent,postId, userId )) {
			return "redirect:/author/home/list";
		} else {
			model.addAttribute("registerMessage", "Edit failed");
			return "editpost.html";
		}
	}
	
	// delete post 
	@PostMapping("/delete")
	public String postDelete(@RequestParam Long postId, Model model) {
		if(postService.deletePost(postId)) {
			return "redirect:/author/home/list";
		} else {
			model.addAttribute("DeleteDetailMessage", "failed");
			return "redirect:/author/home/list"; 
		}
	}
	
	//view post 
	@GetMapping("/viewpost/{postId}")
	public String getPost(@PathVariable Long postId, Model model) {
		PostEntity post = postService.getPost(postId);
		Long postAuthor = post.getPostAuthor();
		UserEntity author = userService.findByUserId(postAuthor);
		List<CommentEntity> comments = commentService.findAllCommentsByPostIdOrderByCommentDateDesc(postId);
		
		model.addAttribute("comments", comments);
		model.addAttribute("post", post);
		model.addAttribute("author", author);
		postService.incrementVisitorCount(postId);
		return "viewpost.html";
	}
	
	//search bar mapping
	@GetMapping("/search")
	public String searchPosts(@RequestParam String searchTerm, Model model) {
		UserEntity user = (UserEntity) session.getAttribute("user");
		Long userId = user.getUserId();
		List<PostEntity> searchResults = postService.searchPosts(searchTerm, userId);
		
		if (searchResults.isEmpty()) {
			model.addAttribute("user", user);
			return "redirect:/author/home/list";

		} else {
			model.addAttribute("searchResults", searchResults);
			return "searchresults.html";
		}
		
	}
}


