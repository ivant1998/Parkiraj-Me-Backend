package hr.fer.littlegreen.parkirajme.webservice.restapi.reservations;

import org.springframework.lang.NonNull;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.BitSet;
import java.util.Objects;

public class ReservationRequestBody {

    @NonNull
    private final Timestamp startTime;

    @NonNull
    private final Timestamp endTime;

    @NonNull
    private final Date expirationDate;

    @NonNull
    private final BitSet daysOfWeek;

    @NonNull
    private final String registrationNumber;

    @NonNull
    private final String parkingId;

    public ReservationRequestBody(
        @NonNull Timestamp startTime,
        @NonNull Timestamp endTime,
        @NonNull Date expirationDate,
        @NonNull BitSet daysOfWeek,
        @NonNull String registrationNumber,
        @NonNull String parkingId
    ) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.expirationDate = expirationDate;
        this.daysOfWeek = daysOfWeek;
        this.registrationNumber = registrationNumber;
        this.parkingId = parkingId;
    }

    @NonNull
    public Timestamp getStartTime() {
        return startTime;
    }

    @NonNull
    public Timestamp getEndTime() {
        return endTime;
    }

    @NonNull
    public Date getExpirationDate() {
        return expirationDate;
    }

    public BitSet getDaysOfWeek() {
        return daysOfWeek;
    }

    @NonNull
    public String getRegistrationNumber() {
        return registrationNumber;
    }

    @NonNull
    public String getParkingId() {
        return parkingId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        ReservationRequestBody that = (ReservationRequestBody) o;
        return daysOfWeek == that.daysOfWeek &&
            startTime.equals(that.startTime) &&
            endTime.equals(that.endTime) &&
            expirationDate.equals(that.expirationDate) &&
            registrationNumber.equals(that.registrationNumber) &&
            parkingId.equals(that.parkingId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startTime, endTime, expirationDate, daysOfWeek, registrationNumber, parkingId);
    }
}
