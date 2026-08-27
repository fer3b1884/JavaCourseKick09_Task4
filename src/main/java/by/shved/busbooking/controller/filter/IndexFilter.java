package by.shved.busbooking.controller.filter;

import by.shved.busbooking.command.CommandUtil;
import by.shved.busbooking.entity.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter(filterName = "IndexFilter", urlPatterns = {"/index.jsp", ""})
public class IndexFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        User user = CommandUtil.getCurrentUser(httpRequest);
        if (user != null) {
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/controller?command=main");
            return;
        }
        chain.doFilter(request, response);
    }
}