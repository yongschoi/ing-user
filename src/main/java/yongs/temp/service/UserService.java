package yongs.temp.service;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import yongs.temp.db.UserMapper;
import yongs.temp.vo.User;

@Service
@Transactional(readOnly = true)
public class UserService {
	@Autowired
	UserMapper mapper;
	@Autowired 
	MinioService minio;

	public List<User> findAll() {
		List<User> users = mapper.findAll();
		for (User user : users) {
			user.setPhotoUrl(minio.getObjectUrl("example-user", user.getEmail()));
		}
		return users;
	}

	public List<User> findAll(RowBounds rb) {
		List<User> users = mapper.findAll(rb);
		for (User user : users) {
			user.setPhotoUrl(minio.getObjectUrl("example-user", user.getEmail()));
		}
		return users;
	}

	public List<User> findAllNoOffset(int lastId, int pageSize) {
		List<User> users = mapper.findAllNoOffset(lastId, pageSize);
		for (User user : users) {
			user.setPhotoUrl(minio.getObjectUrl("example-user", user.getEmail()));
		}
		return users;
	}
	
	public List<User> findByScoreRating() {
		return findAll().stream().sorted((a, b) -> b.getScore() > a.getScore() ? 1 : -1).collect(Collectors.toList());
	}

	public List<User> findByScoreRatingExcludeZero() {
		return findAll().stream().filter(a -> !((Integer) a.getScore()).equals(0))
				.sorted((a, b) -> b.getScore() > a.getScore() ? 1 : -1).collect(Collectors.toList());
	}

	public User findByEmail(String email) {
		User user = mapper.findByEmail(email);
		if(user != null)
			user.setPhotoUrl(minio.getObjectUrl("example-user", user.getEmail()));	
		else {
			// 사용자가 없는경우 Exception 방식으로 처리하지 않고
			// null 사용자를 return한다.
			user = new User(0, null, null, 0, null);
		}
		return user;
    }
	
	public void create(String email, String name, MultipartFile mFile) {      
		minio.putObject("example-user", email, mFile);
		
		User user = new User();
		user.setEmail(email);
		user.setName(name);
		// 최초 score 0
		user.setScore(0);
		
		mapper.insert(user);
	}
	
	public void update(User user, MultipartFile mFile) {     		
		if(null != mFile) {
			// photo 업데이트를 위해 기존 file 삭제하고 생성한다.
			minio.updateObject("example-user", user.getEmail(), mFile);
		}
		
		mapper.update(user);
	}
}
