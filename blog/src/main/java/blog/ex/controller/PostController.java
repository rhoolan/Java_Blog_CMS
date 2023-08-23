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
		// セッションからユーザー情報を取得してUserEntityをインスタンスを作る
		UserEntity userList = (UserEntity) session.getAttribute("user");
	    //　あのインスタンスからユーザーの名前を取得して変数を設定する
		String userName = userList.getUserName();
		//　Modelを使ってユーザー名前をビュイ‐に渡す
		model.addAttribute("userName", userName);
		model.addAttribute("registerMessage","Add new post");
		//　newpost.htmlへ行く
		return "newpost.html";
	}
	
	//ブログ記事の登録処理を行う
	@PostMapping("/author/newpost/process")
	// @RequestParamでHTMLのFORMAのINPUTを受け取る
	public String savePost(@RequestParam String postTitle,
			@RequestParam MultipartFile postImage,
			@RequestParam String postContent) {
		// セッションからユーザー情報を取得してUserEntityをインスタンスを作る		
		UserEntity userList = (UserEntity) session.getAttribute("user");
	    //　あのインスタンスからユーザーのIDを取得して変数を設定する
		Long userId = userList.getUserId();
		// 画像の名前を作る。現在の日付と時間＋ファイルの名前
		String imgFileName = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-").format(new Date()) + postImage.getOriginalFilename();
		// 現在の日付と時間を取得
		LocalDateTime date = LocalDateTime.now();
		// VisitorCountを０に
		Long visitorCount = (long) 0;
		
		// Try文を使って画像をフォームから受け取って、imgFileNameを後付く
		try {
			Files.copy(postImage.getInputStream(), Path.of("src/main/resources/static/blog-img/" + imgFileName));
		}catch(Exception e) {
			e.printStackTrace();
		}
		// postService.creatBlogPostに投稿の情報を渡して、creatBlogPostでデータベースに保存する。
		// 成功したらauthor/home/listへ行く、成功しなかったらauthor/home/newpostに戻る
		if(postService.createBlogPost(postTitle, imgFileName, date, postContent, userId, visitorCount)) {
			return "redirect:/author/home/list";
		}else {
			return "redirect:/author/home/newpost";
		}
		
	}
	
	//GetMappingdeブログ記事を編集画面を表示させる
	@GetMapping("/edit/{postId}")
	public String getPostEditPage(@PathVariable Long postId, Model model) {
		// セッションからユーザー情報を取得してUserEntityをインスタンスを作る		
		UserEntity userList = (UserEntity) session.getAttribute("user");
	    //　あのインスタンスからユーザーの名前を取得して変数を設定する
		String userName = userList.getUserName();
		// モデルを使ってUserNameをEditPostページに表示するためにビューに渡す
		model.addAttribute("userName", userName);
		// PostEntityを使って投稿のpostIDで投稿インスタンスを作る
		PostEntity postList = postService.getPost(postId);
		
		// postインスタンスがなかったら、つまりあの投稿存在してない場合、author/home/listに戻る
		// 投稿存在している場合、EditPostのページに表示するためにモデルでpostの情報をビューに渡してeditpost.htmlに行く
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
	// @RequestParamでHTMLのFORMAのINPUTを受け取る
	public String postUpdate(@RequestParam String postTitle,
			@RequestParam String postContent,
			@RequestParam Long postId,
			Model model) {
		// セッションを使ってUserの情報を取得してUserEntityをインスタンス化
		UserEntity userList = (UserEntity) session.getAttribute("user");
		// あのインスタンスでユーザーのIDを取得して
		Long userId = userList.getUserId();
	
		// 編集した内容をpostService.editPostに渡して編集を実行する
		// 成功した場合、author/home/listに移動する。成功しなかった場合editpostページに戻す
		if (postService.editPost(postTitle,postContent,postId, userId )) {
			return "redirect:/author/home/list";
		} else {
			model.addAttribute("registerMessage", "Edit failed");
			return "editpost.html";
		}
	}
	
	// delete post 
	// @RequestParamでHTMLのFORMAのINPUTを受け取る
	@PostMapping("/delete")
	public String postDelete(@RequestParam Long postId, Model model) {
		// PostIDをPostService.DeletePostに渡して削除処理を始める
		// 成功した場合、author/home/listに移動する
		// 成功しなかった場合, author/home/listに移動する
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
			model.addAttribute("user", user);
			model.addAttribute("searchResults", searchResults);
			return "searchresults.html";
		}
		
	}
}


