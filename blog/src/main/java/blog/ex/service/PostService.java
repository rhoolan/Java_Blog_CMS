package blog.ex.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import blog.ex.model.dao.PostDao;
import blog.ex.model.entity.PostEntity;

@Service
public class PostService {
	
	@Autowired
	private PostDao postDao;
	
	public List<PostEntity> findAllBlogPostByUserId(Long userId){
		if(userId == null) {
			return null;
		} else {
			return blogDao.findByUserId(userId);
		}
	}
	
}
