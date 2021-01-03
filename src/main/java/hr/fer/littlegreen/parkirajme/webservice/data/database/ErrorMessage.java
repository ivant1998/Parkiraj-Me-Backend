package hr.fer.littlegreen.parkirajme.webservice.data.database;


import java.sql.SQLException;

public enum ErrorMessage {

    APP_USER_EMAIL_UN("Već postoji korisnik sa zadanim e-mailom"),
    APP_USER_EMAIL_CHECK("E-mail je zadan u krivom formatu"),
    APP_USER_OIB_AND_ROLE_CHECK("OIB je u krivom formatu"),
    APP_USER_OIB_UN("Već postoji korisnik sa zadanim OIB-om"),

    PERSON_CHECK_CC("Kartica mora sadržavati točno 16 brojeva"),

    VEHICLE_PK("Već ste evidentirali automobil sa zadanom registracijom"),
    VEHICLE_REGISTRATION_NUMBER_CHECK("Registracija vozila je zadana u krivom formatu"),

    COMPANY_UN("Već postoji kompanija sa zadanim adresom sjedišta"),
    COMPANY_NAME_UN("Već postoji kompanija sa zadanim imenom"),

    PARKING_OBJECT_ADDRESS_UN("Već postoji parkirni objekt sa zadanom adresom"),
    PARKING_OBJECT_NAME_UN("Već postoji parkirni objekt sa zadanim imenom"),

    RESERVATION_PKEY("Već imate takvu rezervaciju"),
    RESERVATION_CHECK("Vremenska razlika kraja i početka rezervacije mora biti djeljiva sa 30 minuta"),

    FK_RESERVATION_PARKING_OBJECT("Ne možete stvoriti rezervaciju za nepostojeći objekt"),
    FK_RESERVATION_VEHICLE("Ne možete stvoriti rezervaciju za nepostojeće vozilo");


    public final String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public static String getMessage(SQLException e) {
        if(e.getMessage().contains("constraint")) {
            String violatedConstraint = e.getMessage().split("constraint")[1].strip().split("Detail")[0].strip().replace("\"","").toUpperCase();
            for(var value : ErrorMessage.values()) {
                if(value.name().equals(violatedConstraint)) return value.message;
            }
        }
        return "Unijeli ste krive podatke";
    }

}
