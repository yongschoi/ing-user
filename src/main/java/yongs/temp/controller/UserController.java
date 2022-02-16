package yongs.temp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import yongs.temp.service.UserService;
import yongs.temp.vo.User;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
	@Autowired
	UserService userService;
	
	@GetMapping("/all")
	public List<User> findAll() throws Exception {
		log.debug("UserController.findAll()");
		return userService.findAll();
	}
	
	@GetMapping("/score")
	public List<User> findByScoreRating() throws Exception {
		log.debug("UserController.findByScoreRating()");
		return userService.findByScoreRating();
	}

	@GetMapping("/excludezero")
	public List<User> findByScoreRatingExcludeZero() throws Exception {
		log.debug("UserController.findByScoreRatingExcludeZero()");
		return userService.findByScoreRatingExcludeZero();
	}

	@GetMapping("/email/{email}")
	public User findByEmail(@PathVariable("email") String email) throws Exception {
		log.debug("UserController.findByEmail()");
		return userService.findByEmail(email); 	
	}
}
