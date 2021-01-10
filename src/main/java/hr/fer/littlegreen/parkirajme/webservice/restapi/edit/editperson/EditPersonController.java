package hr.fer.littlegreen.parkirajme.webservice.restapi.edit.editperson;

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
public class EditPersonController {

    DatabaseManager databaseManager;

    TokenManager tokenManager;

    public EditPersonController(DatabaseManager databaseManager, TokenManager tokenManager) {
        this.tokenManager = tokenManager;
        this.databaseManager = databaseManager;
    }

    @PatchMapping("/person/{personId}")
    public ResponseEntity<Response> editPerson(
        @RequestBody EditPersonRequestBody editPersonRequestBody,
        @PathVariable String personId,
        @RequestHeader("Authentication-Token") String token
    ) {

        String id = tokenManager.getId(token);
        if (id != null && personId.equals(id)) {
            try {
                databaseManager.editPerson(
                    id,
                    editPersonRequestBody.getFirstName(),
                    editPersonRequestBody.getLastName(),
                    editPersonRequestBody.getCreditCardNumber(),
                    editPersonRequestBody.getCreditCardExpirationDate()
                );
                return new ResponseEntity<>(new Response(null), HttpStatus.OK);
            } catch (IllegalArgumentException ex) {
                return new ResponseEntity<>(new Response(ex.getMessage()), HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>(new Response("Nemate ovlasti"), HttpStatus.UNAUTHORIZED);

    }

}
