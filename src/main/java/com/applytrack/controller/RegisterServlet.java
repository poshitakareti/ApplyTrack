
package com.applytrack.controller;

import com.applytrack.dao.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword =
                request.getParameter("confirmPassword");

        if (name == null || email == null ||
                password == null || confirmPassword == null) {
            response.sendRedirect("register.html?error=invalid");
            return;
        }

        name = name.trim();
        email = email.trim();

        if (name.isBlank() || email.isBlank() ||
                password.isBlank() || confirmPassword.isBlank()) {
            response.sendRedirect("register.html?error=invalid");
            return;
        }

        if (name.length() > 100 || email.length() > 150) {
            response.sendRedirect("register.html?error=invalid");
            return;
        }

        if (!email.matches(
                "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            response.sendRedirect("register.html?error=invalid");
            return;
        }

        if (password.length() < 8 || password.length() > 72) {
            response.sendRedirect("register.html?error=password");
            return;
        }

        if (!password.equals(confirmPassword)) {
            response.sendRedirect("register.html?error=mismatch");
            return;
        }

        try {
            boolean registered =
                    userDAO.registerUser(name, email, password);

            if (registered) {
                response.sendRedirect("register.html?success=true");
            } else {
                response.sendRedirect("register.html?error=failed");
            }

        } catch (SQLException e) {
            if ("23000".equals(e.getSQLState())) {
                response.sendRedirect(
                        "register.html?error=duplicate");
            } else {
                e.printStackTrace();
                getServletContext().log(
                        "Registration failed", e);
                response.sendRedirect(
                        "register.html?error=failed");
            }
        }
    }
}
