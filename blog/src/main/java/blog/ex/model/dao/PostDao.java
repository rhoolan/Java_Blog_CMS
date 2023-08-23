package blog.ex.model.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import blog.ex.model.entity.PostEntity;
import jakarta.transaction.Transactional;

public interface PostDao extends JpaRepository<PostEntity, Long>{
	List<PostEntity> findAllBlogPostByPostAuthorOrderByPostDateDesc(Long postAuthor);
	PostEntity save(PostEntity postEntity);
	PostEntity findByPostId(Long postId);
//	PostEntity incrementVisitorCount(Long postId);
	
	@Transactional
	List<PostEntity> deleteByPostId(Long postId);
	
	//Search bar 
	//List<PostEntity> findByPostTitleContainingOrPostContentContainingAndPostAuthor(String searchTitleTerm, String searchContentTerm, Long userId);
	
	// SQLでクエリを実行するためのコード
	//SELECT * FROM posts：postsテーブルからすべての列を選択します。
    //	WHERE節内の条件：(post_name ILIKE '%' || ?1 || '%' OR post_content ILIKE '%' || ?2 || '%')
    //	：post_name列またはpost_content列が、
   //	?1で指定された文字列を部分一致検索します。
   //			ILIKEは大文字小文字を区別せずに部分一致を行う演算子です。?1はプレースホルダーであり、実行時に渡される引数で置き換えられます。
    //			AND post_author=?3：post_author列が、?3で指定された値と一致する条件を追加します。
	@Query(value="SELECT * FROM posts WHERE (post_name ILIKE '%' || ?1 || '%' OR post_content ILIKE '%' || ?2 || '%') AND post_author=?3",
		       nativeQuery = true)
	List<PostEntity> findByLikePostNameOrContentAndPostAuthor(String searchTitleTerm, String searchContentTerm, Long userId);
	
}
