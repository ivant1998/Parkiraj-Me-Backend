package hr.fer.littlegreen.parkirajme.webservice.restapi.register.user;

import org.springframework.lang.NonNull;

import java.util.List;
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
    private final List<String> regPlates;

    @NonNull
    private final String creditCard;

    @NonNull
    private final String creditCardExpirationDate;

    public RegisterUserRequestBody(
        @NonNull String email,
        @NonNull String password,
        @NonNull String name,
        @NonNull String surname,
        @NonNull String OIB,
        @NonNull List<String> regPlates,
        @NonNull String creditCard,
        @NonNull String creditCardExpirationDate
    ) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.OIB = OIB;
        this.regPlates = regPlates;
        this.creditCard = creditCard;
        this.creditCardExpirationDate = creditCardExpirationDate;
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
    public List<String> getRegPlates() {
        return regPlates;
    }

    @NonNull
    public String getCreditCard() {
        return creditCard;
    }

    @NonNull
    public String getCreditCardExpirationDate() {
        return creditCardExpirationDate;
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
            regPlates.equals(that.regPlates) &&
            creditCard.equals(that.creditCard) &&
            creditCardExpirationDate.equals(that.creditCardExpirationDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, password, name, surname, OIB, regPlates, creditCard, creditCardExpirationDate);
    }
}
