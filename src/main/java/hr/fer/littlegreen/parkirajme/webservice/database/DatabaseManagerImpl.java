package hr.fer.littlegreen.parkirajme.webservice.database;

import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManagerImpl implements DatabaseManager {

    @NonNull
    private final PasswordEncoder passwordEncoder;

    @NonNull
    private final Connection databaseConnection;

    public DatabaseManagerImpl(
        @NonNull PasswordEncoder passwordEncoder,
        @NonNull Connection databaseConnection
    ) {
        this.passwordEncoder = passwordEncoder;
        this.databaseConnection = databaseConnection;
    }

    @Override
    public String checkLoginCredentials(String email, String password) {
        String query
            = "select user_uuid from app_user where email = " + email + " and password_hash = "
            + passwordEncoder.encode(password);
        try (Statement stmt = databaseConnection.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            if (rs.first()) {
                return rs.getString("user_uuid");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
