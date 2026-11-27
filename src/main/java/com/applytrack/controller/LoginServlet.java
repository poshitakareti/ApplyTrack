
package com.applytrack.controller;

import com.applytrack.dao.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if (email == null || password == null ||
                email.isBlank() || password.isBlank()) {
            response.sendRedirect("login.html?error=invalid");
            return;
        }

        email = email.trim();

        try {
            int userId = userDAO.loginUser(email, password);

            if (userId != -1) {
                HttpSession session = request.getSession();
                session.setAttribute("userId", userId);
                session.setAttribute("userEmail", email);

                response.sendRedirect("dashboard");
            } else {
                response.sendRedirect("login.html?error=credentials");
            }

        } catch (SQLException e) {
            getServletContext().log("Login failed", e);
            response.sendRedirect("login.html?error=failed");
        }
    }
}
