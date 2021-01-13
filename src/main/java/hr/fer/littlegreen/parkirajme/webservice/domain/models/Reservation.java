package hr.fer.littlegreen.parkirajme.webservice.domain.models;

import org.springframework.lang.NonNull;

import java.sql.Timestamp;
import java.util.Objects;

public class Reservation {

    @NonNull
    private final String reservationId;

    @NonNull
    private final String userUuid;

    @NonNull
    private final String objectUuid;

    @NonNull
    private final Timestamp startTime;

    @NonNull
    private final Timestamp endTime;

    @NonNull
    private final String daysOfWeek;


    public Reservation(
        @NonNull String reservationId,
        @NonNull String userUuid,
        @NonNull String objectUuid,
        @NonNull Timestamp startTime,
        @NonNull Timestamp endTime,
        @NonNull String daysOfWeek
    ) {
        this.reservationId = reservationId;
        this.userUuid = userUuid;
        this.objectUuid = objectUuid;
        this.startTime = startTime;
        this.endTime = endTime;
        this.daysOfWeek = daysOfWeek;
    }

    @NonNull
    public String getReservationId() {
        return reservationId;
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
    public Timestamp getStartTime() {
        return startTime;
    }

    @NonNull
    public Timestamp getEndTime() {
        return endTime;
    }

    public String getDaysOfWeek() {
        return daysOfWeek;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        Reservation that = (Reservation) o;
        return daysOfWeek == that.daysOfWeek &&
            userUuid.equals(that.userUuid) &&
            objectUuid.equals(that.objectUuid) &&
            startTime.equals(that.startTime) &&
            endTime.equals(that.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userUuid, objectUuid, startTime, endTime, daysOfWeek);
    }
}
