package cn.jerryshell.polls.interceptor;

import cn.jerryshell.polls.annotation.TokenRequired;
import cn.jerryshell.polls.util.JWTUtil;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TokenInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 如果 handler 不是 HandlerMethod 实例直接放行
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        // 如果 handler 没有 TokenRequired 注解直接放行
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        boolean isTokenRequired = handlerMethod.hasMethodAnnotation(TokenRequired.class);
        if (!isTokenRequired) {
            return true;
        }

        // 校验 token
        String token = request.getHeader("Authorization");
        if (!JWTUtil.verify(token)) {
            return false;
        }

        // 验证通过，在请求域设置相应的属性
        String username = JWTUtil.getUsername(token);
        String role = JWTUtil.getRole(token);
        request.setAttribute("username", username);
        request.setAttribute("role", role);
        return true;
    }
}
