package blog.ex.model.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import blog.ex.model.entity.PostEntity;

public interface PostDao extends JpaRepository<PostEntity, Long>{
	List<PostEntity> findAllBlogPostByPostAuthor(Long postAuthor);
	PostEntity save(PostEntity postEntity);
}
