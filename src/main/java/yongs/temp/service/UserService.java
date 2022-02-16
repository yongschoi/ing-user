package yongs.temp.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import yongs.temp.db.UserMapper;
import yongs.temp.vo.User;

@Service
@Transactional(readOnly = true)
public class UserService {
	@Autowired
	UserMapper mapper;
	@Autowired
	MinioService minio;

	public List<User> findAll() throws Exception {
		List<User> users = mapper.findAll();
		for (User user : users) {
			user.setPhotoUrl(minio.getObjectUrl("example-user", user.getPhoto()));
		}
		return users;
	}

	public List<User> findByScoreRating() throws Exception {
		return findAll().stream().sorted((a, b) -> b.getScore() > a.getScore() ? 1 : -1).collect(Collectors.toList());
	}

	public List<User> findByScoreRatingExcludeZero() throws Exception {
		return findAll().stream().filter(a -> !((Integer) a.getScore()).equals(0))
				.sorted((a, b) -> b.getScore() > a.getScore() ? 1 : -1).collect(Collectors.toList());
	}

	public User findByEmail(String email) throws Exception {
		User user = mapper.findByEmail(email);
		if(user != null)
			user.setPhotoUrl(minio.getObjectUrl("example-user", user.getPhoto()));	
		else {
			// 사용자가 없는경우 Exception 방식으로 처리하지 않고
			// null 사용자를 return한다.
			user = new User(0, null, null, 0, null, null);
		}
		return user;
    }
}
