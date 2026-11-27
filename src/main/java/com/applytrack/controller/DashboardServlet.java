
package com.applytrack.controller;

import com.applytrack.dao.ApplicationDAO;
import com.applytrack.dao.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect("login.html");
            return;
        }

        try {

            int userId = (Integer) session.getAttribute("userId");

            String studentName = userDAO.getUserNameById(userId);

            // Get application statistics
            ApplicationDAO applicationDAO = new ApplicationDAO();

            int totalApplications =
                    applicationDAO.getTotalApplications(userId);

            int appliedApplications =
                    applicationDAO.getApplicationsByStatus(
                            userId, "Applied");

            int interviewApplications =
                    applicationDAO.getApplicationsByStatus(
                            userId, "Interview");

            int offerApplications =
                    applicationDAO.getApplicationsByStatus(
                            userId, "Offer Received");

            // Send student name and statistics to dashboard
            request.setAttribute("studentName", studentName);

            request.setAttribute(
                    "totalApplications", totalApplications);

            request.setAttribute(
                    "appliedApplications", appliedApplications);

            request.setAttribute(
                    "interviewApplications", interviewApplications);

            request.setAttribute(
                    "offerApplications", offerApplications);

            // Open dashboard page
            request.getRequestDispatcher("/dashboard.jsp")
                    .forward(request, response);

        } catch (Exception e) {

            getServletContext().log(
                    "Dashboard loading failed", e);

            throw new ServletException(
                    "Unable to load dashboard.", e);
        }
    }
}
