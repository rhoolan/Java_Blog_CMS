package blog.ex.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import blog.ex.model.dao.CommentDao;
import blog.ex.model.entity.CommentEntity;

@Service
public class CommentService {
	
	@Autowired
	private CommentDao commentDao;
	
	// コメントをデータベースに保存するメソッド
	public boolean createComment(Long postId, String commentContent, LocalDateTime commentDate) {
		//　commentDaoを使ってデータベースに保存する
		commentDao.save(new CommentEntity(postId, commentContent, commentDate));
		return true;
	}

	// 投稿ページに表示させるためのメソッド
	// コメントをリストとして戻す
	public List<CommentEntity> findAllCommentsByPostIdOrderByCommentDateDesc(Long postId) {
		// PostIDはNULLだったら投稿が存在していないのでNullを戻す
		if (postId == null) {
			return null;
		} else {
			//　存在したら、commentDaoを使って投稿のコメントを新しい順のリストを戻す
			return commentDao.findAllCommentsByPostIdOrderByCommentDateDesc(postId);
		}
	}
}
