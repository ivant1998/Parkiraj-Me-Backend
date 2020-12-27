package hr.fer.littlegreen.parkirajme.webservice.domain.session;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.Base64;
import java.util.Map;

public class TokenManagerImpl implements TokenManager {

    @NonNull
    private final Map<String, IdAndExpiration> storageToken;

    @NonNull
    private final Map<String, TokenAndExpiration> storageId;

    @NonNull
    private final SecureRandom secureRandom;

    @NonNull
    private final Base64.Encoder base64Encoder;

    public TokenManagerImpl(
        @NonNull Map<String, IdAndExpiration> storageToken,
        @NonNull Map<String, TokenAndExpiration> storageId,
        @NonNull SecureRandom secureRandom,
        @NonNull Base64.Encoder base64Encoder
    ) {
        this.storageToken = storageToken;
        this.storageId = storageId;
        this.secureRandom = secureRandom;
        this.base64Encoder = base64Encoder;
    }

    @NonNull
    @Override
    public String generateToken(String id) {
        var currentDate = LocalDate.now();
        var endDate = currentDate.plusDays(30);
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        var token = base64Encoder.encodeToString(randomBytes);
        storageToken.put(token, new IdAndExpiration(id, endDate));
        storageId.put(id, new TokenAndExpiration(token, endDate));
        //printStorages();
        return token;
    }

    @NonNull
    @Override
    public boolean tokenValid(String token) {
        if(!storageToken.containsKey(token)) return false;
        return storageToken.get(token).getExpiration().compareTo(LocalDate.now()) >= 0;
    }

    @Nullable
    @Override
    public String getId(String token) {
        var value = storageToken.get(token);
        return value != null ? value.getId() : null;
    }

    @Nullable
    @Override
    public String removeByToken(String token) {
        var removed = storageToken.remove(token);
        if(removed != null) {
            storageId.remove(removed.getId());
            //printStorages();
            return token;
        }
        return null;
    }

    @Nullable
    @Override
    public String removeById(String id) {
        var removed = storageId.remove(id);
        if(removed != null) {
            storageToken.remove(removed.getToken());
            //printStorages();
            return id;
        }

        return null;

    }

    private void printStorages() {
        System.out.println("Storage token: ");
        for(var key : storageToken.keySet()) {
            System.out.println(key +": " + storageToken.get(key).getId() + ", " + storageToken.get(key).getExpiration());
        }
        System.out.println("------------");
        System.out.println("Storage Id: ");
        for(var key : storageId.keySet()) {
            System.out.println(key +": " + storageId.get(key).getToken() + ", " + storageId.get(key).getExpiration());
        }

        System.out.println();
    }
}
