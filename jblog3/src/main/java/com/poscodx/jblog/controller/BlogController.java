package com.poscodx.jblog.controller;

import java.util.Optional;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.poscodx.jblog.security.Auth;
import com.poscodx.jblog.security.AuthUser;
import com.poscodx.jblog.service.BlogService;
import com.poscodx.jblog.service.FileUploadService;
import com.poscodx.jblog.vo.BlogVo;
import com.poscodx.jblog.vo.CategoryVo;
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

        Map<String, Object> blogData = blogService.getBlogMain(id, categoryNo, postNo);
  
        model.addAllAttributes(blogData);

        model.addAttribute("authUser", userVo);

        return "blog/main";
    }
	
	@Auth
	@GetMapping("/admin/basic")
	public String adminBasic(@PathVariable("id") String id, @AuthUser UserVo authUser, Model model) {
	    if (!authUser.getId().equals(id)) {
	        return "redirect:/" + id; // to-do: error 처리 
	    }
		
		BlogVo blogVo = blogService.checkBlogExist(id);
		model.addAttribute("blogVo",blogVo);
		return "blog/admin-basic";
	}
	
	@Auth
	@PostMapping("/admin/basic")
	public String adminBasic(@PathVariable("id") String id, @AuthUser UserVo authUser, BlogVo blogVo, @RequestParam("logo-file") MultipartFile file) {
	    if (!authUser.getId().equals(id)) {
	        return "redirect:/" + id; // to-do: error 처리 
	    }
	    
		blogVo.setId(id);
		String logo = fileUploadService.restore(file);
		if (logo != null) {
			blogVo.setLogo(logo);
		}
		blogService.update(blogVo);

		return "redirect:/" + id;
	}

	@Auth
	@GetMapping("/admin/category")
	public String adminCategory(@PathVariable("id") String id, @AuthUser UserVo authUser, Model model) {
	    if (!authUser.getId().equals(id)) {
	        return "redirect:/" + id;// to-do: error 처리 
	    }
	    
	    BlogVo blogVo = blogService.checkBlogExist(id);
	    List<CategoryVo> cListWithPostCount = blogService.getCategoryListWithPostCount(id);
	    
	    model.addAttribute("authUser", authUser);
	    model.addAttribute("blogVo", blogVo);
	    model.addAttribute("cListWithPostCount", cListWithPostCount);
		return "blog/admin-category";
	}
	
	@Auth
	@PostMapping("/admin/category")
	public String adminCategory(@PathVariable("id") String id,  @AuthUser UserVo authUser, CategoryVo categoryVo) {
	    if (!authUser.getId().equals(id)) {
	        return "redirect:/" + id;// to-do: error 처리 
	    }
	    
	    categoryVo.setId(id);
	    blogService.addCategory(categoryVo);
	    
	    return "redirect:/" + id + "/admin/category";
	}
	
	@Auth
	@GetMapping("/admin/category/delete/{no}")
	public String adminCetegory(@PathVariable("id") String id, @AuthUser UserVo authUser, @PathVariable("no") Long no) {
	    if (!authUser.getId().equals(id)) {
	        return "redirect:/" + id;// to-do: error 처리 
	    }
	    
	    blogService.deleteCategory(id, no);
	    
	    return "redirect:/" + id + "/admin/category";
	}
	
	@Auth
	@RequestMapping("/admin/write")
	public String adminWrite(@PathVariable("id") String id) {
		return "blog/admin-write";
	}
}
