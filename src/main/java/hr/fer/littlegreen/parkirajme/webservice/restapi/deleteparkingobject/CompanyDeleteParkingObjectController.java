package hr.fer.littlegreen.parkirajme.webservice.restapi.deleteparkingobject;

import hr.fer.littlegreen.parkirajme.webservice.data.database.DatabaseManager;
import hr.fer.littlegreen.parkirajme.webservice.data.database.DatabaseManagerImpl;
import hr.fer.littlegreen.parkirajme.webservice.domain.models.ParkingObject;
import hr.fer.littlegreen.parkirajme.webservice.domain.session.TokenManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
public class CompanyDeleteParkingObjectController {

    @NonNull
    private final DatabaseManager databaseManager;

    @NonNull
    private final TokenManager tokenManager;

    public CompanyDeleteParkingObjectController(
        DatabaseManager databaseManager,
        TokenManager tokenManager
    ) {
        this.databaseManager = databaseManager;
        this.tokenManager = tokenManager;
    }

    @DeleteMapping("/parkingObject/{parkingObjectId}")
    public ResponseEntity<HttpStatus> deleteParkingObject (
        @PathVariable String parkingObjectId,
        @RequestHeader("Authentication-token") String token
    ) {
        var tokenId = tokenManager.getId(token);
        String companyId = databaseManager.parkingObjectOwner(parkingObjectId);
        String role = databaseManager.getUserRole(tokenId);

        if(companyId != null && tokenId != null && role != null) {
            if(companyId.equals(tokenId) || role.equals("a")) {
                databaseManager.deleteParkingObject(parkingObjectId);
                return new ResponseEntity<>(HttpStatus.ACCEPTED);
            }
        }

        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

}
