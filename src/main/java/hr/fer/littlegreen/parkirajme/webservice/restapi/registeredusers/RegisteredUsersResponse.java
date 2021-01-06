package hr.fer.littlegreen.parkirajme.webservice.restapi.registeredusers;

import hr.fer.littlegreen.parkirajme.webservice.domain.models.User;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Objects;

public class RegisteredUsersResponse {

    @NonNull
    List<User> UserList;

    public RegisteredUsersResponse(
        @NonNull List<User> userList
    ) {
        UserList = userList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        RegisteredUsersResponse that = (RegisteredUsersResponse) o;
        return UserList.equals(that.UserList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(UserList);
    }
}
