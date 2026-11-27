
<%@ page import="java.util.List" %>
<%@ page import="com.applytrack.model.Application" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    if (session.getAttribute("userId") == null) {
        response.sendRedirect("login.html");
        return;
    }

    List<Application> applications =
        (List<Application>) request.getAttribute("applications");
%>

<!DOCTYPE html>
<html>
<head>
    <title>My Applications - ApplyTrack</title>

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
           max-width: 1200px;
           margin: 0 auto;
           background: white;
           padding: 30px;
           border-radius: 12px;
           box-shadow: 0 4px 16px rgba(0, 0, 0, 0.08);
           overflow-x: auto;
       }

       h2 {
           color: #163A5F;
           text-align: center;
           margin: 0 0 25px;
           font-size: 28px;
       }

       /* Search and Filter */

       .filter-form {
           display: flex;
           align-items: center;
           gap: 10px;
           margin: 0 0 25px;
           padding: 18px;
           background: #f7f9fc;
           border: 1px solid #e1e7ef;
           border-radius: 8px;
           flex-wrap: wrap;
       }

       .filter-form input,
       .filter-form select {
           padding: 10px 12px;
           border: 1px solid #ccd5df;
           border-radius: 6px;
           font-size: 14px;
           background: white;
           color: #243653;
           height: 40px;
       }

       .filter-form input {
           width: 300px;
       }

       .filter-form select {
           width: 180px;
       }

       .filter-form input:focus,
       .filter-form select:focus {
           outline: none;
           border-color: #163A5F;
           box-shadow: 0 0 0 2px rgba(22, 58, 95, 0.1);
       }

       .filter-btn,
       .clear-btn {
           height: 40px;
           padding: 0 16px;
           border: none;
           border-radius: 6px;
           font-size: 14px;
           font-weight: 600;
           text-decoration: none;
           cursor: pointer;
           display: inline-flex;
           align-items: center;
           justify-content: center;
       }

       .filter-btn {
           background: #163A5F;
           color: white;
       }

       .filter-btn:hover {
           background: #245987;
       }

       .clear-btn {
           background: #e5eaf0;
           color: #243653;
       }

       .clear-btn:hover {
           background: #d5dde6;
       }

       /* Success Message */

       .success {
           color: #198754;
           background: #eaf7ef;
           border: 1px solid #b9e3c7;
           padding: 10px 15px;
           border-radius: 6px;
           text-align: center;
           margin: 15px 0;
           font-weight: 600;
       }

       /* Top Navigation */

       .top-links {
           margin: 20px 0;
           display: flex;
           justify-content: space-between;
           flex-wrap: wrap;
           gap: 10px;
       }

       .top-links a {
           background: #163A5F;
           color: white;
           padding: 10px 16px;
           text-decoration: none;
           border-radius: 6px;
           font-size: 14px;
           font-weight: 600;
           transition: background 0.2s ease;
       }

       .top-links a:hover {
           background: #245987;
       }

       /* Applications Table */

       .table-wrapper {
           width: 100%;
           overflow-x: auto;
           margin-top: 20px;
           border: 1px solid #dfe5ec;
           border-radius: 8px;
       }

       table {
           width: 100%;
           border-collapse: collapse;
           min-width: 900px;
       }

       th,
       td {
           padding: 13px 14px;
           border-bottom: 1px solid #e1e6ed;
           text-align: left;
           font-size: 14px;
       }

       th {
           background: #163A5F;
           color: white;
           font-weight: 600;
           white-space: nowrap;
       }

       td {
           color: #34445a;
           vertical-align: middle;
       }

       tr:nth-child(even) {
           background: #f8fafc;
       }

       tr:hover {
           background: #eef4fa;
       }

       td a:not(.edit-btn) {
           color: #163A5F;
           font-weight: 600;
           text-decoration: none;
       }

       td a:not(.edit-btn):hover {
           text-decoration: underline;
       }

       /* Action Buttons */

       .action-buttons {
           display: flex;
           align-items: center;
           gap: 8px;
           white-space: nowrap;
       }

       .action-buttons form {
           margin: 0;
       }

       .edit-btn,
       .delete-btn {
           display: inline-flex;
           align-items: center;
           justify-content: center;
           padding: 7px 13px;
           font-size: 13px;
           font-weight: 600;
           border: none;
           border-radius: 5px;
           text-decoration: none;
           cursor: pointer;
           min-width: 55px;
       }

       .edit-btn {
           background-color: #163A5F;
           color: white !important;
       }

       .edit-btn:hover {
           background-color: #245987;
           color: white !important;
           text-decoration: none !important;
       }

       .delete-btn {
           background-color: #dc3545;
           color: white;
       }

       .delete-btn:hover {
           background-color: #b02a37;
       }

       /* Empty State */

       .empty {
           text-align: center;
           padding: 50px 20px;
           color: #666;
           background: #f8fafc;
           border: 1px dashed #cbd5e1;
           border-radius: 8px;
           margin-top: 20px;
       }

       .empty h3 {
           color: #163A5F;
           margin-bottom: 10px;
       }

       .empty p {
           margin: 0;
       }

       /* Mobile Responsive Design */

       @media (max-width: 768px) {

           body {
               padding: 15px;
           }

           .container {
               padding: 20px 15px;
               border-radius: 8px;
           }

           h2 {
               font-size: 23px;
           }

           .filter-form {
               flex-direction: column;
               align-items: stretch;
           }

           .filter-form input,
           .filter-form select,
           .filter-btn,
           .clear-btn {
               width: 100%;
           }

           .top-links {
               flex-direction: column;
           }

           .top-links a {
               text-align: center;
           }

           .table-wrapper {
               overflow-x: auto;
           }
       }
   </style>

</head>

<body>

<div class="container">

    <h2>My Placement Applications</h2>
        <!-- Search and Filter -->
        <form action="applications" method="get" class="filter-form">

            <input
                type="text"
                name="search"
                placeholder="Search company or job role..."
                value="<%= request.getAttribute("search") != null
                        ? request.getAttribute("search")
                        : "" %>"
            >

            <select name="status">
                <option value="All"
                    <%= "All".equals(request.getAttribute("status"))
                            || request.getAttribute("status") == null
                            ? "selected" : "" %>>
                    All Statuses
                </option>

                <option value="Saved"
                    <%= "Saved".equals(request.getAttribute("status"))
                            ? "selected" : "" %>>
                    Saved
                </option>

                <option value="Applied"
                    <%= "Applied".equals(request.getAttribute("status"))
                            ? "selected" : "" %>>
                    Applied
                </option>

                <option value="Interview"
                    <%= "Interview".equals(request.getAttribute("status"))
                            ? "selected" : "" %>>
                    Interview
                </option>

                <option value="Offer Received"
                    <%= "Offer Received".equals(request.getAttribute("status"))
                            ? "selected" : "" %>>
                    Offer Received
                </option>

                <option value="Rejected"
                    <%= "Rejected".equals(request.getAttribute("status"))
                            ? "selected" : "" %>>
                    Rejected
                </option>
            </select>

            <button type="submit" class="filter-btn">
                Search
            </button>

            <a href="applications" class="clear-btn">
                Clear
            </a>

        </form>
    <%
        if ("added".equals(request.getParameter("success"))) {
    %>
        <p class="success">Application added successfully!</p>
    <%
        } else if ("updated".equals(request.getParameter("success"))) {
    %>
        <p class="success">Application updated successfully!</p>
    <%
        } else if ("deleted".equals(request.getParameter("success"))) {
    %>
        <p class="success">Application deleted successfully!</p>
    <%
        }
    %>

    <div class="top-links">
        <a href="add-application.jsp">+ Add Application</a>
        <a href="dashboard">Back to Dashboard</a>
    </div>

    <% if (applications != null && !applications.isEmpty()) { %>

    <div class="table-wrapper">
        <table>
            <tr>
                <th>Company</th>
                <th>Job Role</th>
                <th>Application Date</th>
                <th>Deadline</th>
                <th>Status</th>
                <th>Job Link</th>
                <th>Actions</th>
            </tr>

            <% for (Application app : applications) { %>

                <tr>
                    <td><%= app.getCompanyName() %></td>
                    <td><%= app.getJobRole() %></td>

                    <td>
                        <%= app.getApplicationDate() == null
                            ? "-" : app.getApplicationDate() %>
                    </td>

                    <td>
                        <%= app.getDeadline() == null
                            ? "-" : app.getDeadline() %>
                    </td>

                    <td><%= app.getStatus() %></td>

                    <td>
                        <% if (app.getJobLink() != null
                                && !app.getJobLink().isBlank()) { %>
                            <a href="<%= app.getJobLink() %>"
                               target="_blank"
                               rel="noopener noreferrer">View Job</a>
                        <% } else { %>
                            -
                        <% } %>
                    </td>

<td class="action-buttons">

    <a href="edit-application?id=<%= app.getApplicationId() %>"
       class="edit-btn">
        Edit
    </a>

    <form action="delete-application" method="post"
          onsubmit="return confirm('Are you sure you want to delete this application?');">

        <input type="hidden"
               name="applicationId"
               value="<%= app.getApplicationId() %>">

        <button type="submit" class="delete-btn">
            Delete
        </button>

    </form>

</td>


                </tr>

            <% } %>

        </table>
        </div>

    <% } else { %>

        <div class="empty">
            <h3>No applications found!</h3>
            <p>Click on Add Application to record your first placement application.</p>
        </div>

    <% } %>

</div>

</body>
</html>
