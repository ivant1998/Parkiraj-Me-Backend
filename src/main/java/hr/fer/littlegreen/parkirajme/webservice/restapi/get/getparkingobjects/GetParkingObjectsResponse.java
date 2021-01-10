package hr.fer.littlegreen.parkirajme.webservice.restapi.get.getparkingobjects;

import hr.fer.littlegreen.parkirajme.webservice.domain.models.ParkingObject;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Objects;

public class GetParkingObjectsResponse {

    @NonNull
    List<ParkingObject> ParkingObjectList;

    public GetParkingObjectsResponse(@NonNull List<ParkingObject> parkingObjectList) {
        ParkingObjectList = parkingObjectList;
    }

    @NonNull
    public List<ParkingObject> getParkingObjectList() {
        return ParkingObjectList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        GetParkingObjectsResponse response = (GetParkingObjectsResponse) o;
        return ParkingObjectList.equals(response.ParkingObjectList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ParkingObjectList);
    }
}
