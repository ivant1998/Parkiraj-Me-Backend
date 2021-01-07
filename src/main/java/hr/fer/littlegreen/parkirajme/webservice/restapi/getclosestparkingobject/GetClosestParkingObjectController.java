package hr.fer.littlegreen.parkirajme.webservice.restapi.getclosestparkingobject;

import hr.fer.littlegreen.parkirajme.webservice.data.database.DatabaseManager;
import hr.fer.littlegreen.parkirajme.webservice.domain.models.ParkingObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import static java.lang.Math.toRadians;

@RestController
public class GetClosestParkingObjectController {

    @NonNull
    private final DatabaseManager databaseManager;

    public GetClosestParkingObjectController(
        @NonNull DatabaseManager databaseManager
    ) {
        this.databaseManager = databaseManager;
    }

    @GetMapping("/closestParkingObject")
    public ResponseEntity<GetClosestParkingObjectResponse> closestParkingObject(
        @RequestParam("lat") BigDecimal latitude,
        @RequestParam("long") BigDecimal longitude
    ) {
        var objects = databaseManager.getParkingObjects();
        if (objects == null) {
            return new ResponseEntity<>(null, HttpStatus.OK);
        }
        ParkingObject min = objects.get(0);
        var curr = getDistanceFromLatLonInKm(latitude, longitude, min);
        for (ParkingObject object : objects) {
            if (curr > getDistanceFromLatLonInKm(latitude, longitude, object)) {
                min = object;
                curr = getDistanceFromLatLonInKm(latitude, longitude, object);
            }
        }
        return new ResponseEntity<>(new GetClosestParkingObjectResponse(min), HttpStatus.OK);
    }

    private double getDistanceFromLatLonInKm(BigDecimal latitude, BigDecimal longitude, ParkingObject object) {
        var R = 6731;
        var dLat = toRadians(object.getLatitude().doubleValue() - latitude.doubleValue());
        var dLong = toRadians(object.getLongitude().doubleValue() - longitude.doubleValue());
        var a = sin(dLat / 2) * sin(dLat / 2) + cos(toRadians(latitude.doubleValue())) *
            cos(toRadians(object.getLatitude().doubleValue())) * sin(dLong / 2) * sin(dLat / 2);
        var c = 2 * atan2(sqrt(a), sqrt(1 - a));
        return R * c;
    }
}
