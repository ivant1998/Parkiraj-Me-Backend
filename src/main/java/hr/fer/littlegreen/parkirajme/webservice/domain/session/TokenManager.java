package hr.fer.littlegreen.parkirajme.webservice.domain.session;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public interface TokenManager {

    @NonNull
    String generateToken(String id);

    @Nullable
    String checkIfTokenValid(String token);
}
