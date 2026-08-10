package by.shved.busbooking.controller;

import java.io.*;

import by.shved.busbooking.command.Command;
import by.shved.busbooking.command.CommandType;
import by.shved.busbooking.exception.CommandException;
import by.shved.busbooking.pool.ConnectionPool;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@WebServlet(name = "helloServlet", value = "/controller")
public class Controller extends HttpServlet {
    private static final Logger logger = LogManager.getLogger();

    public void init() {
        ConnectionPool.getInstance();
        logger.log(Level.INFO, "Servlet Init: ");
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/html");
        String commandStr = request.getParameter("command");
        Command command = CommandType.define(commandStr);
        String page;
        try {
            page = command.execute(request);
            request.getRequestDispatcher(page).forward(request, response);
        } catch (CommandException e) {
//            response.sendError(500); // 1 variant
//            throw new ServletException(e); // 2 variant
            request.setAttribute("error_msg", e.getMessage()); // 3 variant
            request.getRequestDispatcher("pages/error/error_500.jsp").forward(request, response); // 3 variant
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    public void destroy() {
        ConnectionPool.getInstance().destroyPool();
        logger.log(Level.INFO, "Servlet Destroyed: ");
    }
}