package com.poscodx.jblog.service;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poscodx.jblog.repository.BlogRepository;
import com.poscodx.jblog.repository.UserRepository;
import com.poscodx.jblog.vo.BlogVo;
import com.poscodx.jblog.vo.CategoryVo;
import com.poscodx.jblog.vo.UserVo;

@Service
public class UserService {
	
	private final UserRepository userRepository;
	private final BlogRepository blogRepository;
	
	@Autowired
	public UserService(UserRepository userRepository, BlogRepository blogRepository) {
		this.userRepository = userRepository;
		this.blogRepository = blogRepository;
	}

	public void join(@Valid UserVo userVo) {
		// User
		System.out.println("[Join USerVo]: " + userVo);
		userRepository.insert(userVo);  //유저 가입 정보 넣고 
		
		// Blog 
		BlogVo blogVo = new BlogVo();
		blogVo.setBlogId(userVo.getId());
		blogVo.setTitle(userVo.getId()+" 의 블로그");
		blogVo.setLogo("/assets/images/logo.png");
		
		System.out.println("[Blog Info]: " + blogVo);
		
		blogRepository.insert(blogVo);
		
		// Category
		CategoryVo categoryVo = new CategoryVo();
		categoryVo.setNo(1L);
		categoryVo.setName("미분류");
		categoryVo.setDescription("카테고리 미정");
		categoryVo.setBlogId(userVo.getId());
		
		System.out.println("[Category Setup] : " + categoryVo);
		
		blogRepository.insertCategory(categoryVo);
	}
	
	public UserVo getUser(String id, String password) {
		return userRepository.findByIdAndPassword(id, password);
	}
	
	public UserVo getUser(String id) {
		return userRepository.findById(id);
	}

}
