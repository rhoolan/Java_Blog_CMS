package blog.ex.model.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import blog.ex.model.entity.PostEntity;
import jakarta.transaction.Transactional;

public interface PostDao extends JpaRepository<PostEntity, Long>{
	List<PostEntity> findAllBlogPostByPostAuthorOrderByPostDateDesc(Long postAuthor);
	PostEntity save(PostEntity postEntity);
	PostEntity findByPostId(Long postId);
	
	@Transactional
	List<PostEntity> deleteByPostId(Long postId);
}
