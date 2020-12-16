package hr.fer.littlegreen.parkirajme.webservice.data.database;

import hr.fer.littlegreen.parkirajme.webservice.domain.models.ParkingObject;
import hr.fer.littlegreen.parkirajme.webservice.restapi.register.company.RegisterCompanyRequestBody;
import hr.fer.littlegreen.parkirajme.webservice.restapi.register.user.RegisterUserRequestBody;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
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
    public String checkLoginCredentials(@NonNull String email, @NonNull String password) {
        String query
            = "select user_uuid, password_hash from app_user where email = '" + email + "';";
        try (
            Statement stmt = databaseConnection.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY
            )
        ) {
            ResultSet rs = stmt.executeQuery(query);
            if (rs.first()) {
                String uuid = rs.getString("user_uuid");
                String passwordHash = rs.getString("password_hash");
                if (passwordEncoder.matches(password, passwordHash)) {
                    return uuid;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String registerUser(@NonNull RegisterUserRequestBody user) {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("BEGIN TRANSACTION;\n"
            + "INSERT INTO app_user (email, password_hash, role, oib, user_uuid)\n"
            + "VALUES ('" + user.getEmail() + "','" + passwordEncoder.encode(user.getPassword()) + "', 'p','"
            + user.getOIB() + "','" + uuid + "'); "
            + "INSERT INTO person (first_name, last_name, credit_card_number, person_uuid)\n"
            + "VALUES ('" + user.getName() + "','" + user.getSurname() + "','" + user.getCreditcard() + "','" + uuid
            + "'); ");
        for (String regPlate : user.getRegPlates()) {
            queryBuilder.append("INSERT INTO vehicle (registration_number, person_uuid)\n"
                + "VALUES ('" + regPlate + "','" + uuid + "'); ");
        }
        queryBuilder.append("COMMIT TRANSACTION;");
        try (Statement stmt = databaseConnection.createStatement()) {
            stmt.executeUpdate(queryBuilder.toString());
            return uuid;
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
            + "values ('" + company.getEmail() + "', '" + passwordEncoder.encode(company.getPassword()) + "', 'c', '"
            + company.getOib()
            + "', '" + uuid + "');\n"
            + "insert into company (name, headquarter_address, company_uuid) "
            + "values ('" + company.getName() + "', '" + company.getAddress() + "', '" + uuid + "');"
            + "COMMIT TRANSACTION;";
        try (Statement stmt = databaseConnection.createStatement()) {
            stmt.executeUpdate(query);
            return uuid;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<ParkingObject> getParkingObjects() {
        List<ParkingObject> list = new LinkedList<>();
        String query = "select * from parking_object;";
        try (
            Statement stmt = databaseConnection.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY
            )
        ) {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                String id = rs.getString("object_uuid");
                String companyId = rs.getString("company_uuid");
                int freeSlots = rs.getInt("free_slots");
                int price = rs.getInt("30_minute_price");
                String address = rs.getString("address");
                String name = rs.getString("name");
                BigDecimal latitude = rs.getBigDecimal("latitude");
                BigDecimal longitude = rs.getBigDecimal("longitude");
                list.add(new ParkingObject(id, companyId, freeSlots, price, address, name, latitude, longitude));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
