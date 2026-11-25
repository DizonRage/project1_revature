package org.example.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.example.entity.User;
import org.example.exception.ForbiddenException;
import org.example.exception.UnauthorizedException;

public final class SessionUtil {

    public static final String USER_ID = "userId";
    public static final String ROLE = "role";
    private static final String ADMIN = "ADMIN";

    private SessionUtil() {
    }

    /** Replaces any existing session so that a pre-login session id is never reused. */
    public static void signIn(HttpServletRequest request, User user) {
        signOut(request);
        HttpSession session = request.getSession(true);
        session.setAttribute(USER_ID, user.getId());
        session.setAttribute(ROLE, user.getRole());
    }

    public static void signOut(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }

    public static Integer getUserId(HttpSession session) {
        if (session.getAttribute(USER_ID) instanceof Integer userId) {
            return userId;
        }
        throw new UnauthorizedException("Login required");
    }

    public static String getRole(HttpSession session) {
        if (session.getAttribute(ROLE) instanceof String role) {
            return role;
        }
        throw new UnauthorizedException("Login required");
    }

    public static void requireAdmin(HttpSession session) {
        if (!ADMIN.equals(getRole(session))) {
            throw new ForbiddenException("Admin access required");
        }
    }
}
