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

	// Autowiredアノテーションで、DIコンテナが自動的にCommentServiceインスタンスを注入するために使用されます。CommentControllerクラスの中にCommentServiceクラスのメソッド呼び出すことが出来ます
	@Autowired 
	private CommentService commentService;
		 
	// ＠PostMappingアノテーションは、HTTP　GETのリクエストに対するマッピングを設定する
	@PostMapping("/postcomment")
	// @RequestParamでHTMLのFORMAのINPUTを受け取る
	public String saveComment(@RequestParam Long postId,
			@RequestParam String commentContent) {
		
		// LocalDateTimeで今の日付と時間を取得する
		LocalDateTime date = LocalDateTime.now();
		// コメントをデータベースに保存するためにFORMから受け取った内容と現在の日付と時間をCommentServiceに渡す
		commentService.createComment(postId, commentContent, date);
		// 成功したら現在見てる投稿のViewpostのページへ行く
		return "redirect:/author/home/viewpost/"+postId;
	}
}