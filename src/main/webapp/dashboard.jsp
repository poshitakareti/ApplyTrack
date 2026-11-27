
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>ApplyTrack - Dashboard</title>
    <link rel="stylesheet" href="css/dashboard.css">
</head>
<body>

    <div class="dashboard-container">

        <header class="dashboard-header">
            <div>
                <h1>ApplyTrack</h1>
                <p>Student Placement Portal</p>
            </div>

            <a href="logout" class="logout-btn">Logout</a>
        </header>

        <main class="dashboard-content">

            <h2>Welcome, ${studentName}!</h2>

            <p>
                Welcome to your ApplyTrack dashboard.
                Track your placement applications in one place.
            </p>

            <!-- Application Statistics -->
            <div class="statistics-cards">

                <div class="stat-card">
                    <h3>Total Applications</h3>
                    <p class="stat-number">${totalApplications}</p>
                </div>

                <div class="stat-card">
                    <h3>Applied</h3>
                    <p class="stat-number">${appliedApplications}</p>
                </div>

                <div class="stat-card">
                    <h3>Interviews</h3>
                    <p class="stat-number">${interviewApplications}</p>
                </div>

                <div class="stat-card">
                    <h3>Offers Received</h3>
                    <p class="stat-number">${offerApplications}</p>
                </div>

            </div>

            <!-- Dashboard Actions -->
            <div class="dashboard-cards">

                <div class="dashboard-card">
                    <h3>My Applications</h3>

                    <p>
                        View and manage your job applications.
                    </p>

                    <a href="applications">
                        View Applications
                    </a>
                </div>

                <div class="dashboard-card">
                    <h3>Add Application</h3>

                    <p>
                        Record a new company and job opportunity.
                    </p>

                    <a href="add-application.jsp">
                        Add Application
                    </a>
                </div>

            </div>

        </main>

    </div>

</body>
</html>
