package hr.fer.littlegreen.parkirajme.webservice.domain.session;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public interface TokenManager {

    @NonNull
    String generateToken(String id);

    @NonNull
    boolean tokenValid(String token);

    @Nullable
    String getId(String token);

    @Nullable
    String removeByToken(String token);

    @Nullable
    String removeById(String id);
}
