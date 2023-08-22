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
	
	public boolean createComment(Long postId, String commentContent, LocalDateTime commentDate) {
		commentDao.save(new CommentEntity(postId, commentContent, commentDate));
		return true;
	}

	public List<CommentEntity> findAllCommentsByPostIdOrderByCommentDateDesc(Long postId) {
		if (postId == null) {
			return null;
		} else {
			return commentDao.findAllCommentsByPostIdOrderByCommentDateDesc(postId);
		}
	}
}
