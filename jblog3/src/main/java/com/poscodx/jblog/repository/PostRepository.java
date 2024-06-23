package com.poscodx.jblog.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.poscodx.jblog.vo.PostVo;

@Repository
public class PostRepository {
	
	private final SqlSession sqlSession;
	
	public PostRepository(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}
	
	public List<PostVo> findByCategoryNo(Long categoryNo) {
		return sqlSession.selectList("post.findByCategoryNo", categoryNo);
	}

	public PostVo findByPostNo(Long postNo) {
		return sqlSession.selectOne("post.findByPostNo", postNo);
	}

	public Long findDefaultPostByNo(Long categoryNo) {
		return sqlSession.selectOne("post.findDefaultPostByNo", categoryNo);
	}

	public void deleteByCategoryNo(Long no) {
		sqlSession.insert("post.deleteByCategoryNo", no);
	}
}
