package hr.fer.littlegreen.parkirajme.webservice.register.company;

import org.springframework.lang.NonNull;

import java.util.Objects;

public class RegisterCompanyRequestBody {

    @NonNull
    private final String OIB;

    @NonNull
    private final String name;

    @NonNull
    private final String address;

    @NonNull
    private final String email;

    @NonNull
    private final String password;

    public RegisterCompanyRequestBody ( @NonNull String OIB, @NonNull String name, @NonNull String address,
                                        @NonNull String email, @NonNull String password) {
        this.OIB = OIB;
        this.name = name;
        this.address = address;
        this.email = email;
        this.password = password;
    }

    @NonNull
    public String getName() { return name; }

    @NonNull
    public String getAddress() { return address; }

    @NonNull
    public String getOib() { return OIB; }

    @NonNull
    public String getEmail() { return email; }

    @NonNull
    public String getPassword() { return password; }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        RegisterCompanyRequestBody that = (RegisterCompanyRequestBody) o;
        return email.equals(that.email) &&
            OIB.equals(that.OIB);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, OIB);
    }

}
