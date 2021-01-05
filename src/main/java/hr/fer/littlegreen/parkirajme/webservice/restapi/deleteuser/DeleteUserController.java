package hr.fer.littlegreen.parkirajme.webservice.restapi.deleteuser;

import hr.fer.littlegreen.parkirajme.webservice.data.database.DatabaseManager;
import hr.fer.littlegreen.parkirajme.webservice.domain.session.TokenManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DeleteUserController {

    @NonNull
    private final DatabaseManager databaseManager;

    @NonNull
    private final TokenManager tokenManager;

    public DeleteUserController(
        DatabaseManager databaseManager,
        TokenManager tokenManager
    ) {
        this.databaseManager = databaseManager;
        this.tokenManager = tokenManager;
    }

    @DeleteMapping("/user/{userId}")
    public ResponseEntity<HttpStatus> deleteUser(
        @PathVariable String userId,
        @RequestHeader("Authentication-token") String token
    ) {
        var tokenId = tokenManager.getId(token);
        String role = databaseManager.getUserRole(tokenId);

        if (tokenId != null && role != null) {
            if (userId.equals(tokenId) || role.equals("a")) {
                databaseManager.deleteUser(userId);
                return new ResponseEntity<>(HttpStatus.ACCEPTED);
            }
        }

        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

}
