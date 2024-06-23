package com.poscodx.jblog.controller;

import java.util.List;
import java.util.Optional;
import java.util.Locale.Category;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.poscodx.jblog.exception.BlogNotFoundException;
import com.poscodx.jblog.security.Auth;
import com.poscodx.jblog.security.AuthUser;
import com.poscodx.jblog.service.BlogService;
import com.poscodx.jblog.service.FileUploadService;
import com.poscodx.jblog.service.UserService;
import com.poscodx.jblog.vo.BlogVo;
import com.poscodx.jblog.vo.CategoryVo;
import com.poscodx.jblog.vo.PostVo;
import com.poscodx.jblog.vo.UserVo;

@Controller
@RequestMapping("/{id:(?!assets).*}")
public class BlogController {
	
	private final BlogService blogService;
	private final FileUploadService fileUploadService;
	
	public BlogController(BlogService blogService, FileUploadService fileUploadService) {
		this.blogService = blogService;
		this.fileUploadService = fileUploadService;
	}
	
    @RequestMapping({"", "/{categoryNo}", "/{categoryNo}/{postNo}"})
    public String page(@PathVariable String id, 
                       @PathVariable Optional<Long> categoryNo, 
                       @PathVariable Optional<Long> postNo,
                       Model model, 
                       @AuthUser UserVo userVo) {
        
        // 블로그 메인 데이터 조회
        Map<String, Object> blogData = blogService.getBlogMain(id, categoryNo, postNo);
        
        // 모델에 블로그 데이터 추가
        model.addAllAttributes(blogData);
        
        // 인증된 사용자 정보 추가
        model.addAttribute("authUser", userVo);
        
        // 뷰 이름 반환
        return "blog/main";
    }
	
	
	@Auth
	@RequestMapping(value = "/admin/basic", method = RequestMethod.GET)
	public String adminBasic(@PathVariable("id") String id, Model model) {
		BlogVo blogVo = blogService.checkBlogExist(id);
		model.addAttribute("blogVo",blogVo);
		return "blog/admin-basic";
	}
	
	@Auth
	@RequestMapping(value = "/admin/basic", method = RequestMethod.POST)
	public String adminBasic(@PathVariable("id") String id, BlogVo blogVo, @RequestParam("logo-file") MultipartFile file) {
		blogVo.setId(id);
		String logo = fileUploadService.restore(file);
		if (logo != null) {
			blogVo.setLogo(logo);
		}
		blogService.update(blogVo);

		return "redirect:/" + id;
	}

	// @Auth
	@RequestMapping("/admin/category")
	public String adminCategory(@PathVariable("id") String id) {
		
		return "blog/admin-category";
	}
	
	//@Auth
	@RequestMapping("/admin/write")
	public String adminWrite(@PathVariable("id") String id) {
		return "blog/admin-write";
	}
	
	public String isIdExists(String id) {
		if ("".equals(id) || id == null) {
			return "redirect:/";
		}
		return null;
	}

}
