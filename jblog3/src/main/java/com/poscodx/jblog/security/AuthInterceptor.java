package com.poscodx.jblog.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import com.poscodx.jblog.vo.UserVo;

public class AuthInterceptor implements HandlerInterceptor {

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {

    // 1. handler 종류 확인
    if (!(handler instanceof HandlerMethod)) {
      // DefaultServlet Handler가 처리하는 경우(정적 자원(e.g. /assets/**), mapping이 안된 url(e.g. /babo))
      return true;
    }

    // 2. handler casting (mapping 되어있는 핸들러라는 뜻)
    HandlerMethod handlerMethod = (HandlerMethod) handler; // HandlerMethod안에 @정보를 확인하는 메서드 존재

    // 3. Handler Method의 @Auth 가져오기
    Auth auth = handlerMethod.getMethodAnnotation(Auth.class);

    // 4. handlerMethod에 @Auth가 없는 경우 Type(Class)에 붙어 있는지 확인하는 추가적인 작업 필요
    if (auth == null) {
		auth = handlerMethod
				.getMethod()
				.getDeclaringClass()
				.getAnnotation(Auth.class);
    }
    
    if(auth == null) {
    	return true;
    }
    
    // 5. @Auth가 annotated 되어 있기 때문에 인증(Authentication) 확인
    HttpSession session = request.getSession();
    UserVo authUser = (UserVo) session.getAttribute("authUser");

    // 6. session에 authUser에 대한 인증이 안되어 잇는 경우
    if (authUser == null) {
      response.sendRedirect(request.getContextPath() + "/user/login");
      return false; // 뒤로가기 절대 불가!! handler 아직 존재
    }
    
    return true;
  }
}
