package com.poscodx.jblog.repository;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.poscodx.jblog.vo.CategoryVo;

@Repository
public class CategoryRepository {
	
	private final SqlSession sqlSession;
	
	public CategoryRepository(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}

	public void insertCategory(CategoryVo categoryVo) {
		sqlSession.insert("category.insert", categoryVo);
	}
	
	public List<CategoryVo> findById(String id) {
		return sqlSession.selectList("category.findById", id);
	}

	//가장 최근에 생성된 카테고리 번호를 반환
	public Long findDefaultCategoryByNo(String id) {
		return sqlSession.selectOne("category.findDefaultCategoryByNo", id);
	}
}
