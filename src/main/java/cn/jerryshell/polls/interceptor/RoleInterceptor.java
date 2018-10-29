package cn.jerryshell.polls.interceptor;

import cn.jerryshell.polls.annotation.RoleRequired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RoleInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 如果 handler 不是 HandlerMethod 实例直接放行
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        // 如果 handler 没有 RoleRequired 注解直接放行
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        RoleRequired annotation = handlerMethod.getMethodAnnotation(RoleRequired.class);
        if (annotation == null) {
            return true;
        }

        // 校验 role
        String requestRole = (String) request.getAttribute("role");
        String[] requiredRoles = annotation.roles();
        for (String role : requiredRoles) {
            if (requestRole.equals(role)) {
                return true;
            }
        }
        return false;
    }
}
