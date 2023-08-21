package blog.ex.model.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "comments")
public class CommentEntity {
	
	@Id
	@Column(name = "comment_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long commentId;
	
	@NonNull
	@Column(name = "post_id")
	private Long postId;
	
	@NonNull
	@Column(name = "comment_contents")
	private String commentContents;
	
	@NonNull
	@Column(name = "comment_date")
	private LocalDateTime commentDate;
}
