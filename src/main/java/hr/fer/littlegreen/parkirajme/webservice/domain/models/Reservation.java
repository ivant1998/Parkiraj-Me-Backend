package hr.fer.littlegreen.parkirajme.webservice.domain.models;

import org.springframework.lang.NonNull;

import java.sql.Date;
import java.sql.Timestamp;

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
    private final int daysOfWeek;


    public Reservation(
        @NonNull String registrationNumber,
        @NonNull String userUuid,
        @NonNull String objectUuid,
        @NonNull Date expirationDate,
        @NonNull Timestamp startTime,
        @NonNull Timestamp endTime,
        int daysOfWeek
    ) {
        this.registrationNumber = registrationNumber;
        this.userUuid = userUuid;
        this.objectUuid = objectUuid;
        this.expirationDate = expirationDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.daysOfWeek = daysOfWeek;
    }
}
