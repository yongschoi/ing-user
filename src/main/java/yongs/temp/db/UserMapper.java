package yongs.temp.db;

import java.util.List;

import yongs.temp.vo.User;

public interface UserMapper { 
	public List<User> findAll();
	public User findByEmail(String email);
}