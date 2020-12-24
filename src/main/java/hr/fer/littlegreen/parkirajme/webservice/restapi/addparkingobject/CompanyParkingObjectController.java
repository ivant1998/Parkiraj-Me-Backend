package hr.fer.littlegreen.parkirajme.webservice.restapi.addparkingobject;

import hr.fer.littlegreen.parkirajme.webservice.data.database.DatabaseManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CompanyParkingObjectController {

    @NonNull
    DatabaseManager databaseManager;

    public CompanyParkingObjectController(
        @NonNull DatabaseManager databaseManager
    ) {
        this.databaseManager = databaseManager;
    }

    @PostMapping("/company/addParkingObject")
    public ResponseEntity<CompanyAddParkingObjectResponse> addParking(
        @RequestBody CompanyParkingObjectRequestBody parkingObjectRequestBody
    ) {
        String id = databaseManager.addParkingObject(parkingObjectRequestBody);
        if (id != null) {
            return new ResponseEntity<>(new CompanyAddParkingObjectResponse(id), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(null, HttpStatus.CONFLICT);
    }
}
