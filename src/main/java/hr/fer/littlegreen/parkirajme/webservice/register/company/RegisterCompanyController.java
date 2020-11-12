package hr.fer.littlegreen.parkirajme.webservice.register.company;

import hr.fer.littlegreen.parkirajme.webservice.database.DatabaseManager;
import hr.fer.littlegreen.parkirajme.webservice.session.TokenManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public class RegisterCompanyController {

    @NonNull
    private final DatabaseManager databaseManager;

    @NonNull
    private final TokenManager tokenManager;

    public RegisterCompanyController (
        @NonNull DatabaseManager databaseManager,
        @NonNull TokenManager tokenManager
    ) {
        this.databaseManager = databaseManager;
        this.tokenManager = tokenManager;
    }

    @PostMapping("/register/company")
    public ResponseEntity<RegisterCompanyResponse> registerCompany(@RequestBody RegisterCompanyRequestBody registerCompanyRequestBody) {
        String userId = databaseManager.registerCompany(registerCompanyRequestBody);
        if (userId != null) {
            var token = tokenManager.generateToken(userId);
            return new ResponseEntity<>(new RegisterCompanyResponse(token), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(new RegisterCompanyResponse(null), HttpStatus.CONFLICT);
    }

}
