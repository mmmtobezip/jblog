package com.poscodx.jblog.repository;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.poscodx.jblog.vo.BlogVo;
import com.poscodx.jblog.vo.CategoryVo;

@Repository
public class BlogRepository {
	
	@Autowired
	private SqlSession sqlSession;
	
	public int insert(BlogVo blogVo) {
		return sqlSession.insert("blog.insert", blogVo);
		
	}

	public int insertCategory(CategoryVo categoryVo) {
		return sqlSession.insert("category.insert", categoryVo);
	}

}
