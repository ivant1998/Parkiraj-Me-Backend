package hr.fer.littlegreen.parkirajme.webservice.restapi.register.user;

import hr.fer.littlegreen.parkirajme.webservice.data.database.DatabaseManager;
import hr.fer.littlegreen.parkirajme.webservice.domain.session.TokenManager;
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
        var userId = databaseManager.registerUser(regUserReqBody);
        if (userId != null) {
            var token = tokenManager.generateToken(userId);
            return new ResponseEntity<>(new RegisterUserResponse(token, userId), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(new RegisterUserResponse(null, null), HttpStatus.CONFLICT);
    }
}
