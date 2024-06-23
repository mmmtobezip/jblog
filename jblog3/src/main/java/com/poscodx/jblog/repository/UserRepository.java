package com.poscodx.jblog.repository;

import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import org.springframework.stereotype.Repository;

import com.poscodx.jblog.vo.UserVo;

@Repository
public class UserRepository {
	
	private final SqlSession sqlSession;
	
	public UserRepository(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}

	public int insert(UserVo userVo) {
		return sqlSession.insert("user.insert", userVo);	
	}

	public UserVo findByIdAndPassword(String id, String password) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		map.put("password", password);
		
		return sqlSession.selectOne("user.findByEmailAndPassword", map);
	}

	public UserVo findById(String id) {
		return sqlSession.selectOne("user.findById", id);
	}
}
