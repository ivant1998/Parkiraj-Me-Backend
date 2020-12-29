package hr.fer.littlegreen.parkirajme.webservice.restapi.getcompanyparkingobject;

import hr.fer.littlegreen.parkirajme.webservice.data.database.DatabaseManager;
import hr.fer.littlegreen.parkirajme.webservice.domain.models.ParkingObject;
import hr.fer.littlegreen.parkirajme.webservice.domain.session.TokenManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
public class CompanyGetParkingObjectsController {

    @NonNull
    private final DatabaseManager databaseManager;

    @NonNull
    private final TokenManager tokenManager;

    public CompanyGetParkingObjectsController(
        @NonNull DatabaseManager databaseManager,
        @NonNull TokenManager tokenManager
    ) {
        this.databaseManager = databaseManager;
        this.tokenManager = tokenManager;
    }

    @GetMapping("/company/{companyId}/parkingObjects")
    public ResponseEntity<List<ParkingObject>> parkingObjects(
        @PathVariable String companyId,
        @RequestHeader("Authentication-Token") String token
    ) {
        var companyTokenId = tokenManager.getId(token);
        if (companyTokenId.equals(companyId)) {
            var objects = databaseManager.getCompanyParkingObjects(companyId);
            return new ResponseEntity<>(Objects.requireNonNullElseGet(objects, List::of), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
    }

}