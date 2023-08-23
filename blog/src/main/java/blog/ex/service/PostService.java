package blog.ex.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import blog.ex.model.dao.CommentDao;
import blog.ex.model.dao.PostDao;
import blog.ex.model.entity.CommentEntity;
import blog.ex.model.entity.PostEntity;
import blog.ex.model.entity.UserEntity;
import jakarta.servlet.http.HttpSession;

@Service
public class PostService {
	
	@Autowired
	private PostDao postDao;
	
	@Autowired 
	private CommentDao commentDao;
	
	@Autowired
	private HttpSession session;
	
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
	//public boolean editPost(String postTitle, String postImage, String postContent, Long postId, Long userId ) {
//		PostEntity postList = postDao.findByPostId(postId);
//		if(userId==null) {
//			return false;
//		}else {
//			postList.setPostTitle(postTitle);
//			postList.setPostImage(postImage);
//			postList.setPostContent(postContent);
//			postDao.save(postList);
//			return true;
//		}
//			
//	}
	
	public boolean editPost(String postTitle, String postContent, Long postId, Long userId ) {
		// PostEntityでPostIDでインスタすを作って
		PostEntity postList = postDao.findByPostId(postId);
		// 存在してない場合、Falseを戻す
		if(userId==null) {
			return false;
		}else {
			// 編集した内容をpostのオブジェクトを変更する
			postList.setPostTitle(postTitle);
			postList.setPostContent(postContent);
			// 最後にPostdaoで変更したオブジェクトをデータベースに保存する
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
		// postIDはNULLだったら投稿が存在していないのでfalseを戻す
		//　投稿が存在してる場合にはまず投稿のコメントを取得してリストに入れる
		// コメントのforeignKeyはnonnull制約があるので、まずはコメントを全部削除しなかったら投稿を削除できない
		// コメントリストをiterateして全部を削除する
		// 最後に投稿を削除する
		if (postId == null) {
			return false;
		} else {
			List<CommentEntity> comments = commentDao.findAllCommentsByPostIdOrderByCommentDateDesc(postId);
			for (CommentEntity comment: comments) {
				commentDao.deleteById(comment.getCommentId());
			}
			postDao.deleteByPostId(postId);
			return true;
		}
	}

	// increment visitor count 
	public void incrementVisitorCount(Long postId) {
		// PostIdを使ってPostEntityで投稿のインスタンスを作る
		PostEntity post = postDao.findByPostId(postId);
		//　投稿インスタンスから現在のVistiorCountを取得する
		Long currentVisitorCount = post.getVisitorCount();
		//　現在のvisitorCountに１を追加
		Long newVisitorCount = currentVisitorCount + 1;
		//　投稿のインスタンスのVistiorCOuntをcurrentVisitorCountに設定する
		post.setVisitorCount(newVisitorCount);
		// postDaoでインスタンスをデータベースに保存する
		postDao.save(post);
	}

	// search bar 
	public List<PostEntity> searchPosts(String searchTerm, Long userId){
		return postDao.findByLikePostNameOrContentAndPostAuthor(searchTerm, searchTerm, userId);
	}
	
}
