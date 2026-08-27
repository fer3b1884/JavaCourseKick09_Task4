package by.shved.busbooking.controller.filter;

import by.shved.busbooking.entity.User;
import by.shved.busbooking.entity.UserRoleType;
import by.shved.busbooking.util.SessionKeys;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Set;

@WebFilter(filterName = "AuthFilter", urlPatterns = {"/*"})
public class AuthFilter implements Filter {

    private static final Set<String> PUBLIC_COMMANDS = Set.of(
            "login", "add_user", "default", "register"
    );

    private static final Set<String> ADMIN_COMMANDS = Set.of(
            "admin_buses", "save_bus", "delete_bus",
            "admin_routes", "save_route", "delete_route",
            "admin_trips", "save_trip", "delete_trip",
            "admin_reports"
    );

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String servletPath = httpRequest.getServletPath();
        String command = httpRequest.getParameter("command");
        if (servletPath.startsWith("/css/") || servletPath.startsWith("/js/") || servletPath.startsWith("/images/")) {
            chain.doFilter(request, response);
            return;
        }
        if ("/index.jsp".equals(servletPath) || "/".equals(servletPath)) {
            chain.doFilter(request, response);
            return;
        }
        if ("/controller".equals(servletPath) && command != null && PUBLIC_COMMANDS.contains(command.toLowerCase())) {
            chain.doFilter(request, response);
            return;
        }
        HttpSession session = httpRequest.getSession(false);
        User user = session == null ? null : (User) session.getAttribute(SessionKeys.USER);
        if (user == null) {
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/index.jsp");
            return;
        }
        if (command != null && ADMIN_COMMANDS.contains(command.toLowerCase()) && user.getRole() != UserRoleType.ADMIN) {
            httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        if (servletPath.startsWith("/WEB-INF/jsp/admin") && user.getRole() != UserRoleType.ADMIN) {
            httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        chain.doFilter(request, response);
    }
}