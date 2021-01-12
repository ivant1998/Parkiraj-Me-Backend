package hr.fer.littlegreen.parkirajme.webservice.restapi.get.getparkingobjects;

import hr.fer.littlegreen.parkirajme.webservice.domain.models.ParkingObject;
import hr.fer.littlegreen.parkirajme.webservice.restapi.Response;
import org.springframework.lang.Nullable;

import java.util.Objects;

public final class GetParkingObjectResponse extends Response {

    @Nullable
    private final ParkingObject parkingObject;

    public GetParkingObjectResponse(
        String error,
        @Nullable ParkingObject parkingObject
    ) {
        super(error);
        this.parkingObject = parkingObject;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        GetParkingObjectResponse that = (GetParkingObjectResponse) o;
        return Objects.equals(parkingObject, that.parkingObject);
    }

    @Override
    public int hashCode() {
        return Objects.hash(parkingObject);
    }

    @Nullable
    public ParkingObject getParkingObject() {
        return parkingObject;
    }
}
