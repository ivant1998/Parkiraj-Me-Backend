package hr.fer.littlegreen.parkirajme.webservice.data.database;

import hr.fer.littlegreen.parkirajme.webservice.domain.models.Administrator;
import hr.fer.littlegreen.parkirajme.webservice.domain.models.Company;
import hr.fer.littlegreen.parkirajme.webservice.domain.models.ParkingObject;
import hr.fer.littlegreen.parkirajme.webservice.domain.models.Person;
import hr.fer.littlegreen.parkirajme.webservice.domain.models.Reservation;
import hr.fer.littlegreen.parkirajme.webservice.domain.models.User;
import hr.fer.littlegreen.parkirajme.webservice.domain.models.Vehicle;
import hr.fer.littlegreen.parkirajme.webservice.restapi.addparkingobject.CompanyAddParkingObjectRequestBody;
import hr.fer.littlegreen.parkirajme.webservice.restapi.edit.editparkingobject.EditParkingObjectRequestBody;
import hr.fer.littlegreen.parkirajme.webservice.restapi.register.company.RegisterCompanyRequestBody;
import hr.fer.littlegreen.parkirajme.webservice.restapi.register.person.RegisterPersonRequestBody;
import hr.fer.littlegreen.parkirajme.webservice.restapi.reservations.ReservationAndParkingObjectPair;
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
        var query = """
            select * from app_user au
            left join company c on company_uuid = user_uuid
            left join person p on person_uuid = user_uuid
            left join administrator a2 on administrator_uuid = user_uuid
            left join (select person_uuid ,array_agg(registration_number) vehicles from vehicle
            		group by person_uuid) as v2
            	on au.user_uuid = v2.person_uuid
            where email = ?
            """;
        try (
            var statement = databaseConnection.prepareStatement(
                query,
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY
            );
        ) {
            statement.setString(1, inputEmail);
            var resultSet = statement.executeQuery();
            if (resultSet.first()) {
                String userUuid = resultSet.getString("user_uuid");
                String email = resultSet.getString("email");
                String passwordHash = resultSet.getString("password_hash");
                String role = resultSet.getString("role");
                String oib = resultSet.getString("oib");
                if (passwordEncoder.matches(inputPassword, passwordHash)) {
                    switch (role) {
                    case ROLE_ADMINISTRATOR:
                        return new Administrator(userUuid, email, role, oib);
                    case ROLE_PERSON:
                        String firstName = resultSet.getString("first_name");
                        String lastName = resultSet.getString("last_name");
                        String creditCardNumber = resultSet.getString("credit_card_number");
                        Date creditCardExpirationDate = resultSet.getDate("credit_card_expiration_date");
                        var vehiclesArray = (String[]) resultSet.getArray("vehicles").getArray();
                        List<Vehicle> vehicles = new ArrayList<>();
                        for (var vehicle : vehiclesArray) {
                            System.out.print("vehicle: " + vehicle);
                            vehicles.add(new Vehicle(vehicle));
                        }

                        return new Person(
                            userUuid,
                            email,
                            role,
                            oib,
                            firstName,
                            lastName,
                            creditCardNumber,
                            YearMonth.from(creditCardExpirationDate.toLocalDate()),
                            vehicles
                        );
                    case ROLE_COMPANY:
                        String name = resultSet.getString("name");
                        String headquarterAddress = resultSet.getString("headquarter_address");
                        return new Company(userUuid, email, role, oib, headquarterAddress, name);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
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
            for (var regPlate : registerPersonRequestBody.getRegistrationNumbers()) {
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
        } catch (Exception ex) {
            throw new IllegalArgumentException("Pogreška pri registraciji");
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
        } catch (Exception ex) {
            throw new IllegalArgumentException("Pogreška pri registraciji");
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
                    price,
                    address,
                    name,
                    capacity,
                    latitude,
                    longitude,
                    freeSlots
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public ParkingObject getParkingObject(String objectUuid) {
        String query = "select * from parking_object where object_uuid = ?;";
        try (
            var stmt = databaseConnection.prepareStatement(query,
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY
            )
        ) {
            stmt.setString(1, objectUuid);
            ResultSet rs = stmt.executeQuery();
            if (rs.first()) {
                String id = rs.getString("object_uuid");
                String companyId = rs.getString("company_uuid");
                int freeSlots = rs.getInt("free_slots");
                int price = rs.getInt("30_minute_price");
                int capacity = rs.getInt("capacity");
                String address = rs.getString("address");
                String name = rs.getString("object_name");
                BigDecimal latitude = rs.getBigDecimal("latitude");
                BigDecimal longitude = rs.getBigDecimal("longitude");
                return new ParkingObject(
                    id,
                    companyId,
                    price,
                    address,
                    name,
                    capacity,
                    latitude,
                    longitude,
                    freeSlots
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
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
    public List<ReservationAndParkingObjectPair> getUserParkingReservations(@NonNull String userId) {
        List<ReservationAndParkingObjectPair> list = new LinkedList<>();
        String query = "select * from reservation natural join parking_object where person_uuid = ?;";
        try (
            var stmt = databaseConnection.prepareStatement(query,
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY
            )
        ) {
            stmt.setString(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String reservationId = rs.getString("reservation_uuid");
                String parkingId = rs.getString("object_uuid");
                String personId = rs.getString("person_uuid");
                Timestamp startTime = rs.getTimestamp("start_time");
                Timestamp endTime = rs.getTimestamp("end_time");
                String daysOfWeek = rs.getString("days_in_week");

                String companyId = rs.getString("company_uuid");
                int freeSlots = rs.getInt("free_slots");
                int price = rs.getInt("30_minute_price");
                int capacity = rs.getInt("capacity");
                String address = rs.getString("address");
                String name = rs.getString("object_name");
                BigDecimal latitude = rs.getBigDecimal("latitude");
                BigDecimal longitude = rs.getBigDecimal("longitude");

                list.add(new ReservationAndParkingObjectPair(
                    new Reservation(
                        reservationId,
                        personId,
                        parkingId,
                        startTime,
                        endTime,
                        daysOfWeek
                    ),
                    new ParkingObject(
                        parkingId,
                        companyId,
                        price,
                        address,
                        name,
                        capacity,
                        latitude,
                        longitude,
                        freeSlots
                    )
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<ReservationAndParkingObjectPair> getReservationsOnParking(@NonNull String objectId) {
        List<ReservationAndParkingObjectPair> list = new LinkedList<>();
        String query = "select * from reservation natural join parking_object where object_uuid = ?;";
        try (
            var stmt = databaseConnection.prepareStatement(query,
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY
            )
        ) {
            stmt.setString(1, objectId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String reservationId = rs.getString("reservation_uuid");
                String parkingId = rs.getString("object_uuid");
                String personId = rs.getString("person_uuid");
                Timestamp startTime = rs.getTimestamp("start_time");
                Timestamp endTime = rs.getTimestamp("end_time");
                String daysOfWeek = rs.getString("days_in_week");

                String companyId = rs.getString("company_uuid");
                int freeSlots = rs.getInt("free_slots");
                int price = rs.getInt("30_minute_price");
                int capacity = rs.getInt("capacity");
                String address = rs.getString("address");
                String name = rs.getString("object_name");
                BigDecimal latitude = rs.getBigDecimal("latitude");
                BigDecimal longitude = rs.getBigDecimal("longitude");

                list.add(new ReservationAndParkingObjectPair(
                    new Reservation(
                        reservationId,
                        personId,
                        parkingId,
                        startTime,
                        endTime,
                        daysOfWeek
                    ),
                    new ParkingObject(
                        parkingId,
                        companyId,
                        price,
                        address,
                        name,
                        capacity,
                        latitude,
                        longitude,
                        freeSlots
                    )
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
        var query = ("""
            BEGIN TRANSACTION;
            INSERT INTO reservation(reservation_uuid, person_uuid, object_uuid, days_in_week, start_time, end_time)
            VALUES(?, ?, ?, ?::BIT(7), ?::TIMESTAMP(0), ?::TIMESTAMP(0));
            COMMIT TRANSACTION;
            """);
        try (var stmt = databaseConnection.prepareStatement(query)) {
            stmt.setString(1, reservation_uuid);
            stmt.setString(2, userId);
            stmt.setString(3, reservation.getParkingId());
            stmt.setString(4, reservation.getDaysOfWeek());
            stmt.setTimestamp(5, reservation.getStartTime());
            stmt.setTimestamp(6, reservation.getEndTime());
            savepoint = databaseConnection.setSavepoint();
            stmt.executeUpdate();
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

        String queryPerson = """
            select au.user_uuid , email, role , oib, first_name , last_name,
                credit_card_number , credit_card_expiration_date, vehicles
            	from person p
            	join app_user au on au.user_uuid = p.person_uuid
            	join (select person_uuid , array_agg(registration_number) vehicles from vehicle
            		group by person_uuid) as v2
            	on au.user_uuid = v2.person_uuid;
            """;
        String queryCompany = "select * from company c join app_user au on au.user_uuid = c.company_uuid;";
        String queryAdministrator = "select * from app_user where role = 'a';";
        try (
            Statement stmt = databaseConnection.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY
            )
        ) {
            ResultSet rs = stmt.executeQuery(queryPerson);
            while (rs.next()) {
                String id = rs.getString("user_uuid");
                String email = rs.getString("email");
                String role = rs.getString("role");
                String oib = rs.getString("oib");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String creditCardNumber = rs.getString("credit_card_number");
                YearMonth creditCardExpirationDate = YearMonth.from(rs
                    .getDate("credit_card_expiration_date")
                    .toLocalDate());
                var vehiclesArray = rs.getString("vehicles").replace("{", "").replace("}", "").split(",");
                List<Vehicle> vehicles = new ArrayList<>();
                for (var vehicle : vehiclesArray) {
                    vehicles.add(new Vehicle(vehicle));
                }
                registeredUsers.add(new Person(
                    id,
                    email,
                    role,
                    oib,
                    firstName,
                    lastName,
                    creditCardNumber,
                    creditCardExpirationDate,
                    vehicles
                ));
            }

            rs = stmt.executeQuery(queryCompany);
            while (rs.next()) {
                String id = rs.getString("user_uuid");
                String email = rs.getString("email");
                String role = rs.getString("role");
                String oib = rs.getString("oib");
                String name = rs.getString("name");
                String headquarterAddress = rs.getString("headquarter_address");
                registeredUsers.add(new Company(id, email, role, oib, headquarterAddress, name));
            }
            rs = stmt.executeQuery(queryAdministrator);
            while (rs.next()) {
                String id = rs.getString("user_uuid");
                String email = rs.getString("email");
                String role = rs.getString("role");
                String oib = rs.getString("oib");
                registeredUsers.add(new Administrator(id, email, role, oib));
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
        String query = "SELECT * FROM parking_object WHERE company_uuid = ?;";
        try (
            var stmt = databaseConnection.prepareStatement(query,
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY
            )
        ) {
            stmt.setString(1, companyId);
            ResultSet rs = stmt.executeQuery();
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
                    price,
                    address,
                    name,
                    capacity,
                    latitude,
                    longitude,
                    freeSlots
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
        String query = """
            BEGIN TRANSACTION;
            DELETE FROM parking_object WHERE object_uuid = ?;
            COMMIT TRANSACTION;
            """;

        try (var stmt = databaseConnection.prepareStatement(query)) {
            stmt.setString(1, parkingObjectId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteUser(@NonNull String userId) {
        String query = """
            BEGIN TRANSACTION;
            DELETE FROM app_user WHERE user_uuid = ?;
            COMMIT TRANSACTION;""";

        try (var stmt = databaseConnection.prepareStatement(query)) {
            stmt.setString(1, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteReservation(String reservationId, String userId) {
        String query = """
            BEGIN TRANSACTION;
            DELETE FROM reservation WHERE person_uuid = ? 
            AND reservation_uuid = ?;
            COMMIT TRANSACTION;
            """;

        try (var stmt = databaseConnection.prepareStatement(query)) {
            stmt.setString(1, userId);
            stmt.setString(2, reservationId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void editPerson(
        String uuid,
        String firstName,
        String lastName,
        String creditCardNumber,
        String creditCardExpirationDate
    ) {
        Savepoint savepoint = null;

        String query = """
            BEGIN TRANSACTION;
            update person
            set first_name = ?,
            last_name = ?,
            credit_card_number = ?,
            credit_card_expiration_date = ?
            where person_uuid = ?;
            COMMIT TRANSACTION;
            """;
        try (PreparedStatement stmt = databaseConnection.prepareStatement(query)) {
            savepoint = databaseConnection.setSavepoint();
            stmt.setString(1, firstName);
            stmt.setString(2, lastName);
            stmt.setString(3, creditCardNumber);
            stmt.setDate(4, Date.valueOf(creditCardExpirationDate + "-01"));
            stmt.setString(5, uuid);
            stmt.executeUpdate();

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
        } catch (Exception ex) {
            throw new IllegalArgumentException("Pogreška pri uređivanju podataka");
        }

    }

    @Override
    public void editCompany(String uuid, String name, String headquarterAddress) {
        Savepoint savepoint = null;
        String query = """
            BEGIN TRANSACTION;
            update company
            set name = ?,
            headquarter_address = ?
            where company_uuid = ?;
            COMMIT TRANSACTION;
            """;
        try (PreparedStatement stmt = databaseConnection.prepareStatement(query)) {
            savepoint = databaseConnection.setSavepoint();
            stmt.setString(1, name);
            stmt.setString(2, headquarterAddress);
            stmt.setString(3, uuid);
            stmt.executeUpdate();
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
        } catch (Exception ex) {
            throw new IllegalArgumentException("Pogreška pri uređivanju podataka");
        }
    }

    @Override
    public void editParkingObject(String parkingObjectId, EditParkingObjectRequestBody editParkingObjectRequestBody) {
        Savepoint savepoint = null;
        String query = """ 
                BEGIN TRANSACTION;
                UPDATE parking_object
                SET capacity = ?, "30_minute_price" = ?, free_slots = ?
                WHERE object_uuid = ?;
                COMMIT TRANSACTION;
            """;

        try (PreparedStatement stmt = databaseConnection.prepareStatement(query)) {
            savepoint = databaseConnection.setSavepoint();
            stmt.setInt(1, editParkingObjectRequestBody.getCapacity());
            stmt.setInt(2, editParkingObjectRequestBody.getPrice());
            stmt.setInt(3, editParkingObjectRequestBody.getFreeSlots());
            stmt.setString(4, parkingObjectId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            if (savepoint != null) {
                try {
                    databaseConnection.rollback(savepoint);
                } catch (SQLException e2) {
                    e2.printStackTrace();
                }
            }
            e.printStackTrace();
        }
    }


    @Override
    public void deleteVehicle(String ownerId, String registrationNumber) {
        Savepoint savepoint = null;
        String query = """
             BEGIN TRANSACTION;
             DELETE FROM vehicle WHERE person_uuid = ? AND registration_number = ?;
             COMMIT TRANSACTION;
            """;
        try (var stmt = databaseConnection.prepareStatement(query)) {
            savepoint = databaseConnection.setSavepoint();
            stmt.setString(1, ownerId);
            stmt.setString(2, registrationNumber);
            stmt.executeUpdate();
        } catch (SQLException e) {
            if(savepoint != null) {
                try {
                    databaseConnection.rollback(savepoint);
                    throw new IllegalArgumentException();
                } catch (SQLException e2) {
                    e2.printStackTrace();
                }
            }
            e.printStackTrace();
        }
    }

    @Override
    public void addVehicle(String ownerId, String registrationNumber) {
        Savepoint savepoint = null;
        String query = """
            BEGIN TRANSACTION;
            insert into vehicle (person_uuid, registration_number)
            values (?, ?);
            COMMIT TRANSACTION;
            """;
        try (var stmt = databaseConnection.prepareStatement(query)) {
            savepoint = databaseConnection.setSavepoint();
            stmt.setString(1, ownerId);
            stmt.setString(2, registrationNumber);
            stmt.executeUpdate();
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
    }

}
