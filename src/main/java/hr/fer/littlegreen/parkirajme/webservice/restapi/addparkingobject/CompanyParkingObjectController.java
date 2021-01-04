package hr.fer.littlegreen.parkirajme.webservice.restapi.addparkingobject;

import hr.fer.littlegreen.parkirajme.webservice.data.database.DatabaseManager;
import hr.fer.littlegreen.parkirajme.webservice.domain.session.TokenManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CompanyParkingObjectController {

    @NonNull
    DatabaseManager databaseManager;

    @NonNull
    TokenManager tokenManager;

    public CompanyParkingObjectController(
        @NonNull DatabaseManager databaseManager,
        @NonNull TokenManager tokenManager
    ) {
        this.databaseManager = databaseManager;
        this.tokenManager = tokenManager;
    }

    @PostMapping("/company/addParkingObject")
    public ResponseEntity<CompanyAddParkingObjectResponse> addParking(
        @RequestBody CompanyParkingObjectRequestBody parkingObjectRequestBody,
        @RequestHeader("Authentication-Token") String token
    ) {
        var companyId = tokenManager.getId(token);
        if (companyId != null) {
            try {
                String id = databaseManager.addParkingObject(parkingObjectRequestBody, companyId);
                return new ResponseEntity<>(new CompanyAddParkingObjectResponse(id, null), HttpStatus.CREATED);
            } catch(IllegalArgumentException ex) {
                return new ResponseEntity<>(new CompanyAddParkingObjectResponse(null, ex.getMessage()), HttpStatus.CONFLICT);
            }
        }
        return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
    }
}
