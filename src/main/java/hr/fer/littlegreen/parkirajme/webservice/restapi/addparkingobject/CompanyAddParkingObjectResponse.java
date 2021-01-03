package hr.fer.littlegreen.parkirajme.webservice.restapi.addparkingobject;

import hr.fer.littlegreen.parkirajme.webservice.restapi.Response;
import org.springframework.lang.NonNull;

import java.util.Objects;

public class CompanyAddParkingObjectResponse extends Response {

    @NonNull
    private final String parkingObjectId;


    public CompanyAddParkingObjectResponse(@NonNull String parkingObjectId, @NonNull String error) {
        super(error);
        this.parkingObjectId = parkingObjectId;
    }

    @NonNull
    public String getParkingObjectId() {
        return parkingObjectId;
    }

    @NonNull
    public String getError() { return error; }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        CompanyAddParkingObjectResponse that = (CompanyAddParkingObjectResponse) o;
        return parkingObjectId.equals(that.parkingObjectId) && error.equals(that.error);
    }

    @Override
    public int hashCode() {
        return Objects.hash(parkingObjectId, error);
    }

}
