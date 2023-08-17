package blog.ex.model.entity;

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
@Table( name = "users")
public class UserEntity {

	@Id
	@Column( name = "user_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long userId;
	
	@NonNull
	@Column(name = "user_name")
	private String userName;
	
	@NonNull
	@Column(name = "user_email")
	private String email;
	
	@NonNull
	@Column(name = "password")
	private String password;
	
	@NonNull
	@Column(name = "salt")
	private String salt;
	
}
