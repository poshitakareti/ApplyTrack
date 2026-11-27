package com.applytrack.controller;

import com.applytrack.dao.ApplicationDAO;
import com.applytrack.model.Application;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet("/applications")
public class ViewApplicationsServlet extends HttpServlet {

    private final ApplicationDAO applicationDAO =
            new ApplicationDAO();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        // Check whether the user is logged in
        if (session == null ||
                session.getAttribute("userId") == null) {

            response.sendRedirect("login.html");
            return;
        }

        try {

            int userId =
                    (Integer) session.getAttribute("userId");

            // Get search and filter values
            String search = request.getParameter("search");
            String status = request.getParameter("status");

            // Get filtered applications
            List<Application> applications =
                    applicationDAO.searchApplications(
                            userId,
                            search,
                            status
                    );

            // Send data to JSP
            request.setAttribute(
                    "applications", applications);

            request.setAttribute(
                    "search", search);

            request.setAttribute(
                    "status", status);

            request.getRequestDispatcher(
                            "/applications.jsp")
                    .forward(request, response);

        } catch (Exception e) {

            throw new ServletException(
                    "Unable to load applications",
                    e
            );
        }
    }
}
