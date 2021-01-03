package hr.fer.littlegreen.parkirajme.webservice.restapi.registeredusers;

import hr.fer.littlegreen.parkirajme.webservice.data.database.DatabaseManager;
import hr.fer.littlegreen.parkirajme.webservice.domain.models.User;
import hr.fer.littlegreen.parkirajme.webservice.domain.session.TokenManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RegisteredUsersController {

    @NonNull
    private final DatabaseManager databaseManager;

    @NonNull
    private final TokenManager tokenManager;

    public RegisteredUsersController(
        @NonNull DatabaseManager databaseManager,
        @NonNull TokenManager tokenManager
    ) {
        this.databaseManager = databaseManager;
        this.tokenManager = tokenManager;
    }

    @GetMapping("/registeredUsers")
    public ResponseEntity<List<User>> registeredUsers(@RequestHeader("Authentication-Token") String token) {
        String id = tokenManager.getId(token);
        if(id == null) return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        String role = databaseManager.getUserRole(id);
        if(!role.equals("a")) return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        List<User> list = databaseManager.getRegisteredUsers();
        return new ResponseEntity<>(list, HttpStatus.ACCEPTED);
    }
}
