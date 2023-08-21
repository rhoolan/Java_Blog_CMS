package blog.ex.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import blog.ex.model.dao.UserDao;
import blog.ex.model.entity.UserEntity;

@Service
public class UserService {

	@Autowired
	private UserDao userDao;
	
	public boolean createAccount(String userName, String email, String password) {
		UserEntity userEntity = userDao.findByEmail(email);
		
		if (userEntity == null) {
			UUID uuid = UUID.randomUUID();
			String saltStr = uuid.toString();
			String salt = saltStr.substring(0,10);
			String hashedPassword = hashPassword(password+salt);
			userDao.save(new UserEntity(userName, email, hashedPassword, salt));
			return true;
		} else {
			return false;
		}
	}
	
	public UserEntity login(String email, String password) {
		UserEntity userEntity = userDao.findByEmail(email);
		String salt = userEntity.getSalt();
		String hashPassword = hashPassword(password+salt);
		
		if (userEntity.getPassword().equals(hashPassword)) {
			return userEntity;
		}else {
			return null;
		}
	}
	
	private String hashPassword(String password) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(password.getBytes());
			return Base64.getEncoder().encodeToString(hash);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Hashing Algorithm not found", e);
		}
	}
	
	public boolean updateUser(String currentUserEmail, String newUserName, String newUserEmail, String newUserPassword) {
		UserEntity userEntity = userDao.findByEmail(currentUserEmail);
		String salt = userEntity.getSalt();
		String newPasswordHash = hashPassword(newUserPassword+salt);
		
		if(userEntity==null) {
			return false;
		}else {
			userEntity.setUserName(newUserName);
			userEntity.setEmail(newUserEmail);
			userEntity.setPassword(newPasswordHash);
			userDao.save(userEntity);
			return true;
		}
	}
}
