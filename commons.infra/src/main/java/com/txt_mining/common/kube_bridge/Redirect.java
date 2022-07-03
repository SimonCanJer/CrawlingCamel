package com.txt_mining.common.kube_bridge;


import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.logging.LogRecord;

/**
 * The class is to be used to remap probness and micrometer url  requests for Kubernetes inherent
 * url, when Kubernetes plugin is not used explicitly
 */
@Component
public class Redirect implements Filter {


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest= (HttpServletRequest) request;
        String uri= httpRequest.getRequestURI();
        String redirectUri=null;
        String prop="actuator.endpoint.redirect."+uri.substring(1);
        redirectUri=System.getProperty(prop);
        if(redirectUri!=null)
        {
            httpRequest.getRequestDispatcher(redirectUri).forward(request, response);
            return;
        }
        chain.doFilter(request, response);


    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
