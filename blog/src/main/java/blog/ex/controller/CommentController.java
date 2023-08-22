package blog.ex.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import blog.ex.service.CommentService;


@Controller
public class CommentController {

	@Autowired 
	private CommentService commentService;
		 
	@PostMapping("/postcomment")
	public String saveComment(@RequestParam Long postId,
			@RequestParam String commentContent) {
		
		System.out.println(postId);
		System.out.println(commentContent);
		LocalDateTime date = LocalDateTime.now();
		commentService.createComment(postId, commentContent, date);
		return "redirect:/author/home/viewpost/"+postId;
	}
}