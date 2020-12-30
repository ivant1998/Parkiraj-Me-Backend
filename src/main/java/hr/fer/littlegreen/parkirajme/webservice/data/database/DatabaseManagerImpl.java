package hr.fer.littlegreen.parkirajme.webservice.data.database;

import hr.fer.littlegreen.parkirajme.webservice.domain.models.Administrator;
import hr.fer.littlegreen.parkirajme.webservice.domain.models.Company;
import hr.fer.littlegreen.parkirajme.webservice.domain.models.ParkingObject;
import hr.fer.littlegreen.parkirajme.webservice.domain.models.Person;
import hr.fer.littlegreen.parkirajme.webservice.domain.models.User;
import hr.fer.littlegreen.parkirajme.webservice.domain.models.Vehicle;
import hr.fer.littlegreen.parkirajme.webservice.restapi.addparkingobject.CompanyParkingObjectRequestBody;
import hr.fer.littlegreen.parkirajme.webservice.restapi.register.company.RegisterCompanyRequestBody;
import hr.fer.littlegreen.parkirajme.webservice.restapi.register.user.RegisterUserRequestBody;
import hr.fer.littlegreen.parkirajme.webservice.restapi.registeredusers.RegisteredUser;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@SuppressWarnings("SqlResolve")
public class DatabaseManagerImpl implements DatabaseManager {

    private static final String ROLE_PERSON = "p";
    private static final String ROLE_COMPANY = "c";
    private static final String ROLE_ADMINISTRATOR = "a";

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
    public User checkLoginCredentials(@NonNull String inputEmail, @NonNull String inputPassword) {
        var query = "SELECT * FROM app_user WHERE email = '%s';"
            .formatted(inputEmail);
        try (
            var statement = databaseConnection.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY
            )
        ) {
            var resultSet = statement.executeQuery(query);
            if (resultSet.first()) {
                String userUuid = resultSet.getString("user_uuid");
                String email = resultSet.getString("email");
                String passwordHash = resultSet.getString("password_hash");
                String role = resultSet.getString("role");
                String oib = resultSet.getString("oib");
                if (passwordEncoder.matches(inputPassword, passwordHash)) {
                    switch (role) {
                    case ROLE_PERSON:
                        return getPerson(userUuid, email, role, oib);
                    case ROLE_COMPANY:
                        return getCompany(userUuid, email, role, oib);
                    case ROLE_ADMINISTRATOR:
                        return getAdministrator(userUuid, email, role, oib);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Person getPerson(String userUuid, String email, String role, String oib) {
        var query = "SELECT * FROM person WHERE person_uuid = '%s';"
            .formatted(userUuid);
        try (
            var statement = databaseConnection.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY
            )
        ) {
            var resultSet = statement.executeQuery(query);
            if (resultSet.first()) {
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String creditCardNumber = resultSet.getString("credit_card_number");
                Date creditCardExpirationDate = resultSet.getDate("credit_card_expiration_date");
                return new Person(
                    userUuid,
                    email,
                    role,
                    oib,
                    firstName,
                    lastName,
                    creditCardNumber,
                    YearMonth.from(creditCardExpirationDate.toLocalDate()),
                    getVehicles(userUuid)
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<Vehicle> getVehicles(String personUuid) {
        var query = "SELECT registration_number FROM vehicle WHERE person_uuid = '%s';"
            .formatted(personUuid);
        try (
            var statement = databaseConnection.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY
            )
        ) {
            var resultSet = statement.executeQuery(query);
            var vehicles = new ArrayList<Vehicle>();
            if (resultSet.next()) {
                String registrationNumber = resultSet.getString("registration_number");
                vehicles.add(new Vehicle(registrationNumber));
            }
            return vehicles;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return List.of();
    }

    private Company getCompany(String userUuid, String email, String role, String oib) {
        var query = "SELECT * FROM company WHERE company_uuid = '%s';"
            .formatted(userUuid);
        try (
            var statement = databaseConnection.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY
            )
        ) {
            var resultSet = statement.executeQuery(query);
            if (resultSet.first()) {
                String name = resultSet.getString("name");
                String headquarterAddress = resultSet.getString("headquarter_address");
                return new Company(userUuid, email, role, oib, headquarterAddress, name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Administrator getAdministrator(String userUuid, String email, String role, String oib) {
        return new Administrator(userUuid, email, role, oib);
    }

    @Override
    public String registerUser(@NonNull RegisterUserRequestBody user) {
        Savepoint savepoint = null;
        var uuid = UUID.randomUUID().toString().replace("-", "");
        var passwordHash = passwordEncoder.encode(user.getPassword());
        var queryBuilder = new StringBuilder(
            """
                BEGIN TRANSACTION;
                INSERT INTO app_user (email, password_hash, role, oib, user_uuid)
                VALUES ('%s', '%s', 'p', '%s', '%s');
                INSERT INTO person (first_name, last_name, credit_card_number, credit_card_expiration_date, person_uuid)
                VALUES ('%s', '%s', '%s', '%s-01'::DATE, '%s');
                """.formatted(
                user.getEmail(), passwordHash, user.getOIB(), uuid,
                user.getName(), user.getSurname(), user.getCreditCard(), user.getCreditCardExpirationDate(), uuid
            )
        );
        for (String regPlate : user.getRegPlates()) {
            queryBuilder.append(
                "INSERT INTO vehicle (registration_number, person_uuid) VALUES ('%s', '%s');\n"
                    .formatted(regPlate, uuid)
            );
        }
        queryBuilder.append("COMMIT TRANSACTION;");
        try (Statement stmt = databaseConnection.createStatement()) {
            savepoint = databaseConnection.setSavepoint();
            stmt.executeUpdate(queryBuilder.toString());
            return uuid;
        } catch (SQLException e) {
            if (savepoint != null) {
                try {
                    databaseConnection.rollback(savepoint);
                    throw new IllegalArgumentException(ErrorMessage.getMessage(e));
                } catch (SQLException e2) {
                    e2.printStackTrace();

                }
            }
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String registerCompany(@NonNull RegisterCompanyRequestBody company) {
        Savepoint savepoint = null;
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
            savepoint = databaseConnection.setSavepoint();
            stmt.executeUpdate(query);
            return uuid;
        } catch (SQLException e) {
            if (savepoint != null) {
                try {
                    databaseConnection.rollback(savepoint);
                    throw new IllegalArgumentException(ErrorMessage.getMessage(e));
                } catch (SQLException e2) {
                    e2.printStackTrace();
                }
            }
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
                String name = rs.getString("object_name");
                BigDecimal latitude = rs.getBigDecimal("latitude");
                BigDecimal longitude = rs.getBigDecimal("longitude");
                list.add(new ParkingObject(id, companyId, freeSlots, price, address, name, latitude, longitude));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public String addParkingObject(@NonNull CompanyParkingObjectRequestBody parkingObject, @NonNull String companyId) {
        Savepoint savepoint = null;
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String query = "BEGIN TRANSACTION;\n" + "insert into parking_object values ('" + uuid + "', '" + companyId
            + "', '"
            + parkingObject.getPrice() + "', '"
            + parkingObject.getAddress() + "', '" + parkingObject.getName() + "', '" + parkingObject.getCapacity()
            + "', '"
            + parkingObject.getLatitude().toString() + "', '" + parkingObject.getLongitude().toString() + "', '"
            + parkingObject.getFree_slots() + "');" + "COMMIT TRANSACTION;";
        try (Statement stmt = databaseConnection.createStatement()) {
            savepoint = databaseConnection.setSavepoint();
            stmt.executeUpdate(query);
            return uuid;
        } catch (SQLException e) {
            if (savepoint != null) {
                try {
                    databaseConnection.rollback(savepoint);
                    throw new IllegalArgumentException(ErrorMessage.getMessage(e));
                } catch (SQLException e2) {
                    e2.printStackTrace();
                }
            }
            e.printStackTrace();
        }
        return null;
    }

    @NonNull
    @Override
    public List<RegisteredUser> getRegisteredUsers() {
        List<RegisteredUser> registeredUsers = new ArrayList<>();
        String query = "select * from app_user"
            + " where role='c' OR role='p';";
        try (
            Statement stmt = databaseConnection.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY
            )
        ) {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                String id = rs.getString("user_uuid");
                String email = rs.getString("email");
                String role = rs.getString("role");
                String oib = rs.getString("oib");
                registeredUsers.add(new RegisteredUser(id, email, role, oib));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return registeredUsers;
    }

    @Override
    public String getUserRole(String id) {
        String queryString = "select * from app_user where user_uuid=?";
        try (PreparedStatement getRole = databaseConnection.prepareStatement(queryString))
        {
            getRole.setString(1, id);
            ResultSet rs = getRole.executeQuery();
            if(!rs.next()) return null;
            return rs.getString("role");


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

}
