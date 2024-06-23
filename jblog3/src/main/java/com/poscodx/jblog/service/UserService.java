package com.poscodx.jblog.service;

import javax.validation.Valid;

import org.springframework.stereotype.Service;

import com.poscodx.jblog.repository.BlogRepository;
import com.poscodx.jblog.repository.CategoryRepository;
import com.poscodx.jblog.repository.UserRepository;
import com.poscodx.jblog.vo.BlogVo;
import com.poscodx.jblog.vo.CategoryVo;
import com.poscodx.jblog.vo.UserVo;

@Service
public class UserService {
	
	private final UserRepository userRepository;
	private final BlogRepository blogRepository;
	private final CategoryRepository categoryRepository;

	public UserService(UserRepository userRepository, BlogRepository blogRepository, CategoryRepository categoryRepository) {
		this.userRepository = userRepository;
		this.blogRepository = blogRepository;
		this.categoryRepository = categoryRepository;
	}

	public void join(@Valid UserVo userVo) {
		// User
		System.out.println("[Join USerVo]: " + userVo);
		userRepository.insert(userVo);  
		
		// Blog 
		BlogVo blogVo = new BlogVo();
		blogVo.setId(userVo.getId());
		blogVo.setTitle(userVo.getId()+" 의 블로그");
		blogVo.setLogo("/assets/images/spring-logo.jpg");
		
		System.out.println("[Blog Info]: " + blogVo);
		
		blogRepository.insert(blogVo);
		
		// Category
		CategoryVo categoryVo = new CategoryVo();
		categoryVo.setId(userVo.getId());
		categoryVo.setName("미분류");
		categoryVo.setDescription("카테고리 미정");
		System.out.println("[Category Setup] : " + categoryVo);
		categoryRepository.insertCategory(categoryVo);
	}
	
	public UserVo getUser(String id, String password) {
		return userRepository.findByIdAndPassword(id, password);
	}
	
	public UserVo getUser(String id) {
		return userRepository.findById(id);
	}
}
