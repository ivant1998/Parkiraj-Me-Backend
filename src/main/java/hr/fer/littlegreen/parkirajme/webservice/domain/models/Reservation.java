package hr.fer.littlegreen.parkirajme.webservice.domain.models;

import org.springframework.lang.NonNull;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Objects;

public class Reservation {

    @NonNull
    private final String registrationNumber;

    @NonNull
    private final String userUuid;

    @NonNull
    private final String objectUuid;

    @NonNull
    private final Date expirationDate;

    @NonNull
    private final Timestamp startTime;

    @NonNull
    private final Timestamp endTime;

    @NonNull
    private final short daysOfWeek;


    public Reservation(
        @NonNull String registrationNumber,
        @NonNull String userUuid,
        @NonNull String objectUuid,
        @NonNull Date expirationDate,
        @NonNull Timestamp startTime,
        @NonNull Timestamp endTime,
        short daysOfWeek
    ) {
        this.registrationNumber = registrationNumber;
        this.userUuid = userUuid;
        this.objectUuid = objectUuid;
        this.expirationDate = expirationDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.daysOfWeek = daysOfWeek;
    }

    @NonNull
    public String getRegistrationNumber() {
        return registrationNumber;
    }

    @NonNull
    public String getUserUuid() {
        return userUuid;
    }

    @NonNull
    public String getObjectUuid() {
        return objectUuid;
    }

    @NonNull
    public Date getExpirationDate() {
        return expirationDate;
    }

    @NonNull
    public Timestamp getStartTime() {
        return startTime;
    }

    @NonNull
    public Timestamp getEndTime() {
        return endTime;
    }

    public short getDaysOfWeek() {
        return daysOfWeek;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        Reservation that = (Reservation) o;
        return daysOfWeek == that.daysOfWeek &&
            registrationNumber.equals(that.registrationNumber) &&
            userUuid.equals(that.userUuid) &&
            objectUuid.equals(that.objectUuid) &&
            expirationDate.equals(that.expirationDate) &&
            startTime.equals(that.startTime) &&
            endTime.equals(that.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(registrationNumber, userUuid, objectUuid, expirationDate, startTime, endTime, daysOfWeek);
    }
}
