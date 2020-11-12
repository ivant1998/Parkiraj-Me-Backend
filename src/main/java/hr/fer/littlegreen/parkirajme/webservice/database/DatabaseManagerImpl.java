package hr.fer.littlegreen.parkirajme.webservice.database;

import hr.fer.littlegreen.parkirajme.webservice.register.company.RegisterCompanyRequestBody;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

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

    @Override
    public String registerCompany(RegisterCompanyRequestBody company) {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String query = "BEGIN TRANSACTION;\n"
            + "insert into app_user (email, password_hash, role, oib, user_uuid) "
            + "values ('" + company.getEmail() + "', '" + company.getPassword() + "', 'c', '" + company.getOib()
            + "', '" + uuid + "');\n"
            + "insert into company (name, headquarter_address, company_uuid) "
            + "values ('" + company.getName() + "', '" + company.getAddress() + "', '" + uuid + "');"
            + "END TRANSACTION;";
        try (Statement stmt = databaseConnection.createStatement()) {
            stmt.executeUpdate(query);
            return uuid;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
