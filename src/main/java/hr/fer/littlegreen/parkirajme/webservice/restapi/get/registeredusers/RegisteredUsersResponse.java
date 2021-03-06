package hr.fer.littlegreen.parkirajme.webservice.restapi.get.registeredusers;

import hr.fer.littlegreen.parkirajme.webservice.domain.models.User;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Objects;

public class RegisteredUsersResponse {

    @NonNull
    List<User> userList;

    public RegisteredUsersResponse (
        @NonNull List<User> userList
    ) {
        this.userList = userList;
    }

    @NonNull
    public List<User> getUserList() {
        return userList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        RegisteredUsersResponse that = (RegisteredUsersResponse) o;
        return userList.equals(that.userList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userList);
    }
}
