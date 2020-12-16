package hr.fer.littlegreen.parkirajme.webservice.domain.session;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.Base64;
import java.util.Map;

public class TokenManagerImpl implements TokenManager {

    @NonNull
    private final Map<Token, String> storage;

    @NonNull
    private final SecureRandom secureRandom;

    @NonNull
    private final Base64.Encoder base64Encoder;

    public TokenManagerImpl(
        @NonNull Map<Token, String> storage,
        @NonNull SecureRandom secureRandom,
        @NonNull Base64.Encoder base64Encoder
    ) {
        this.storage = storage;
        this.secureRandom = secureRandom;
        this.base64Encoder = base64Encoder;
    }

    @NonNull
    public String generateToken(String id) {
        var currentDate = LocalDate.now();
        var endDate = currentDate.plusDays(30);
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        var token = base64Encoder.encodeToString(randomBytes);
        storage.put(new Token(token, endDate), id);
        return token;
    }

    @Nullable
    public String checkIfTokenValid(String token) {
        for (var entry : storage.entrySet()) {
            var storedToken = entry.getKey();
            if (storedToken.getToken().equals(token) && storedToken.getEndDate().compareTo(LocalDate.now()) >= 0) {
                return entry.getValue();
            }
        }
        return null;
    }
}
