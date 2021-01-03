package hr.fer.littlegreen.parkirajme.webservice.restapi.register.company;

import org.springframework.lang.NonNull;

import java.util.Objects;

public class RegisterCompanyRequestBody {

    @NonNull
    private final String oib;

    @NonNull
    private final String name;

    @NonNull
    private final String headquarterAddress;

    @NonNull
    private final String email;

    @NonNull
    private final String password;

    public RegisterCompanyRequestBody(
        @NonNull String oib, @NonNull String name, @NonNull String headquarterAddress,
        @NonNull String email, @NonNull String password
    ) {
        this.oib = oib;
        this.name = name;
        this.headquarterAddress = headquarterAddress;
        this.email = email;
        this.password = password;
    }

    @NonNull
    public String getName() { return name; }

    @NonNull
    public String getHeadquarterAddress() { return headquarterAddress; }

    @NonNull
    public String getOib() { return oib; }

    @NonNull
    public String getEmail() { return email; }

    @NonNull
    public String getPassword() { return password; }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (!(o instanceof RegisterCompanyRequestBody)) { return false; }
        RegisterCompanyRequestBody that = (RegisterCompanyRequestBody) o;
        return oib.equals(that.oib) &&
            getName().equals(that.getName()) &&
            getHeadquarterAddress().equals(that.getHeadquarterAddress()) &&
            getEmail().equals(that.getEmail()) &&
            getPassword().equals(that.getPassword());
    }

    @Override
    public int hashCode() {
        return Objects.hash(oib, getName(), getHeadquarterAddress(), getEmail(), getPassword());
    }
}
