
<%@ page import="com.applytrack.model.Application" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    if (session.getAttribute("userId") == null) {
        response.sendRedirect("login.html");
        return;
    }

    Application app =
        (Application) request.getAttribute("application");

    if (app == null) {
        response.sendRedirect("applications");
        return;
    }
%>

<!DOCTYPE html>
<html>
<head>
    <title>Edit Application - ApplyTrack</title>

    <style>
        * {
            box-sizing: border-box;
        }

        body {
            font-family: Arial, sans-serif;
            background: #f2f5fa;
            margin: 0;
            padding: 30px;
            color: #243653;
        }

        .container {
            width: 100%;
            max-width: 520px;
            margin: 30px auto;
            background: white;
            padding: 32px;
            border-radius: 12px;
            box-shadow: 0 4px 16px rgba(0, 0, 0, 0.08);
        }

        h2 {
            text-align: center;
            color: #163A5F;
            margin: 0 0 25px;
            font-size: 26px;
        }

        .form-group {
            margin-bottom: 17px;
        }

        label {
            display: block;
            margin-bottom: 7px;
            font-weight: 600;
            color: #34445a;
            font-size: 14px;
        }

        input,
        select,
        textarea {
            width: 100%;
            padding: 11px 12px;
            border: 1px solid #ccd5df;
            border-radius: 6px;
            font-size: 14px;
            font-family: Arial, sans-serif;
            color: #243653;
            background: white;
            transition: border-color 0.2s ease, box-shadow 0.2s ease;
        }

        input,
        select {
            height: 42px;
        }

        textarea {
            height: 95px;
            resize: vertical;
        }

        input:focus,
        select:focus,
        textarea:focus {
            outline: none;
            border-color: #163A5F;
            box-shadow: 0 0 0 2px rgba(22, 58, 95, 0.1);
        }

        input::placeholder,
        textarea::placeholder {
            color: #8a96a6;
        }

        .error {
            color: #b02a37;
            background: #fdecec;
            border: 1px solid #f3c2c7;
            padding: 10px 12px;
            border-radius: 6px;
            text-align: center;
            margin: 0 0 20px;
            font-size: 14px;
        }

        .update-btn {
            width: 100%;
            margin-top: 5px;
            padding: 12px;
            background: #163A5F;
            color: white;
            border: none;
            border-radius: 6px;
            cursor: pointer;
            font-size: 15px;
            font-weight: 600;
            transition: background 0.2s ease;
        }

        .update-btn:hover {
            background: #245987;
        }

        .back {
            display: block;
            text-align: center;
            margin-top: 18px;
            color: #163A5F;
            text-decoration: none;
            font-size: 14px;
            font-weight: 600;
        }

        .back:hover {
            text-decoration: underline;
        }

        @media (max-width: 600px) {
            body {
                padding: 15px;
            }

            .container {
                margin: 15px auto;
                padding: 25px 20px;
            }

            h2 {
                font-size: 23px;
            }
        }
    </style>
</head>

<body>

<div class="container">

    <h2>Edit Placement Application</h2>

    <% if ("required".equals(request.getParameter("error"))) { %>
        <p class="error">Company name and job role are required.</p>
    <% } else if ("status".equals(request.getParameter("error"))) { %>
        <p class="error">Please select a valid status.</p>
    <% } else if ("failed".equals(request.getParameter("error"))) { %>
        <p class="error">Unable to update application.</p>
    <% } %>

    <form action="edit-application" method="post">

        <input type="hidden"
               name="applicationId"
               value="<%= app.getApplicationId() %>">

        <div class="form-group">
            <label for="companyName">Company Name</label>
            <input
                    type="text"
                    id="companyName"
                    name="companyName"
                    value="<%= app.getCompanyName() %>"
                    required>
        </div>

        <div class="form-group">
            <label for="jobRole">Job Role</label>
            <input
                    type="text"
                    id="jobRole"
                    name="jobRole"
                    value="<%= app.getJobRole() %>"
                    required>
        </div>

        <div class="form-group">
            <label for="applicationDate">Application Date</label>
            <input
                    type="date"
                    id="applicationDate"
                    name="applicationDate"
                    value="<%= app.getApplicationDate() == null
                        ? "" : app.getApplicationDate() %>">
        </div>

        <div class="form-group">
            <label for="deadline">Application Deadline</label>
            <input
                    type="date"
                    id="deadline"
                    name="deadline"
                    value="<%= app.getDeadline() == null
                        ? "" : app.getDeadline() %>">
        </div>

        <div class="form-group">
            <label for="status">Application Status</label>
            <select id="status" name="status" required>

                <option value="Saved"
                    <%= "Saved".equals(app.getStatus())
                        ? "selected" : "" %>>
                    Saved
                </option>

                <option value="Applied"
                    <%= "Applied".equals(app.getStatus())
                        ? "selected" : "" %>>
                    Applied
                </option>

                <option value="Interview"
                    <%= "Interview".equals(app.getStatus())
                        ? "selected" : "" %>>
                    Interview
                </option>

                <option value="Offer Received"
                    <%= "Offer Received".equals(app.getStatus())
                        ? "selected" : "" %>>
                    Offer Received
                </option>

                <option value="Rejected"
                    <%= "Rejected".equals(app.getStatus())
                        ? "selected" : "" %>>
                    Rejected
                </option>

            </select>
        </div>

        <div class="form-group">
            <label for="jobLink">Job Link</label>
            <input
                    type="url"
                    id="jobLink"
                    name="jobLink"
                    value="<%= app.getJobLink() == null
                        ? "" : app.getJobLink() %>">
        </div>

        <div class="form-group">
            <label for="notes">Notes</label>
            <textarea
                    id="notes"
                    name="notes"><%= app.getNotes() == null
                        ? "" : app.getNotes() %></textarea>
        </div>

        <button type="submit" class="update-btn">
            Update Application
        </button>

    </form>

    <a href="applications" class="back">Back to Applications</a>

</div>

</body>
</html>
