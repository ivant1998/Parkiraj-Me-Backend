package hr.fer.littlegreen.parkirajme.webservice.restapi.delete.deleteparkingobject;

import hr.fer.littlegreen.parkirajme.webservice.data.database.DatabaseManager;
import hr.fer.littlegreen.parkirajme.webservice.domain.session.TokenManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CompanyDeleteParkingObjectController {

    @NonNull
    private final DatabaseManager databaseManager;

    @NonNull
    private final TokenManager tokenManager;

    public CompanyDeleteParkingObjectController(
        @NonNull DatabaseManager databaseManager,
        @NonNull TokenManager tokenManager
    ) {
        this.databaseManager = databaseManager;
        this.tokenManager = tokenManager;
    }

    @DeleteMapping("/parkingObject/{parkingObjectId}")
    public ResponseEntity<HttpStatus> deleteParkingObject (
        @PathVariable String parkingObjectId,
        @RequestHeader("Authentication-Token") String token
    ) {
        var tokenId = tokenManager.getId(token);
        String companyId = databaseManager.parkingObjectOwner(parkingObjectId);
        String role = databaseManager.getUserRole(tokenId);

        if(companyId != null && tokenId != null && role != null) {
            if(companyId.equals(tokenId) || role.equals("a")) {
                databaseManager.deleteParkingObject(parkingObjectId);
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }

        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

}
