package yongs.temp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;

import lombok.extern.slf4j.Slf4j;
import yongs.temp.service.MinioService;
import yongs.temp.service.UserService;
import yongs.temp.vo.User;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
	@Autowired
	UserService userService;

	@Autowired
	MinioService minioService;
	
	@GetMapping("/all")
	public PageInfo<User> findAll(
			final @RequestParam(name = "pageNum") int pageNum,
			final @RequestParam(name = "pageSize") int pageSize) {
		log.debug("UserController.findAll({}, {})", pageNum, pageSize);		
		PageMethod.startPage(pageNum, pageSize);
		return PageInfo.of(userService.findAll());
	}
	
	@GetMapping("/allnooffset")
	public List<User> allnooffset(
			final @RequestParam(name = "lastId") int lastId,
			final @RequestParam(name = "pageSize") int pageSize) {
		log.debug("UserController.allnooffset({}, {})", lastId, pageSize);		
		return userService.findAllNoOffset(lastId, pageSize);
	}
	
	@GetMapping("/score")
	public List<User> findByScoreRating() {
		log.debug("UserController.findByScoreRating()");
		return userService.findByScoreRating();
	}

	@GetMapping("/excludezero")
	public List<User> findByScoreRatingExcludeZero() {
		log.debug("UserController.findByScoreRatingExcludeZero()");
		return userService.findByScoreRatingExcludeZero();
	}
	
	@GetMapping("/email/{email}")
	public User findByEmail(@PathVariable("email") String email) {
		log.debug("UserController.findByEmail()");
		return userService.findByEmail(email); 	
	}

	@PostMapping(value="/create")
    public void create(@RequestPart("email") String email, @RequestPart("name") String name, @RequestPart("file") MultipartFile file) {
		userService.create(email, name, file);	
	}

	@PutMapping(value="/update")
    public void update(@RequestPart("user") User user, @RequestPart(required=false) MultipartFile file) {
		userService.update(user, file);
	}
}
