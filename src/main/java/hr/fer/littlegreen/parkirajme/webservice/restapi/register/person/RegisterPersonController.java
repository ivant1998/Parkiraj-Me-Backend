package hr.fer.littlegreen.parkirajme.webservice.restapi.register.person;

import hr.fer.littlegreen.parkirajme.webservice.data.database.DatabaseManager;
import hr.fer.littlegreen.parkirajme.webservice.domain.session.TokenManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegisterPersonController {

    @NonNull
    private final DatabaseManager databaseManager;

    @NonNull
    private final TokenManager tokenManager;

    public RegisterPersonController(
        @NonNull DatabaseManager databaseManager,
        @NonNull TokenManager tokenManager
    ) {
        this.databaseManager = databaseManager;
        this.tokenManager = tokenManager;
    }

    @PostMapping("/register/person")
    public ResponseEntity<RegisterPersonResponse> regUser(@RequestBody RegisterPersonRequestBody regUserReqBody) {
        if(regUserReqBody.getRegistrationNumbers().size() == 0) return new ResponseEntity<>(new RegisterPersonResponse(null, null, "Morate navesti barem jedno vozilo"), HttpStatus.CONFLICT);
        try {
            var userId = databaseManager.registerPerson(regUserReqBody);
            var token = tokenManager.generateToken(userId);
            return new ResponseEntity<>(new RegisterPersonResponse(token, userId, null), HttpStatus.CREATED);
        } catch (IllegalArgumentException ex) {
            return new ResponseEntity<>(new RegisterPersonResponse(null, null, ex.getMessage()), HttpStatus.CONFLICT);
        }


    }
}
