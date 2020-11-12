package hr.fer.littlegreen.parkirajme.webservice.register.company;

import hr.fer.littlegreen.parkirajme.webservice.database.DatabaseManager;
import hr.fer.littlegreen.parkirajme.webservice.session.TokenManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
        databaseManager.registerCompany(registerCompanyRequestBody);
        return new ResponseEntity<>(new RegisterCompanyResponse(null), HttpStatus.CREATED);
    }

}
