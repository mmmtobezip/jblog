package com.poscodx.jblog.service;

import java.util.HashMap;
import java.util.List;
import java.util.Locale.Category;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.poscodx.jblog.exception.BlogNotFoundException;
import com.poscodx.jblog.exception.PostNotFoundException;
import com.poscodx.jblog.repository.BlogRepository;
import com.poscodx.jblog.repository.CategoryRepository;
import com.poscodx.jblog.repository.PostRepository;
import com.poscodx.jblog.vo.BlogVo;
import com.poscodx.jblog.vo.CategoryVo;
import com.poscodx.jblog.vo.PostVo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class BlogService {
	
	private final BlogRepository blogRepository;
	private final CategoryRepository categoryRepository;
	private final PostRepository postRepository;
	
	public BlogService(BlogRepository blogRepository, CategoryRepository categoryRepository, PostRepository postRepository) {
		this.blogRepository = blogRepository;
		this.categoryRepository = categoryRepository;
		this.postRepository = postRepository;
	}
	
	public BlogVo checkBlogExist(String id) {
	    BlogVo vo = blogRepository.findById(id);
	    if (vo == null) throw new BlogNotFoundException("존재하지 않는 블로그 주소입니다.");
	    return vo;
	}
	
	// blog id를 통해 카테고리 리스트 조회 - 카테고리의 no, name 추출 
	public List<CategoryVo> getAllCategory(String id) {
		return categoryRepository.findById(id);
	}
	
	// 특정 categoryno가 가지고 있는 모든 포스트 조회 
	public List<PostVo> getAllPost(Long categoryNo) {
		return postRepository.findByCategoryNo(categoryNo);
	}
	
	// 특정 포스트 번호로 요청한 특정 게시글 조회 
	public PostVo getPost(Long postNo) {
		if(postNo == null) return null;
		return postRepository.findByPostNo(postNo);
	}
	
	public Map<String, Object> getBlogMain(String id, Optional<Long> categoryNo, Optional<Long> postNo) {
	    Map<String, Object> result = new HashMap<>();

	    // 블로그 존재 여부 체크
	    BlogVo blogVo = checkBlogExist(id);
	    result.put("blogVo", blogVo);

	    // blog id에 따른 카테고리 목록 조회
	    List<CategoryVo> cList = getAllCategory(blogVo.getId());
	    result.put("cList", cList);

	    // 카테고리 번호 결정
	    Long cno = categoryNo.orElseGet(() -> getDefaultCategoryNo(id));
	    result.put("cno", cno);

	    // 선택된 카테고리의 포스트 목록 조회
	    List<PostVo> posts = getAllPost(cno);
	    result.put("posts", posts);

	    // 포스트 번호 결정
	    Long pno = postNo.orElseGet(() -> getDefaultPostNo(cno));
	    result.put("pno", pno);

	    // 선택된 포스트 조회
	    PostVo postVo = getPost(pno);
	    result.put("postVo", postVo);

	    return result;
	}
	
	// 주어진 블로그 id에 대한 기본 카테고리 번호 결정 
	private Long getDefaultCategoryNo(String id) {
	    return categoryRepository.findDefaultCategoryByNo(id);
	}

	private Long getDefaultPostNo(Long categoryNo) {
	    Long defaultPostNo = postRepository.findDefaultPostByNo(categoryNo);
	    return defaultPostNo != null ? defaultPostNo : 0L;
	}
    
	public void update(BlogVo blogVo) {
		blogRepository.update(blogVo);
	}

	public List<CategoryVo> getCategoryListWithPostCount(String id) {
		return categoryRepository.findCategoryListWithPostCount(id);
	}
	
	public void addCategory(CategoryVo categoryVo) {
		categoryRepository.insert(categoryVo);
	}

	@Transactional
	public void deleteCategory(String id, Long no) {
		postRepository.deleteByCategoryNo(no);
		categoryRepository.deleteByNo(id, no);
		
	}
}
