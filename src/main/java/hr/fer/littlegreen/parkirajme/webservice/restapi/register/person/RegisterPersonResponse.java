package hr.fer.littlegreen.parkirajme.webservice.restapi.register.person;

import hr.fer.littlegreen.parkirajme.webservice.restapi.Response;
import org.springframework.lang.Nullable;

import java.util.Objects;

public class RegisterPersonResponse extends Response {

    @Nullable
    private final String token;

    @Nullable
    private final String userUuid;

    public RegisterPersonResponse(@Nullable String token, @Nullable String userUuid, @Nullable String error) {
        super(error);
        this.token = token;
        this.userUuid = userUuid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        RegisterPersonResponse that = (RegisterPersonResponse) o;
        return Objects.equals(token, that.token) && Objects.equals(userUuid, that.userUuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(token, userUuid);
    }

    @Nullable
    public String getToken() {
        return token;
    }

    @Nullable
    public String getUserUuid() {
        return userUuid;
    }

    @Nullable
    public String getError() {return error;}
}
