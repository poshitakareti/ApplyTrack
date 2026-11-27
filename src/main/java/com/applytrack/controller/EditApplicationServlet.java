
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
import java.util.Set;

@WebServlet("/edit-application")
public class EditApplicationServlet extends HttpServlet {

    private final ApplicationDAO dao = new ApplicationDAO();

    private static final Set<String> VALID_STATUSES = Set.of(
            "Saved",
            "Applied",
            "Interview",
            "Offer Received",
            "Rejected"
    );

    // Display the selected application's existing details
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
            int applicationId = Integer.parseInt(
                    request.getParameter("id")
            );

            Application app =
                    dao.getApplicationById(applicationId, userId);

            if (app == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND,
                        "Application not found.");
                return;
            }

            request.setAttribute("application", app);

            request.getRequestDispatcher("/edit-application.jsp")
                    .forward(request, response);

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid application ID.");
        } catch (Exception e) {
            throw new ServletException(
                    "Unable to retrieve application", e);
        }
    }

    // Save the updated application details
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

            // Check that the application belongs to this student
            Application app =
                    dao.getApplicationById(applicationId, userId);

            if (app == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND,
                        "Application not found.");
                return;
            }

            String companyName = request.getParameter("companyName");
            String jobRole = request.getParameter("jobRole");
            String applicationDate = request.getParameter("applicationDate");
            String deadline = request.getParameter("deadline");
            String status = request.getParameter("status");
            String jobLink = request.getParameter("jobLink");
            String notes = request.getParameter("notes");

            if (companyName == null || companyName.isBlank()
                    || jobRole == null || jobRole.isBlank()) {
                response.sendRedirect(
                        "edit-application?id=" + applicationId
                                + "&error=required");
                return;
            }

            if (!VALID_STATUSES.contains(status)) {
                response.sendRedirect(
                        "edit-application?id=" + applicationId
                                + "&error=status");
                return;
            }

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

            if (dao.updateApplication(app)) {
                response.sendRedirect("applications?success=updated");
            } else {
                response.sendRedirect(
                        "edit-application?id=" + applicationId
                                + "&error=failed");
            }

        } catch (IllegalArgumentException e) {
            response.sendRedirect("applications?error=invalid");
        } catch (Exception e) {
            throw new ServletException(
                    "Unable to update application", e);
        }
    }
}
