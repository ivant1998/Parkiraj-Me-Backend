package hr.fer.littlegreen.parkirajme.webservice.restapi.register.company;

import hr.fer.littlegreen.parkirajme.webservice.restapi.Response;
import org.springframework.lang.Nullable;

import java.util.Objects;

public class RegisterCompanyResponse extends Response {

    @Nullable
    private final String token;

    @Nullable
    private final String userUuid;

    public RegisterCompanyResponse(@Nullable String token, @Nullable String userUuid, @Nullable String error) {
        super(error);
        this.token = token;
        this.userUuid = userUuid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        RegisterCompanyResponse that = (RegisterCompanyResponse) o;
        return Objects.equals(token, that.token) && Objects.equals(userUuid, that.userUuid) && Objects.equals(
            error,
            that.error
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(token, userUuid, error);
    }

    @Nullable
    public final String getToken() { return token; }

    @Nullable
    public String getUserUuid() {
        return userUuid;
    }

    @Nullable
    public String getError() {return error;}
}
