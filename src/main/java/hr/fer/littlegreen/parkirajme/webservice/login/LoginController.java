package hr.fer.littlegreen.parkirajme.webservice.login;

import hr.fer.littlegreen.parkirajme.webservice.database.DatabaseManager;
import hr.fer.littlegreen.parkirajme.webservice.session.TokenManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @NonNull
    private final DatabaseManager databaseManager;

    @NonNull
    private final TokenManager tokenManager;

    public LoginController(
        @NonNull DatabaseManager databaseManager,
        @NonNull TokenManager tokenManager
    ) {
        this.databaseManager = databaseManager;
        this.tokenManager = tokenManager;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequestBody loginRequestBody) {
        var user = databaseManager.checkLoginCredentials(loginRequestBody.getEmail(), loginRequestBody.getPassword());
        if (user != null) {
            var token = tokenManager.generateToken(user.getId());
            return new ResponseEntity<>(new LoginResponse(token), HttpStatus.OK);
        }
        return new ResponseEntity<>(new LoginResponse(null), HttpStatus.UNAUTHORIZED);
    }
}
