package com.example.epos.filter;

import com.alibaba.fastjson.JSON;
import com.example.epos.common.BaseContext;
import com.example.epos.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//check if the user has login or not
@Slf4j
@WebFilter(filterName = "loginCheckFilter",urlPatterns = "/*")
public class LoginCheckFilter implements Filter {
    //    path matcher
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        //Get the current request URI
        String requestURI = request.getRequestURI();
        //whether it needs process
        String[] urls = new String[]
                {
                        "/employee/login",
                        "/employee/logout",
                        "/backend/**",
                        "/front/**",
                        "/user/login",
                        "/user/sendMsg",
                        "/send-mail/**",
                        "/**"
                };
        boolean check = check(urls,requestURI);
        if (check)
        {
            filterChain.doFilter(request,response);
            return;
        }
        //if employee login in
        if(request.getSession().getAttribute("employee")!=null)
        {
            log.info("User has been logged in");
            Long empId = (Long) request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(empId);
            filterChain.doFilter(request,response);
            return;
        }
        //if user login in
        if(request.getSession().getAttribute("user")!=null)
        {
            log.info("User has been logged in");
            Long userId = (Long) request.getSession().getAttribute("user");
            BaseContext.setCurrentId(userId);
            filterChain.doFilter(request,response);
            return;
        }
        //if not logged, return the not result which is not logged, and return the data
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;


    }

//check the matched path
public boolean check(String[] urls, String requestURI)
{
    for(String url:urls)
    {
       boolean match= PATH_MATCHER.match(url,requestURI);
       if(match)
       {
           return true;
       }
    }
    return false;
}
}





