package hr.fer.littlegreen.parkirajme.webservice.queries;

import hr.fer.littlegreen.parkirajme.webservice.dao.Administrator;
import hr.fer.littlegreen.parkirajme.webservice.dao.Company;
import hr.fer.littlegreen.parkirajme.webservice.dao.Person;
import hr.fer.littlegreen.parkirajme.webservice.dao.Vehicle;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class Query{
    /*public static void queryUser(Connection con) throws SQLException {
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

     */
    public static Administrator queryAdministrator(Connection con) throws SQLException {
        String email = null;
        String password = null;
        String role = null;
        String oib = null;
        String id = null;
        String query = "select email, password_hash, role, oib, administrator_uuid from administrator join app_user on administrator_uuid = user_uuid";
        try (Statement stmt = con.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                //String id = rs.getString("user_uuid");
                email = rs.getString("email");
                password = rs.getString("password_hash");
                role = rs.getString("role");
                oib = rs.getString("oib");
                id = rs.getString("administrator_uuid");
            }
        } catch (SQLException e) {
            throw new SQLException();
        }
        return new Administrator(id, email, password, role, oib);
    }
    public static Person queryPerson(Connection con) throws SQLException {
        String email = null;
        String password = null;
        String role = null;
        String oib = null;
        String id = null;
        String firstName = null;
        String lastName = null;
        String creditCard = null;
        String query = "select email, password_hash, role, oib, person_uuid, first_name, last_name, credit_card_number from person join app_user on person_uuid = user_uuid";
        try (Statement stmt = con.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                //String id = rs.getString("user_uuid");
                email = rs.getString("email");
                password = rs.getString("password_hash");
                role = rs.getString("role");
                oib = rs.getString("oib");
                id = rs.getString("person_uuid");
                firstName = rs.getString("first_name");
                lastName = rs.getString("last_name");
                creditCard = rs.getString("credit_card_number");
            }
        } catch (SQLException e) {
            throw new SQLException();
        }
        return new Person(id,email,password, role,oib, firstName, lastName, creditCard);
    }
    public static Company queryCompany(Connection con) throws SQLException {
        String email = null;
        String password = null;
        String role = null;
        String oib = null;
        String id = null;
        String name = null;
        String address = null;
        String query = "select email, password_hash, role, oib, company_uuid, name, headquarter_adress from app_user join company on company_uuid = user_uuid";
        try (Statement stmt = con.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                //String id = rs.getString("user_uuid");
                email = rs.getString("email");
                password = rs.getString("password_hash");
                role = rs.getString("role");
                oib = rs.getString("oib");
                id = rs.getString("company_uuid");
                name = rs.getString("name");
                address = rs.getString("headquarter_adress");
            }
        } catch (SQLException e) {
            throw new SQLException();
        }
        return new Company(id, email, password, role, oib, name, address);
    }
    public static Vehicle queryVehicle(Connection con) throws SQLException {
        String registrationNumber = null;
        String ownerID = null;
        String query = "select email, password_hash, role, oib, registration_number, owner_uuid from person join vehicle on owner_uuid = person_uuid";
        try (Statement stmt = con.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                registrationNumber = rs.getString("registration_number");
                ownerID = rs.getString("owner_uuid");
            }
        } catch (SQLException e) {
            throw new SQLException();
        }
        return new Vehicle(registrationNumber, ownerID);
    }
}
