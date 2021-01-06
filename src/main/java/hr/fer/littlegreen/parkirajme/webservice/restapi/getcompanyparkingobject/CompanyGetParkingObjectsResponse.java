package hr.fer.littlegreen.parkirajme.webservice.restapi.getcompanyparkingobject;

import hr.fer.littlegreen.parkirajme.webservice.domain.models.ParkingObject;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Objects;

public class CompanyGetParkingObjectsResponse {

    @NonNull
    List<ParkingObject> CompanyParkingObjectList;

    public CompanyGetParkingObjectsResponse(
        @NonNull
            List<ParkingObject> companyParkingObjectList
    ) {
        CompanyParkingObjectList = companyParkingObjectList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        CompanyGetParkingObjectsResponse that = (CompanyGetParkingObjectsResponse) o;
        return CompanyParkingObjectList.equals(that.CompanyParkingObjectList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(CompanyParkingObjectList);
    }
}
