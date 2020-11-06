package hr.fer.littlegreen.parkirajme.webservice.queries;

import hr.fer.littlegreen.parkirajme.webservice.QueryController;
import hr.fer.littlegreen.parkirajme.webservice.dao.Administrator;
import hr.fer.littlegreen.parkirajme.webservice.dao.Company;
import hr.fer.littlegreen.parkirajme.webservice.dao.Person;
import hr.fer.littlegreen.parkirajme.webservice.dao.Vehicle;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

public abstract class Query implements QueryController {
    public static void queryUser(Connection con) throws SQLException {
        String query = "select user_uuid, email, password_hash, role, oib from public.app_user";
        try (Statement stmt = con.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                String id = rs.getString("user_uuid");
                String email = rs.getString("email");
                String password = rs.getString("password_hash");
                String role = rs.getString("role");
                String oib = rs.getString("oib");
            }
        } catch (SQLException e) {
            throw new SQLException();
        }
    }
    public static void queryAdministrator(Connection con) throws SQLException {
        String query = "select user_uuid, email, password_hash, role, oib, administrator_uuid from public.administrator";
        try (Statement stmt = con.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                //String id = rs.getString("user_uuid");
                String email = rs.getString("email");
                String password = rs.getString("password_hash");
                String role = rs.getString("role");
                String oib = rs.getString("oib");
                String id = rs.getString("administrator_uuid");
                Administrator administrator = new Administrator(id, email, password, role, oib);
            }
        } catch (SQLException e) {
            throw new SQLException();
        }
    }
    public static void queryPerson(Connection con) throws SQLException {
        String query = "select user_uuid, email, password_hash, role, oib, person_uuid, first_name, last_name, credit_card_number from public.person";
        try (Statement stmt = con.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                //String id = rs.getString("user_uuid");
                String email = rs.getString("email");
                String password = rs.getString("password_hash");
                String role = rs.getString("role");
                String oib = rs.getString("oib");
                String id = rs.getString("person_uuid");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String creditCard = rs.getString("credit_card_number");
                Person person = new Person(id,email,password, role,oib, firstName, lastName, creditCard);
            }
        } catch (SQLException e) {
            throw new SQLException();
        }
    }
    public static void queryCompany(Connection con) throws SQLException {
        String query = "select user_uuid, email, password_hash, role, oib, company_uuid, name, headquarter_adress from public.app_user";
        try (Statement stmt = con.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                //String id = rs.getString("user_uuid");
                String email = rs.getString("email");
                String password = rs.getString("password_hash");
                String role = rs.getString("role");
                String oib = rs.getString("oib");
                String id = rs.getString("company_uuid");
                String name = rs.getString("name");
                String address = rs.getString("headquarter_adress");
                Company company = new Company(id, email, password, role, oib, name, address);
            }
        } catch (SQLException e) {
            throw new SQLException();
        }
    }
    public static void queryVehicle(Connection con) throws SQLException {
        String query = "select user_uuid, email, password_hash, role, oib, registration_number, owner_uuid from public.app_user";
        try (Statement stmt = con.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                String id = rs.getString("user_uuid");
                String email = rs.getString("email");
                String password = rs.getString("password_hash");
                String role = rs.getString("role");
                String oib = rs.getString("oib");
                String registrationNumber = rs.getString("registration_number");
                String ownerID = rs.getString("owner_uuid");
                Vehicle vehicle = new Vehicle(registrationNumber, ownerID);
            }
        } catch (SQLException e) {
            throw new SQLException();
        }
    }
}
