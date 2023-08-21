package blog.ex.model.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import blog.ex.model.entity.UserEntity;

public interface UserDao extends JpaRepository<UserEntity, Long> {
	UserEntity save(UserEntity userEntity);
	UserEntity findByEmail(String user_email);
	UserEntity findByUserId(Long userId);
}
