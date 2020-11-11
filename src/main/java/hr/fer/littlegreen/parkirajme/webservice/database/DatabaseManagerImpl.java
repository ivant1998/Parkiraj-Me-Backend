package hr.fer.littlegreen.parkirajme.webservice.database;

import hr.fer.littlegreen.parkirajme.webservice.register.user.RegisterUserRequestBody;
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
    public String checkLoginCredentials(@NonNull String email, @NonNull String password) {
        String query
            = "select user_uuid from app_user where email = '" + email + "' and password_hash = '"
            + passwordEncoder.encode(password)+ "'";
        try (Statement stmt = databaseConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
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
    public String checkUserExists(@NonNull String email,@NonNull String oib) {
        String query
            = "select user_uuid from app_user where email = '" + email +"' or oib = '" + oib+"';" ;
        try (Statement stmt = databaseConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
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
    public void registerUser(@NonNull RegisterUserRequestBody user) {
        String query
            = "BEGIN TRANSACTION;\n"
            + "INSERT INTO app_user (email, password_hash, role, oib, user_uuid)\n"
            + "VALUES ('"+user.getEmail()+"','"+user.getPassword()+"', 'p','"+user.getOIB()+"','"+"00000000000000000000000000000000"+"'); "
            + "INSERT INTO person (first_name, last_name, credit_card_number, person_uuid)\n"
            + "VALUES ('"+user.getName()+"','"+user.getSurname()+"','"+user.getCreditcard()+"','"+"00000000000000000000000000000000"+"'); "
            + "INSERT INTO vehicle (registration_number, person_uuid)\n"
            + "VALUES ('"+user.getRegPlate()+"','"+"00000000000000000000000000000000"+"'); "
            //+ "ROLLBACK TRANSACTION\n"
            + "COMMIT TRANSACTION;";
        try (Statement stmt = databaseConnection.createStatement()) {
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
