package blog.ex.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
	
	public List<PostEntity> findAllBlogPostByPostAuthorOrderByPostDateDesc(Long postAuthor){
		if(postAuthor == null) {
			return null;
		} else {
			return postDao.findAllBlogPostByPostAuthorOrderByPostDateDesc(postAuthor);
		}
	}
	
	// Get single blog post 
	public PostEntity getPost(Long postId) {
		if (postId == null) {
			return null;
		} else {
			return postDao.findByPostId(postId);
		}
	}
	
	// edit blog post function 
	public boolean editPost(String postTitle, String postImage, String postContent, Long postId, Long userId ) {
		PostEntity postList = postDao.findByPostId(postId);
		if(userId==null) {
			return false;
		}else {
			postList.setPostTitle(postTitle);
			postList.setPostImage(postImage);
			postList.setPostContent(postContent);
			postDao.save(postList);
			return true;
		}
			
	}
	
	// save function
	public boolean createBlogPost(String postTitle, String postImage, LocalDateTime postDate, String postContent, Long postAuthor, Long visitorCount) {
		postDao.save(new PostEntity(postTitle, postImage, postDate, postContent, postAuthor, visitorCount));
		return true;
	}
	
	// delete 
	public boolean deletePost(Long postId) {
		if (postId == null) {
			return false;
		} else {
			postDao.deleteByPostId(postId);
			return true;
		}
	}

	// increment visitor count 
	public void incrementVisitorCount(Long postId) {
		PostEntity post = postDao.findByPostId(postId);
		Long currentVisitorCount = post.getVisitorCount();
		Long newVisitorCount = currentVisitorCount + 1;
		post.setVisitorCount(newVisitorCount);
		postDao.save(post);
	}

	
}
