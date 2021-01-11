package hr.fer.littlegreen.parkirajme.webservice.restapi.vehicle;

import hr.fer.littlegreen.parkirajme.webservice.data.database.DatabaseManager;
import hr.fer.littlegreen.parkirajme.webservice.domain.session.TokenManager;
import hr.fer.littlegreen.parkirajme.webservice.restapi.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VehicleController {


    @NonNull
    private final DatabaseManager databaseManager;

    @NonNull
    private final TokenManager tokenManager;

    public VehicleController(@NonNull DatabaseManager databaseManager,
                             @NonNull TokenManager tokenManager) {
        this.databaseManager = databaseManager;
        this.tokenManager = tokenManager;

    }

    @DeleteMapping("/user/{userId}/vehicles/{registrationNumber}")
    public HttpStatus deleteVehicle(
        @RequestHeader("Authentication-Token") String token,
        @PathVariable String userId,
        @PathVariable String registrationNumber
    ) {
        var id = tokenManager.getId(token);
        if(id != null && id.equals(userId)) {
            databaseManager.deleteVehicle(id, registrationNumber);
            return HttpStatus.OK;
        }
        return HttpStatus.UNAUTHORIZED;

    }

    @PostMapping("/user/{userId}/vehicles/{registrationNumber}")
    public ResponseEntity<Response> addVehicle(
        @RequestHeader("Authentication-Token") String token,
        @PathVariable String userId,
        @PathVariable String registrationNumber
    ) {
        var id = tokenManager.getId(token);
        if(id != null && id.equals(userId) && databaseManager.getUserRole(id).equals("p")) {
            try {
                databaseManager.addVehicle(id, registrationNumber);
                return new ResponseEntity<>(new Response(null), HttpStatus.OK);
            } catch (IllegalArgumentException ex) {
                return new ResponseEntity<>(new Response(ex.getMessage()), HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>(new Response("Nemate ovlasti"), HttpStatus.UNAUTHORIZED);

    }
}
