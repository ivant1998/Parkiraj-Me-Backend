package hr.fer.littlegreen.parkirajme.webservice.session;

import org.springframework.lang.Nullable;

public interface TokenManager {

    void generateToken(String id);

    @Nullable
    String checkIfTokenValid(String token);
}
