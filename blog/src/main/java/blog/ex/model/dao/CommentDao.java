package blog.ex.model.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import blog.ex.model.entity.CommentEntity;

public interface CommentDao extends JpaRepository<CommentEntity, Long>{
	CommentEntity save(CommentEntity commentEntity);
}
