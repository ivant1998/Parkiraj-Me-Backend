package hr.fer.littlegreen.parkirajme.webservice.restapi.reservations;

import org.springframework.lang.NonNull;

import java.sql.Timestamp;
import java.util.Objects;

public class ReservationRequestBody {

    @NonNull
    private final Timestamp startTime;

    @NonNull
    private final Timestamp endTime;

    @NonNull
    private final String daysOfWeek;

    @NonNull
    private final String parkingId;

    public ReservationRequestBody(
        @NonNull String startTime,
        @NonNull String endTime,
        @NonNull String daysOfWeek,
        @NonNull String parkingId
    ) {
        this.startTime = Timestamp.valueOf(startTime);
        this.endTime = Timestamp.valueOf(endTime);
        this.daysOfWeek = daysOfWeek;
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
    public String getDaysOfWeek() {
        return daysOfWeek;
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
            parkingId.equals(that.parkingId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startTime, endTime, daysOfWeek, parkingId);
    }
}
