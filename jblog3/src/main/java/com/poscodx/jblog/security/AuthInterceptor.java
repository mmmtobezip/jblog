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
    // 로그인 했으니까 @Auth 가져올 수 있음
    Auth auth = handlerMethod.getMethodAnnotation(Auth.class); // @Auth가 존재하는 클래스명
    Auth adminRole = handlerMethod.getMethod().getDeclaringClass().getAnnotation(Auth.class);

    // *과제
    // auth가 null인 경우
    // 4. handlerMethod에 @Auth가 없는 경우 Type(Class)에 붙어 있는지 확인하는 추가적인 작업 필요
    if (auth == null) {
      auth = adminRole;
    }

    // 5. Type이나 Method에 @Auth가 없는 경우 -> 메인페이지의 경우(User, Board 등)
    if (auth == null) {
      return true;
    }

    // 6. @Auth가 annotated 되어 있기 때문에 인증(Authentication) 확인
    HttpSession session = request.getSession();
    UserVo authUser = (UserVo) session.getAttribute("authUser");

    // 7. session에 authUser에 대한 인증이 안되어 잇는 경우
    if (authUser == null) {
      response.sendRedirect(request.getContextPath() + "/user/login");
      return false; // 뒤로가기 절대 불가!! handler 아직 존재ㄴ
    }

    // 8. 권한(Authorization) 체크를 위해 @Auth의 role 가져오기("USER", "ADMIN")
    String role = auth.role();

    // 9. @Auth role이 "USER"인 경우, authUser의 role은 상관없다.
    // 인증 완료 시 통과되어 Controller 내 @Auth로 이동
    if ("USER".equals(role)) {
      return true;
    }

    // 10. @Auth role이 "ADMIN"인 경우, authUser의 role은 반드시 "ADMIN"
    // 8번에서 체크하고 내려온 경우에 해당
//    if (!"ADMIN".equals(authUser.getRole())) {
//      // ADMIN이 아니라면 USER role
//      // 메인으로 보내기
//      response.sendRedirect(request.getContextPath());
//      return false;
//    }

    // @Auth 붙어있고, 인증도 된 경우
    // 11. @Auth(role="ADMIN"), authUser.getRole() == "ADMIN"
    // 옳은 관리자만이 해당 라인을 탐
    return true;
  }
}
