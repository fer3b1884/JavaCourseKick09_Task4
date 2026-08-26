package by.shved.busbooking.controller;

import by.shved.busbooking.command.Command;
import by.shved.busbooking.command.CommandResult;
import by.shved.busbooking.command.CommandType;
import by.shved.busbooking.exception.CommandException;
import by.shved.busbooking.pool.ConnectionPool;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

@WebServlet(name = "controllerServlet", value = "/controller")
public class Controller extends HttpServlet {
    private static final Logger logger = LogManager.getLogger(Controller.class);

    @Override
    public void init() {
        ConnectionPool.getInstance();
        logger.info("Controller servlet initialized");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String commandStr = request.getParameter("command");
        Command command = CommandType.define(commandStr);
        try {
            CommandResult result = command.execute(request);
            if (result.isRedirect()) {
                response.sendRedirect(result.getPath());
            } else {
                request.getRequestDispatcher(result.getPath()).forward(request, response);
            }
        } catch (CommandException e) {
            logger.error("Command execution failed: {}", commandStr, e);
            request.setAttribute("error_msg", e.getMessage());
            request.getRequestDispatcher("/pages/error/error_500.jsp").forward(request, response);
        }
    }

    @Override
    public void destroy() {
        ConnectionPool.getInstance().destroyPool();
        logger.info("Controller servlet destroyed");
    }
}
