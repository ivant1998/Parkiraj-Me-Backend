package hr.fer.littlegreen.parkirajme.webservice.data.database;

import hr.fer.littlegreen.parkirajme.webservice.domain.models.ParkingObject;
import hr.fer.littlegreen.parkirajme.webservice.domain.models.Reservation;
import hr.fer.littlegreen.parkirajme.webservice.domain.models.User;
import hr.fer.littlegreen.parkirajme.webservice.restapi.addparkingobject.CompanyAddParkingObjectRequestBody;
import hr.fer.littlegreen.parkirajme.webservice.restapi.edit.editparkingobject.EditParkingObjectRequestBody;
import hr.fer.littlegreen.parkirajme.webservice.restapi.register.company.RegisterCompanyRequestBody;
import hr.fer.littlegreen.parkirajme.webservice.restapi.register.person.RegisterPersonRequestBody;
import hr.fer.littlegreen.parkirajme.webservice.restapi.reservations.ReservationRequestBody;
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

    @Nullable
    ParkingObject getParkingObject(@NonNull String objectUuid);

    @NonNull
    List<User> getRegisteredUsers();

    @Nullable
    String getUserRole(String userUuid);

    @Nullable
    String addParkingObject(
        @NonNull CompanyAddParkingObjectRequestBody companyAddParkingObjectRequestBody,
        @NonNull String companyUuid
    );

    @Nullable
    List<ParkingObject> getCompanyParkingObjects(@NonNull String companyId);

    @Nullable
    String parkingObjectOwner(@NonNull String parkingObjectId);

    @Nullable
    void deleteParkingObject(@NonNull String parkingObjectId);

    @Nullable
    void deleteUser(@NonNull String userId);

    @Nullable
    List<Reservation> getUserParkingReservations(@NonNull String userId);

    @Nullable
    List<Reservation> getReservationsOnParking(@NonNull String companyId);

    @Nullable
    String addReservation(@NonNull ReservationRequestBody reservation, @NonNull String userId);

    @Nullable
    void deleteReservation(String reservationId, String userId);

    @Nullable
    void editPerson(
        String uuid,
        String firstName,
        String lastName,
        String creditCardNumber,
        String creditCardExpirationDate
    );

    @Nullable
    void editCompany(String uuid, String name, String headquarterAddress);

    @Nullable
    void editParkingObject(String parkingObjectId, EditParkingObjectRequestBody editParkingObjectRequestBody);

    void deleteVehicle(String ownerId, String registrationNumber);

    void addVehicle(String ownerId, String registrationNumber);
}
