package hr.fer.littlegreen.parkirajme.webservice.restapi.editperson;



import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Objects;

public class EditPersonRequestBody {
    

    @NonNull
    private final String firstName;

    @NonNull
    private final String lastName;


    @NonNull
    private final String creditCardNumber;

    @NonNull
    private final String creditCardExpirationDate;

    public EditPersonRequestBody(
        @NonNull String firstName,
        @NonNull String lastName,
        @NonNull String creditCardNumber,
        @NonNull String creditCardExpirationDate
    ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.creditCardNumber = creditCardNumber;
        this.creditCardExpirationDate = creditCardExpirationDate;
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
       EditPersonRequestBody that = (EditPersonRequestBody) o;
        return firstName.equals(that.firstName) &&
            lastName.equals(that.lastName) &&
            creditCardNumber.equals(that.creditCardNumber) &&
            creditCardExpirationDate.equals(that.creditCardExpirationDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, creditCardNumber, creditCardExpirationDate
        );
    }
}
