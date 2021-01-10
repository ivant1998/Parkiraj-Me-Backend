package hr.fer.littlegreen.parkirajme.webservice.restapi.edit.editparkingobject;

import hr.fer.littlegreen.parkirajme.webservice.data.database.DatabaseManager;
import hr.fer.littlegreen.parkirajme.webservice.domain.session.TokenManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EditParkingObjectController {

    @NonNull
    private final DatabaseManager databaseManager;

    @NonNull
    private final TokenManager tokenManager;

    public EditParkingObjectController(
        @NonNull TokenManager tokenManager,
        @NonNull DatabaseManager databaseManager
    ) {
        this.databaseManager = databaseManager;
        this.tokenManager = tokenManager;
    }

    @PatchMapping("/parkingObject/{parkingObjectId}")
    public ResponseEntity<HttpStatus> updateParkingObject(
        @PathVariable String parkingObjectId,
        @RequestBody EditParkingObjectRequestBody editParkingObjectRequestBody,
        @RequestHeader("Authentication-Token") String token
    ) {
        var tokenId = tokenManager.getId(token);
        String companyId = databaseManager.parkingObjectOwner(parkingObjectId);

        if (companyId != null && tokenId != null) {
            if (companyId.equals(tokenId)) {
                databaseManager.editParkingObject(parkingObjectId, editParkingObjectRequestBody);
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }

        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
}
