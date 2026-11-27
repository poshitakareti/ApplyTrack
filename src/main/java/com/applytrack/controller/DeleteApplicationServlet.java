
package com.applytrack.controller;

import com.applytrack.dao.ApplicationDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/delete-application")
public class DeleteApplicationServlet extends HttpServlet {

    private final ApplicationDAO dao = new ApplicationDAO();

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect("login.html");
            return;
        }

        try {
            int userId = (Integer) session.getAttribute("userId");

            int applicationId = Integer.parseInt(
                    request.getParameter("applicationId")
            );

            boolean deleted =
                    dao.deleteApplication(applicationId, userId);

            if (deleted) {
                response.sendRedirect("applications?success=deleted");
            } else {
                response.sendRedirect("applications?error=deletefailed");
            }

        } catch (NumberFormatException e) {
            response.sendRedirect("applications?error=invalid");
        } catch (Exception e) {
            throw new ServletException(
                    "Unable to delete application", e);
        }
    }
}
