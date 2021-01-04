package hr.fer.littlegreen.parkirajme.webservice.data.database;

import hr.fer.littlegreen.parkirajme.webservice.domain.models.ParkingObject;
import hr.fer.littlegreen.parkirajme.webservice.domain.models.User;
import hr.fer.littlegreen.parkirajme.webservice.restapi.addparkingobject.CompanyParkingObjectRequestBody;
import hr.fer.littlegreen.parkirajme.webservice.restapi.register.company.RegisterCompanyRequestBody;
import hr.fer.littlegreen.parkirajme.webservice.restapi.register.person.RegisterPersonRequestBody;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.List;

public interface DatabaseManager {

    @Nullable
    User checkLoginCredentials(@NonNull String email, @NonNull String password);

    @Nullable
    String registerPerson(@NonNull RegisterPersonRequestBody registerPersonRequestBody);

    @Nullable
    String registerCompany(@NonNull RegisterCompanyRequestBody registerCompanyRequestBody);

    @Nullable
    List<ParkingObject> getParkingObjects();

    @NonNull
    List<User> getRegisteredUsers();

    @Nullable
    String getUserRole(String userUuid);

    @Nullable
    String addParkingObject(
        @NonNull CompanyParkingObjectRequestBody companyParkingObjectRequestBody,
        @NonNull String companyUuid
    );

    @Nullable
    List<ParkingObject> getCompanyParkingObjects(@NonNull String companyId);
}
