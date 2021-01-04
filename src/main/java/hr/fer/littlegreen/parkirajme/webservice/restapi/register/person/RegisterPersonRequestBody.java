package hr.fer.littlegreen.parkirajme.webservice.restapi.register.person;

import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Objects;

public class RegisterPersonRequestBody {

    @NonNull
    private final String email;

    @NonNull
    private final String password;

    @NonNull
    private final String firstName;

    @NonNull
    private final String lastName;

    @NonNull
    private final String oib;

    @NonNull
    private final List<String> registrationNumbers;

    @NonNull
    private final String creditCardNumber;

    @NonNull
    private final String creditCardExpirationDate;

    public RegisterPersonRequestBody(
        @NonNull String email,
        @NonNull String password,
        @NonNull String firstName,
        @NonNull String lastName,
        @NonNull String oib,
        @NonNull List<String> registrationNumbers,
        @NonNull String creditCardNumber,
        @NonNull String creditCardExpirationDate
    ) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.oib = oib;
        this.registrationNumbers = registrationNumbers;
        this.creditCardNumber = creditCardNumber;
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
    public String getFirstName() {
        return firstName;
    }

    @NonNull
    public String getLastName() {
        return lastName;
    }

    @NonNull
    public String getOib() {
        return oib;
    }

    @NonNull
    public List<String> getRegistrationNumbers() {
        return registrationNumbers;
    }

    @NonNull
    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    @NonNull
    public String getCreditCardExpirationDate() {
        return creditCardExpirationDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        RegisterPersonRequestBody that = (RegisterPersonRequestBody) o;
        return email.equals(that.email) &&
            password.equals(that.password) &&
            firstName.equals(that.firstName) &&
            lastName.equals(that.lastName) &&
            oib.equals(that.oib) &&
            registrationNumbers.equals(that.registrationNumbers) &&
            creditCardNumber.equals(that.creditCardNumber) &&
            creditCardExpirationDate.equals(that.creditCardExpirationDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, password, firstName, lastName, oib,
            registrationNumbers, creditCardNumber, creditCardExpirationDate
        );
    }
}
