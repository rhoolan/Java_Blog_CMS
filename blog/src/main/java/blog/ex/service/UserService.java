package blog.ex.service;

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
			userDao.save(new UserEntity(userName, email, password));
			return true;
		} else {
			return false;
		}
	}
}
