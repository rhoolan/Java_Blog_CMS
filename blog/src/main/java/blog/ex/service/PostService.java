package blog.ex.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import blog.ex.model.dao.PostDao;
import blog.ex.model.entity.PostEntity;
import blog.ex.model.entity.UserEntity;
import jakarta.servlet.http.HttpSession;

@Service
public class PostService {
	
	@Autowired
	private PostDao postDao;
	
	public List<PostEntity> findAllBlogPostByPostAuthor(Long postAuthor){
		if(postAuthor == null) {
			return null;
		} else {
			return postDao.findAllBlogPostByPostAuthor(postAuthor);
		}
	}
	
	// save function
	public boolean createBlogPost(String postTitle, String postImage, LocalDate postDate, String postContent, Long postAuthor) {
		postDao.save(new PostEntity(postTitle, postImage, postDate, postContent, postAuthor));
		return true;
	}
	

	
}
