package hr.fer.littlegreen.parkirajme.webservice.restapi.edit.editcompany;

import hr.fer.littlegreen.parkirajme.webservice.data.database.DatabaseManager;
import hr.fer.littlegreen.parkirajme.webservice.domain.session.TokenManager;
import hr.fer.littlegreen.parkirajme.webservice.restapi.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EditCompanyController {

    DatabaseManager databaseManager;

    TokenManager tokenManager;

    public EditCompanyController(
        DatabaseManager databaseManager,
        TokenManager tokenManager
    ) {
        this.databaseManager = databaseManager;
        this.tokenManager = tokenManager;
    }

    @PatchMapping("/company/{companyId}")
    public ResponseEntity<Response> editCompany(
        @RequestBody EditCompanyRequestBody editCompanyRequestBody,
        @PathVariable String companyId,
        @RequestHeader("Authentication-Token") String token
    ) {

        String id = tokenManager.getId(token);
        if (id != null && companyId.equals(id)) {
            try {
                databaseManager.editCompany(
                    id,
                    editCompanyRequestBody.getName(),
                    editCompanyRequestBody.getHeadquarterAddress()
                );
                return new ResponseEntity<>(new Response(null), HttpStatus.OK);
            } catch (IllegalArgumentException ex) {
                return new ResponseEntity<>(new Response(ex.getMessage()), HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>(new Response("Nemate ovlasti"), HttpStatus.UNAUTHORIZED);
    }
}
