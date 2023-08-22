package blog.ex.model.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import blog.ex.model.entity.CommentEntity;
import blog.ex.model.entity.PostEntity;

public interface CommentDao extends JpaRepository<CommentEntity, Long>{
	CommentEntity save(CommentEntity commentEntity);
	List<CommentEntity> findAllCommentsByPostIdOrderByCommentDateDesc(Long postId);

	void deleteById(Long postId);
}

