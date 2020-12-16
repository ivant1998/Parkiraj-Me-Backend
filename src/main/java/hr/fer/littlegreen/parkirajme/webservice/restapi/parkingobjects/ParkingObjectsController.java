package hr.fer.littlegreen.parkirajme.webservice.restapi.parkingobjects;

import hr.fer.littlegreen.parkirajme.webservice.data.database.DatabaseManager;
import hr.fer.littlegreen.parkirajme.webservice.domain.models.ParkingObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ParkingObjectsController {

    @NonNull
    private final DatabaseManager databaseManager;

    public ParkingObjectsController(
        @NonNull DatabaseManager databaseManager
    ) {
        this.databaseManager = databaseManager;
    }

    @GetMapping("/parkingObjects")
    public ResponseEntity<List<ParkingObject>> parkingObjects() {
        var objects = databaseManager.getParkingObjects();
        if (objects == null) {
            return new ResponseEntity<>(List.of(), HttpStatus.OK);
        }
        return new ResponseEntity<>(objects, HttpStatus.OK);
    }

}
