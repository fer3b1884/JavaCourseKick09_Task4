package by.shved.busbooking.command;

import by.shved.busbooking.entity.User;
import by.shved.busbooking.util.SessionKeys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.List;

public final class CommandUtil {

    public static String param(HttpServletRequest request, String name) {
        String value = request.getParameter(name);
        return value == null ? "" : value.trim();
    }

    public static User getCurrentUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }
        return (User) session.getAttribute(SessionKeys.USER);
    }

    public static void setFlashMessage(HttpServletRequest request, String message) {
        request.getSession().setAttribute(SessionKeys.FLASH_MESSAGE, message);
    }

    public static void setFlashError(HttpServletRequest request, String message) {
        request.getSession().setAttribute(SessionKeys.FLASH_ERROR, message);
    }

    public static void transferFlashMessages(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return;
        }
        Object message = session.getAttribute(SessionKeys.FLASH_MESSAGE);
        Object error = session.getAttribute(SessionKeys.FLASH_ERROR);
        if (message != null) {
            request.setAttribute("message", message);
            session.removeAttribute(SessionKeys.FLASH_MESSAGE);
        }
        if (error != null) {
            request.setAttribute("error", error);
            session.removeAttribute(SessionKeys.FLASH_ERROR);
        }
    }
}