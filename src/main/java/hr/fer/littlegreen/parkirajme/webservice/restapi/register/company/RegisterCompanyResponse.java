package hr.fer.littlegreen.parkirajme.webservice.restapi.register.company;

import hr.fer.littlegreen.parkirajme.webservice.restapi.Response;
import org.springframework.lang.Nullable;

import java.util.Objects;

public class RegisterCompanyResponse extends Response {

    @Nullable
    private final String token;

    @Nullable
    private final String id;

    public RegisterCompanyResponse(@Nullable String token, @Nullable String id, @Nullable String error) {
        super(error);
        this.token = token;
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        RegisterCompanyResponse that = (RegisterCompanyResponse) o;
        return Objects.equals(token, that.token) && Objects.equals(id, that.id) && Objects.equals(error, that.error);
    }

    @Override
    public int hashCode() {
        return Objects.hash(token, id, error);
    }

    @Nullable
    public final String getToken() { return token; }

    @Nullable
    public String getId() {
        return id;
    }

    @Nullable
    public String getError() {return error;}
}
