
package com.applytrack.dao;

import com.applytrack.util.DBConnection;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserDAO {

    public boolean registerUser(String name, String email,
                                String password)
            throws SQLException {

        String sql = """
                INSERT INTO users (name, email, password_hash)
                VALUES (?, ?, ?)
                """;

        String passwordHash = BCrypt.hashpw(
                password, BCrypt.gensalt(12));

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, name);
            statement.setString(2, email);
            statement.setString(3, passwordHash);

            return statement.executeUpdate() == 1;
        }
    }


    public int loginUser(String email, String password)
            throws SQLException {

        String sql = "SELECT user_id, password_hash FROM users WHERE email = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, email);

            try (java.sql.ResultSet result = statement.executeQuery()) {

                if (result.next()) {
                    String storedHash = result.getString("password_hash");

                    if (BCrypt.checkpw(password, storedHash)) {
                        return result.getInt("user_id");
                    }
                }
            }
        }

        return -1;
    }


    public String getUserNameById(int userId) throws SQLException {

        String sql = "SELECT name FROM users WHERE user_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);

            try (java.sql.ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    return result.getString("name");
                }
            }
        }

        return null;
    }

}
