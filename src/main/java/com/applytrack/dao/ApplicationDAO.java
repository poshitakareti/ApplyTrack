
package com.applytrack.dao;

import com.applytrack.model.Application;
import com.applytrack.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ApplicationDAO {

    // 1. Add a new application
    public boolean addApplication(Application app)
            throws SQLException {

        String sql = """
                INSERT INTO applications
                (user_id, company_name, job_role,
                 application_date, deadline, status,
                 job_link, notes)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, app.getUserId());
            statement.setString(2, app.getCompanyName());
            statement.setString(3, app.getJobRole());
            statement.setDate(4, app.getApplicationDate());
            statement.setDate(5, app.getDeadline());
            statement.setString(6, app.getStatus());
            statement.setString(7, app.getJobLink());
            statement.setString(8, app.getNotes());

            return statement.executeUpdate() == 1;
        }
    }

    // 2. View applications belonging to a student
    public List<Application> getApplicationsByUserId(int userId)
            throws SQLException {

        List<Application> applications = new ArrayList<>();

        String sql = """
                SELECT * FROM applications
                WHERE user_id = ?
                ORDER BY created_at DESC
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, userId);

            try (ResultSet result = statement.executeQuery()) {

                while (result.next()) {

                    Application app = new Application();

                    app.setApplicationId(
                            result.getInt("application_id"));
                    app.setUserId(result.getInt("user_id"));
                    app.setCompanyName(
                            result.getString("company_name"));
                    app.setJobRole(result.getString("job_role"));
                    app.setApplicationDate(
                            result.getDate("application_date"));
                    app.setDeadline(result.getDate("deadline"));
                    app.setStatus(result.getString("status"));
                    app.setJobLink(result.getString("job_link"));
                    app.setNotes(result.getString("notes"));

                    applications.add(app);
                }
            }
        }

        return applications;
    }

    // Search and filter applications for a user
    public List<Application> searchApplications(
            int userId,
            String search,
            String status) throws Exception {

        StringBuilder sql = new StringBuilder(
                "SELECT * FROM applications WHERE user_id = ?"
        );

        if (search != null && !search.trim().isEmpty()) {
            sql.append(
                    " AND (company_name LIKE ? OR job_role LIKE ?)"
            );
        }

        if (status != null && !status.trim().isEmpty()
                && !status.equals("All")) {
            sql.append(" AND status = ?");
        }

        sql.append(" ORDER BY created_at DESC");

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql.toString())) {

            int parameterIndex = 1;

            statement.setInt(parameterIndex++, userId);

            if (search != null && !search.trim().isEmpty()) {

                String searchPattern = "%" + search.trim() + "%";

                statement.setString(parameterIndex++, searchPattern);
                statement.setString(parameterIndex++, searchPattern);
            }

            if (status != null && !status.trim().isEmpty()
                    && !status.equals("All")) {

                statement.setString(parameterIndex++, status);
            }

            List<Application> applications = new ArrayList<>();

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {

                    Application app = new Application();

                    app.setApplicationId(
                            resultSet.getInt("application_id"));

                    app.setUserId(
                            resultSet.getInt("user_id"));

                    app.setCompanyName(
                            resultSet.getString("company_name"));

                    app.setJobRole(
                            resultSet.getString("job_role"));

                    app.setApplicationDate(
                            resultSet.getDate("application_date"));

                    app.setDeadline(
                            resultSet.getDate("deadline"));

                    app.setStatus(
                            resultSet.getString("status"));

                    app.setJobLink(
                            resultSet.getString("job_link"));

                    app.setNotes(
                            resultSet.getString("notes"));

                    applications.add(app);
                }
            }

            return applications;
        }
    }


    // 3. Get one application belonging to a student
    public Application getApplicationById(int applicationId,
                                          int userId)
            throws SQLException {

        String sql = """
                SELECT * FROM applications
                WHERE application_id = ? AND user_id = ?
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, applicationId);
            statement.setInt(2, userId);

            try (ResultSet result = statement.executeQuery()) {

                if (result.next()) {

                    Application app = new Application();

                    app.setApplicationId(
                            result.getInt("application_id"));
                    app.setUserId(result.getInt("user_id"));
                    app.setCompanyName(
                            result.getString("company_name"));
                    app.setJobRole(result.getString("job_role"));
                    app.setApplicationDate(
                            result.getDate("application_date"));
                    app.setDeadline(result.getDate("deadline"));
                    app.setStatus(result.getString("status"));
                    app.setJobLink(result.getString("job_link"));
                    app.setNotes(result.getString("notes"));

                    return app;
                }
            }
        }

        return null;
    }

    // 4. Update an application
    public boolean updateApplication(Application app)
            throws SQLException {

        String sql = """
                UPDATE applications
                SET company_name = ?,
                    job_role = ?,
                    application_date = ?,
                    deadline = ?,
                    status = ?,
                    job_link = ?,
                    notes = ?
                WHERE application_id = ? AND user_id = ?
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, app.getCompanyName());
            statement.setString(2, app.getJobRole());
            statement.setDate(3, app.getApplicationDate());
            statement.setDate(4, app.getDeadline());
            statement.setString(5, app.getStatus());
            statement.setString(6, app.getJobLink());
            statement.setString(7, app.getNotes());
            statement.setInt(8, app.getApplicationId());
            statement.setInt(9, app.getUserId());

            return statement.executeUpdate() == 1;
        }
    }

    // 5. Delete an application
    public boolean deleteApplication(int applicationId,
                                     int userId)
            throws SQLException {

        String sql = """
                DELETE FROM applications
                WHERE application_id = ? AND user_id = ?
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, applicationId);
            statement.setInt(2, userId);

            return statement.executeUpdate() == 1;
        }
    }
    // Get total number of applications for a user
    public int getTotalApplications(int userId) throws Exception {

        String sql = "SELECT COUNT(*) FROM applications WHERE user_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
            }
        }

        return 0;
    }


    // Get number of applications with a specific status
    public int getApplicationsByStatus(int userId, String status)
            throws Exception {

        String sql = "SELECT COUNT(*) FROM applications " +
                "WHERE user_id = ? AND status = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);
            statement.setString(2, status);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
            }
        }

        return 0;
    }
}
