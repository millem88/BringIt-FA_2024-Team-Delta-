/// The AuthFilter class will do an authentication check on the use that is logging in.
/// this is now in the controller class.
/// Author(s): Darien Dalton, Jamie Mizelle
/// Date: 11/3/2024
package controllers;

import models.User;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import java.io.IOException;

@Component
@Order(1)
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);
        
        Object userObj = (session != null) ? session.getAttribute("user") : null;
        if (userObj == null || ((User) userObj).getId() == -1) {
        	
        	httpResponse.sendRedirect(httpRequest.getContextPath() + "/login");
            return;
        }
        
        chain.doFilter(request, response);
    }
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

        // Here, we will Initialization code, if needed

    }

    @Override
    public void destroy() {
        //Here we will do Cleanup code, if needed
    }

    @Bean
    public FilterRegistrationBean<AuthFilter> filterRegistrationBean() 
    { 
      // Filter Registration Bean 
      FilterRegistrationBean<AuthFilter> registrationBean = new FilterRegistrationBean<>();

    // Configure Authorization Filter 
      registrationBean.setFilter(new AuthFilter()); 
      // Specify URL Pattern 
      registrationBean.addUrlPatterns("");
      registrationBean.addUrlPatterns("/logout");
      registrationBean.addUrlPatterns("/");

      
      registrationBean.addUrlPatterns("/myEvents");
      registrationBean.addUrlPatterns("/newEvent");
      registrationBean.addUrlPatterns("/searchEvents");
      registrationBean.addUrlPatterns("/eventClicked");


      // Set the Execution Order of Filter 
      registrationBean.setOrder(1); 
      
      return registrationBean; 
    }
}