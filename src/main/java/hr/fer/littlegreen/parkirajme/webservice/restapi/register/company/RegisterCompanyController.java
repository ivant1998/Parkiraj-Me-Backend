package hr.fer.littlegreen.parkirajme.webservice.restapi.register.company;

import hr.fer.littlegreen.parkirajme.webservice.data.database.DatabaseManager;
import hr.fer.littlegreen.parkirajme.webservice.domain.session.TokenManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegisterCompanyController {

    @NonNull
    private final DatabaseManager databaseManager;

    @NonNull
    private final TokenManager tokenManager;

    public RegisterCompanyController(
        @NonNull DatabaseManager databaseManager,
        @NonNull TokenManager tokenManager
    ) {
        this.databaseManager = databaseManager;
        this.tokenManager = tokenManager;
    }

    @PostMapping("/register/company")
    public ResponseEntity<RegisterCompanyResponse> registerCompany(
        @RequestBody RegisterCompanyRequestBody registerCompanyRequestBody

        ) {
        try{
            var userId = databaseManager.registerCompany(registerCompanyRequestBody);
            var token = tokenManager.generateToken(userId);
            return new ResponseEntity<>(new RegisterCompanyResponse(token, userId, null), HttpStatus.CREATED);
        } catch (IllegalArgumentException ex) {
            return new ResponseEntity<>(new RegisterCompanyResponse(null, null, ex.getMessage()), HttpStatus.CONFLICT);
        }
    }

}
