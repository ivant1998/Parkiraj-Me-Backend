package hr.fer.littlegreen.parkirajme.webservice.register.user;

import hr.fer.littlegreen.parkirajme.webservice.database.DatabaseManager;
import hr.fer.littlegreen.parkirajme.webservice.session.TokenManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegisterUserController {

    @NonNull
    private final DatabaseManager databaseManager;

    @NonNull
    private final TokenManager tokenManager;

    public RegisterUserController(
        @NonNull DatabaseManager databaseManager,
        @NonNull TokenManager tokenManager
    ) {
        this.databaseManager = databaseManager;
        this.tokenManager = tokenManager;
    }

    @PostMapping("/register/user")
    public ResponseEntity<RegisterUserResponse> regUser(@RequestBody RegisterUserRequestBody regUserReqBody){
        var userId = databaseManager.checkUserExists(regUserReqBody.getEmail(),regUserReqBody.getOIB());
        if (userId == null) {
            databaseManager.registerUser(regUserReqBody);
            return new ResponseEntity<>(new RegisterUserResponse(null), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(new RegisterUserResponse(null), HttpStatus.CONFLICT);
    }
}
