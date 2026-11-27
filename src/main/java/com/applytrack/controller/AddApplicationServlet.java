
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
import java.sql.Date;

@WebServlet("/add-application")
public class AddApplicationServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        // Check whether the user is logged in
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect("login.html");
            return;
        }

        try {
            int userId = (Integer) session.getAttribute("userId");

            String companyName = request.getParameter("companyName");
            String jobRole = request.getParameter("jobRole");
            String applicationDate = request.getParameter("applicationDate");
            String deadline = request.getParameter("deadline");
            String status = request.getParameter("status");
            String jobLink = request.getParameter("jobLink");
            String notes = request.getParameter("notes");

            if (companyName == null || companyName.trim().isEmpty()
                    || jobRole == null || jobRole.trim().isEmpty()) {
                response.sendRedirect("add-application.jsp?error=required");
                return;
            }

            Application app = new Application();

            app.setUserId(userId);
            app.setCompanyName(companyName.trim());
            app.setJobRole(jobRole.trim());

            app.setApplicationDate(
                    applicationDate == null || applicationDate.isBlank()
                            ? null : Date.valueOf(applicationDate)
            );

            app.setDeadline(
                    deadline == null || deadline.isBlank()
                            ? null : Date.valueOf(deadline)
            );

            app.setStatus(status);
            app.setJobLink(jobLink);
            app.setNotes(notes);

            ApplicationDAO dao = new ApplicationDAO();

            if (dao.addApplication(app)) {
                response.sendRedirect("applications?success=added");
            } else {
                response.sendRedirect("add-application.jsp?error=failed");
            }

        } catch (IllegalArgumentException e) {
            response.sendRedirect("add-application.jsp?error=invaliddate");
        } catch (Exception e) {
            throw new ServletException("Unable to add application", e);
        }
    }
}
