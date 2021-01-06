package hr.fer.littlegreen.parkirajme.webservice.restapi.getclosestparkingobject;

import hr.fer.littlegreen.parkirajme.webservice.domain.models.ParkingObject;
import org.springframework.lang.NonNull;

import java.util.Objects;

public class GetClosestParkingObjectResponse {

    @NonNull
    ParkingObject parkingObject;

    public GetClosestParkingObjectResponse(
        @NonNull ParkingObject parkingObject
    ) {
        this.parkingObject = parkingObject;
    }

    @NonNull
    public ParkingObject getParkingObject() {
        return parkingObject;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        GetClosestParkingObjectResponse that = (GetClosestParkingObjectResponse) o;
        return parkingObject.equals(that.parkingObject);
    }

    @Override
    public int hashCode() {
        return Objects.hash(parkingObject);
    }
}
