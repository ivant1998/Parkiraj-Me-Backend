package hr.fer.littlegreen.parkirajme.webservice.database;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public interface DatabaseManager {

    @Nullable
    String checkLoginCredentials(@NonNull String email, @NonNull String password);
}
