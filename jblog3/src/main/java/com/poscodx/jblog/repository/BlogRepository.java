package com.poscodx.jblog.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.poscodx.jblog.vo.BlogVo;
import com.poscodx.jblog.vo.CategoryVo;

@Repository
public class BlogRepository {
	
	private final SqlSession sqlSession;
	
	public BlogRepository(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}
	
	public int insert(BlogVo blogVo) {
		return sqlSession.insert("blog.insert", blogVo);
	}
//
//	public BlogVo findBlogById(String id) {
//        return sqlSession.selectOne("blog.findById", id);
//	}
	
//	public BlogVo findBlogById(String id) {
//	    return sqlSession.selectOne("blog.findById", id);
//	}
//	
//	public List<Map<String, Object>> getCategoryById(String blogId) {
//		return sqlSession.selectList("category.findAll",blogId);
//	}
//    public Optional<BlogVo> findById(String id) {
//        return Optional.ofNullable(sqlSession.selectOne("blog.findById", Map.of("id", id)));
//    }
    
//	public BlogVo getBlogById(String blogId) {
//		return sqlSession.selectOne("blog.getBlogById",blogId);
//	}
//	
//	
//	public void update(BlogVo blogVo) {
//		sqlSession.update("blog.update", blogVo);
//	}

	public BlogVo findById(String id) {
		return sqlSession.selectOne("blog.findById", id);
	}

	public int update(BlogVo vo) {
		return sqlSession.update("blog.update", vo);
	}
}
