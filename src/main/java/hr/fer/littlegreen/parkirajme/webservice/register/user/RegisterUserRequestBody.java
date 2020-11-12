package hr.fer.littlegreen.parkirajme.webservice.register.user;

import org.springframework.lang.NonNull;

import java.util.Objects;

public class RegisterUserRequestBody {

    @NonNull
    private final String email;

    @NonNull
    private final String password;

    @NonNull
    private final String name;

    @NonNull
    private final String surname;

    @NonNull
    private final String OIB;

    @NonNull
    private final String regPlate;

    @NonNull
    private final String creditcard;

    public RegisterUserRequestBody(
        @NonNull String email,
        @NonNull String password,
        @NonNull String name,
        @NonNull String surname,
        @NonNull String OIB,
        @NonNull String regPlate,
        @NonNull String creditcard
    ) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.OIB = OIB;
        this.regPlate = regPlate;
        this.creditcard = creditcard;
    }

    @NonNull
    public String getEmail() {
        return email;
    }

    @NonNull
    public String getPassword() {
        return password;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    public String getSurname() {
        return surname;
    }

    @NonNull
    public String getOIB() {
        return OIB;
    }

    @NonNull
    public String getRegPlate() {
        return regPlate;
    }

    @NonNull
    public String getCreditcard() {
        return creditcard;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        RegisterUserRequestBody that = (RegisterUserRequestBody) o;
        return email.equals(that.email) &&
            password.equals(that.password) &&
            name.equals(that.name) &&
            surname.equals(that.surname) &&
            OIB.equals(that.OIB) &&
            regPlate.equals(that.regPlate) &&
            creditcard.equals(that.creditcard);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, password, name, surname, OIB, regPlate, creditcard);
    }
}
