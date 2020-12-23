package hr.fer.littlegreen.parkirajme.webservice.restapi.parkingobjects;

import hr.fer.littlegreen.parkirajme.webservice.data.database.DatabaseManager;
import hr.fer.littlegreen.parkirajme.webservice.domain.models.ParkingObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import static java.lang.Math.abs;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

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
        return new ResponseEntity<>(Objects.requireNonNullElseGet(objects, List::of), HttpStatus.OK);
    }

    @GetMapping("/closestParkingObject?lat=x&long=y")
    public ResponseEntity<ParkingObject> parkingObjects(
        @RequestParam BigDecimal latitude,
        @RequestParam BigDecimal longitude
    ) {
        var objects = databaseManager.getParkingObjects();
        if (objects == null) {
            return new ResponseEntity<>(null, HttpStatus.OK);
        }
        ParkingObject min = objects.get(0);
        var currentPos = sqrt(
            pow(abs(objects.get(0).getLatitude().doubleValue() - latitude.doubleValue()), 2) + pow(abs(
                objects.get(0).getLongitude().doubleValue() - longitude.doubleValue()), 2));
        for (var object : objects) {
            if (currentPos > sqrt(
                pow(abs(object.getLatitude().doubleValue() - latitude.doubleValue()), 2) + pow(abs(
                    object.getLongitude().doubleValue() - longitude.doubleValue()), 2))) {
                min = object;
                currentPos = sqrt(
                    pow(abs(object.getLatitude().doubleValue() - latitude.doubleValue()), 2) + pow(abs(
                        object.getLongitude().doubleValue() - longitude.doubleValue()), 2));
            }
        }
        return new ResponseEntity<>(min, HttpStatus.OK);
    }

}
