package yongs.temp.db;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import yongs.temp.vo.User;

public interface UserMapper { 
	public List<User> findAll();
	public List<User> findAll(RowBounds rb);
	public List<User> findAllNoOffset(@Param("lastId") int lastId, @Param("pageSize") int pageSize);	
	 
	public User findByEmail(String email);
	public void insert(User user);	
	public void update(User user);
}