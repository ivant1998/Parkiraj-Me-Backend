package hr.fer.littlegreen.parkirajme.webservice.restapi.get.getparkingobjects;

import hr.fer.littlegreen.parkirajme.webservice.data.database.DatabaseManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class GetParkingObjectsController {

    @NonNull
    private final DatabaseManager databaseManager;

    public GetParkingObjectsController(
        @NonNull DatabaseManager databaseManager
    ) {
        this.databaseManager = databaseManager;
    }

    @GetMapping("/parkingObjects")
    public ResponseEntity<GetParkingObjectsResponse> parkingObjects() {
        var objects = databaseManager.getParkingObjects();
        return new ResponseEntity<>(new GetParkingObjectsResponse(objects), HttpStatus.OK);
    }

}
