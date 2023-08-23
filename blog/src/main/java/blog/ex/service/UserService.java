package blog.ex.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import blog.ex.model.dao.UserDao;
import blog.ex.model.entity.PostEntity;
import blog.ex.model.entity.UserEntity;

@Service
public class UserService {

	@Autowired
	private UserDao userDao;
	
	// createAccount/Registerの処理
	public boolean createAccount(String userName, String email, String password) {
		// UserDaoのfindByEmailを使ってユーザデータベースからユーザの情報を取得してUserEntityでユーザーインスタンスを作る
		UserEntity userEntity = userDao.findByEmail(email);
		
		// userEntityがNullだったらつまりユーザーが存在していない場合、ユーザー登録処理を始める
		if (userEntity == null) {
			// UUIDを使ってRandom文字を作る
			UUID uuid = UUID.randomUUID();
			// あの文字配列を文字配列化
			String saltStr = uuid.toString();
			// あの文字配列を最初の１０文字に絞り込む
			String salt = saltStr.substring(0,10);
			// あの文字配列をユーザーを入力したパスワードと組み合わせてHasedPasswordに渡してHashedPasswordを作る
			String hashedPassword = hashPassword(password+salt);
			// UserName, email, hashedpassword, saltをUserDaoに渡してデータベースに保存する。
			// 成功した場合Trueを戻す・成功してない場合FALSEを戻す
			userDao.save(new UserEntity(userName, email, hashedPassword, salt));
			return true;
		} else {
			return false;
		}
	}
	
	// Loginの処理
	public UserEntity login(String email, String password) {
		// UserDaoのfindByEmailを使ってユーザデータベースからユーザの情報を取得してUserEntityでユーザーインスタンスを作る
		UserEntity userEntity = userDao.findByEmail(email);
		// ユーザーインスタンスからユーザーのSALTを取得して
		String salt = userEntity.getSalt();
		// ユーザーが入力したパスワードとSALTを組み合わせてhashPasswordに渡してハッシュする
		String hashPassword = hashPassword(password+salt);
		
		// あのhashPasswordがデータベースに保存してるhashedPasswordと同じ場合、UserEntityを戻す
		//　同じではない場合、NUllを戻す
		if (userEntity.getPassword().equals(hashPassword)) {
			return userEntity;
		}else {
			return null;
		}
	}
	
	// SHA-256を使ってplain-textのパスワードを暗号化する
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

	public UserEntity findByUserId(Long postAuthor) {
		if (postAuthor == null) {
			return null;
		} else {
			return userDao.findByUserId(postAuthor);
		}
	}
	
}