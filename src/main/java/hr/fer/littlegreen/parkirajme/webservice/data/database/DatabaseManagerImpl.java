package hr.fer.littlegreen.parkirajme.webservice.data.database;

import hr.fer.littlegreen.parkirajme.webservice.domain.models.Administrator;
import hr.fer.littlegreen.parkirajme.webservice.domain.models.Company;
import hr.fer.littlegreen.parkirajme.webservice.domain.models.ParkingObject;
import hr.fer.littlegreen.parkirajme.webservice.domain.models.Person;
import hr.fer.littlegreen.parkirajme.webservice.domain.models.Reservation;
import hr.fer.littlegreen.parkirajme.webservice.domain.models.User;
import hr.fer.littlegreen.parkirajme.webservice.domain.models.Vehicle;
import hr.fer.littlegreen.parkirajme.webservice.restapi.addparkingobject.CompanyAddParkingObjectRequestBody;
import hr.fer.littlegreen.parkirajme.webservice.restapi.register.company.RegisterCompanyRequestBody;
import hr.fer.littlegreen.parkirajme.webservice.restapi.register.person.RegisterPersonRequestBody;
import hr.fer.littlegreen.parkirajme.webservice.restapi.reservations.ReservationRequestBody;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;


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
            while (resultSet.next()) {
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
    public String registerPerson(@NonNull RegisterPersonRequestBody registerPersonRequestBody) {
        Savepoint savepoint = null;
        var uuid = UUID.randomUUID().toString().replace("-", "");
        var passwordHash = passwordEncoder.encode(registerPersonRequestBody.getPassword());

        String query = """
            BEGIN TRANSACTION;\s
            insert into app_user (email, password_hash, role, oib, user_uuid) 
            values (?, ?, 'p', ?, ?);
            insert into person (first_name, last_name, credit_card_number, credit_card_expiration_date, person_uuid) 
            values (?, ?, ?, ?, ?);
            \s"""
            + "insert into vehicle (registration_number, person_uuid) values (?, ?);"
            .repeat(registerPersonRequestBody
                .getRegistrationNumbers()
                .size())
            + "COMMIT TRANSACTION;";
        try (PreparedStatement stmt = databaseConnection.prepareStatement(query)) {
            savepoint = databaseConnection.setSavepoint();
            stmt.setString(1, registerPersonRequestBody.getEmail());
            stmt.setString(2, passwordHash);
            stmt.setString(3, registerPersonRequestBody.getOib());
            stmt.setString(4, uuid);
            stmt.setString(5, registerPersonRequestBody.getFirstName());
            stmt.setString(6, registerPersonRequestBody.getLastName());
            stmt.setString(7, registerPersonRequestBody.getCreditCardNumber());
            stmt.setDate(8, Date.valueOf(registerPersonRequestBody.getCreditCardExpirationDate() + "-01"));
            stmt.setString(9, uuid);
            int i = 10;
            for(var regPlate : registerPersonRequestBody.getRegistrationNumbers()) {
                stmt.setString(i++, regPlate);
                stmt.setString(i++, uuid);
            }
            stmt.executeUpdate();
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
    public String registerCompany(@NonNull RegisterCompanyRequestBody registerCompanyRequestBody) {
        Savepoint savepoint = null;
        String uuid = UUID.randomUUID().toString().replace("-", "");
        var passwordHash = passwordEncoder.encode(registerCompanyRequestBody.getPassword());

        String query = """
            BEGIN TRANSACTION;
            insert into app_user (email, password_hash, role, oib, user_uuid) 
            values (?, ?, 'c', ?, ?);
            insert into company (name, headquarter_address, company_uuid) 
            values (?, ?, ?);
            COMMIT TRANSACTION;
            """;
        try (PreparedStatement stmt = databaseConnection.prepareStatement(query)) {
            savepoint = databaseConnection.setSavepoint();
            stmt.setString(1, registerCompanyRequestBody.getEmail());
            stmt.setString(2, passwordHash);
            stmt.setString(3, registerCompanyRequestBody.getOib());
            stmt.setString(4, uuid);
            stmt.setString(5, registerCompanyRequestBody.getName());
            stmt.setString(6, registerCompanyRequestBody.getHeadquarterAddress());
            stmt.setString(7, uuid);
            stmt.executeUpdate();
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
                int capacity = rs.getInt("capacity");
                String address = rs.getString("address");
                String name = rs.getString("object_name");
                BigDecimal latitude = rs.getBigDecimal("latitude");
                BigDecimal longitude = rs.getBigDecimal("longitude");
                list.add(new ParkingObject(
                    id,
                    companyId,
                    freeSlots,
                    capacity,
                    price,
                    address,
                    name,
                    latitude,
                    longitude
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public String addParkingObject(
        @NonNull CompanyAddParkingObjectRequestBody parkingObject,
        @NonNull String companyUuid
    ) {
        Savepoint savepoint = null;
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String query = """
            BEGIN TRANSACTION;
            insert into parking_object (object_uuid, company_uuid, "30_minute_price", address, object_name, capacity, latitude, longitude, free_slots)
            values (?, ?, ?, ?, ?, ?, ?, ?, ?);
            COMMIT TRANSACTION;
            """;
        try (PreparedStatement stmt = databaseConnection.prepareStatement(query)) {
            savepoint = databaseConnection.setSavepoint();
            stmt.setString(1, uuid);
            stmt.setString(2, companyUuid);
            stmt.setInt(3, parkingObject.getPrice());
            stmt.setString(4, parkingObject.getAddress());
            stmt.setString(5, parkingObject.getName());
            stmt.setInt(6, parkingObject.getCapacity());
            stmt.setBigDecimal(7, parkingObject.getLatitude());
            stmt.setBigDecimal(8, parkingObject.getLongitude());
            stmt.setInt(9, parkingObject.getFree_slots());
            stmt.executeUpdate();
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
    public List<Reservation> getUserParkingReservations(String userId) {
        List<Reservation> list = new LinkedList<>();
        String query = "select * from reservation where person_uuid = '" + userId + "';";
        try (
            Statement stmt = databaseConnection.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY
            )
        ) {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                String reservationId = rs.getString("reservation_uuid");
                String parkingId = rs.getString("object_uuid");
                String personId = rs.getString("person_uuid");
                Timestamp startTime = rs.getTimestamp("start_time");
                Timestamp endTime = rs.getTimestamp("end_time");
                String daysOfWeek = rs.getString("days_in_week");
                list.add(new Reservation(
                    reservationId,
                    personId,
                    parkingId,
                    startTime,
                    endTime,
                    daysOfWeek
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<Reservation> getReservationsOnParking(String objectId) {
        List<Reservation> list = new LinkedList<>();
        String query = "select * from reservation where object_uuid = " + objectId + ";";
        try (
            Statement stmt = databaseConnection.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY
            )
        ) {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                String reservationId = rs.getString("reservation_uuid");
                String parkingId = rs.getString("object_uuid");
                String personId = rs.getString("person_uuid");
                Timestamp startTime = rs.getTimestamp("start_time");
                Timestamp endTime = rs.getTimestamp("end_time");
                String daysOfWeek = rs.getString("days_in_week");
                list.add(new Reservation(
                    reservationId,
                    personId,
                    parkingId,
                    startTime,
                    endTime,
                    daysOfWeek
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Nullable
    @Override
    public String addReservation(ReservationRequestBody reservation, String userId) {
        Savepoint savepoint = null;
        String reservation_uuid = UUID.randomUUID().toString().replace("-", "");
        String query = "BEGIN TRANSACTION;\n" + "insert into reservation values ('" + userId
            + "', '"
            + reservation.getParkingId() + "', '"
            + reservation.getDaysOfWeek() + "', '"
            + reservation_uuid + "', '"
            + reservation.getStartTime() + "', '"
            + reservation.getEndTime() + "');" + "COMMIT TRANSACTION;";
        try (Statement stmt = databaseConnection.createStatement()) {
            savepoint = databaseConnection.setSavepoint();
            stmt.executeUpdate(query);
            return reservation_uuid;
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
    public List<User> getRegisteredUsers() {
        List<User> registeredUsers = new ArrayList<>();

        String query = "select * from app_user;";
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
                if(role.equals("c")) registeredUsers.add(getCompany(id,email,role,oib));
                else if(role.equals("p")) registeredUsers.add(getPerson(id,email,role,oib));
                else registeredUsers.add(getAdministrator(id,email,role,oib));
                }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return registeredUsers;
    }

    @Override
    @Nullable
    public String getUserRole(String userUuid) {
        String queryString = "select * from app_user where user_uuid=?";
        try (PreparedStatement getRole = databaseConnection.prepareStatement(queryString)) {
            getRole.setString(1, userUuid);
            ResultSet rs = getRole.executeQuery();
            if (!rs.next()) { return null; }
            return rs.getString("role");


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<ParkingObject> getCompanyParkingObjects(@NonNull String companyId) {
        List<ParkingObject> list = new LinkedList<>();
        String query = "SELECT * FROM parking_object WHERE company_uuid = '" + companyId + "';";
        try (
            Statement stmt = databaseConnection.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY
            )
        ) {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                String id = rs.getString("object_uuid");
                int freeSlots = rs.getInt("free_slots");
                int price = rs.getInt("30_minute_price");
                int capacity = rs.getInt("capacity");
                String address = rs.getString("address");
                String name = rs.getString("object_name");
                BigDecimal latitude = rs.getBigDecimal("latitude");
                BigDecimal longitude = rs.getBigDecimal("longitude");
                list.add(new ParkingObject(
                    id,
                    companyId,
                    freeSlots,
                    capacity,
                    price,
                    address,
                    name,
                    latitude,
                    longitude
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public String parkingObjectOwner(@NonNull String parkingObjectId) {
        String query = "SELECT company_uuid FROM parking_object WHERE object_uuid =?";

        try (PreparedStatement stmt = databaseConnection.prepareStatement(query)) {
            stmt.setString(1, parkingObjectId);
            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) { return null; }
            return rs.getString("company_uuid");

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void deleteParkingObject(@NonNull String parkingObjectId) {
        String query = "BEGIN TRANSACTION;\n" + "DELETE FROM parking_object WHERE object_uuid = '"
            + parkingObjectId + "';" + "COMMIT TRANSACTION;";

        try (Statement stmt = databaseConnection.createStatement()) {
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteUser(@NonNull String userId) {
        String query = "BEGIN TRANSACTION;\n" + "DELETE FROM app_user WHERE user_uuid = '"
            + userId + "';" + "COMMIT TRANSACTION;";

        try (Statement stmt = databaseConnection.createStatement()) {
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteReservation(String reservationId, String userId) {
        String query = "BEGIN TRANSACTION;\n" + "DELETE FROM reservation WHERE person_uuid = '"
            + userId + "'AND reservation_uuid='" + reservationId + "';" + "COMMIT TRANSACTION;";

        try (Statement stmt = databaseConnection.createStatement()) {
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
