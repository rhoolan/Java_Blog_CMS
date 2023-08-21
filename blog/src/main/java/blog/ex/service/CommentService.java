package blog.ex.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import blog.ex.model.dao.CommentDao;
import blog.ex.model.entity.CommentEntity;

@Service
public class CommentService {
	
	@Autowired
	private CommentDao commentDao;
	
	public boolean createComment(Long postId, String commentContents, LocalDateTime commentDate) {
		commentDao.save(new CommentEntity(postId, commentContents, commentDate));
		return true;
	}

}
