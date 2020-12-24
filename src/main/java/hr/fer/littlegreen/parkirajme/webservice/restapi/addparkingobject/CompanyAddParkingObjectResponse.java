package hr.fer.littlegreen.parkirajme.webservice.restapi.addparkingobject;

import org.springframework.lang.NonNull;

import java.util.Objects;

public class CompanyAddParkingObjectResponse {

    @NonNull
    private final String parkingObjectId;


    public CompanyAddParkingObjectResponse(@NonNull String parkingObjectId) {this.parkingObjectId = parkingObjectId;}

    @NonNull
    public String getParkingObjectId() {
        return parkingObjectId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        CompanyAddParkingObjectResponse that = (CompanyAddParkingObjectResponse) o;
        return parkingObjectId.equals(that.parkingObjectId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(parkingObjectId);
    }

}
