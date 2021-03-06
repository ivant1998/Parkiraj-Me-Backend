package hr.fer.littlegreen.parkirajme.webservice.domain.models;

import org.springframework.lang.NonNull;

import java.math.BigDecimal;
import java.util.Objects;

public final class ParkingObject {

    @NonNull
    private final String objectUuid;

    @NonNull
    private final String companyUuid;

    private final int freeSlots;

    private final int thirtyMinutePrice;

    private final int capacity;

    @NonNull
    private final String address;

    @NonNull
    private final String name;

    @NonNull
    private final BigDecimal latitude;

    @NonNull
    private final BigDecimal longitude;

    public ParkingObject(
        @NonNull String objectUuid,
        @NonNull String companyUuid,
        int thirtyMinutePrice,
        @NonNull String address,
        @NonNull String name,
        int capacity,
        @NonNull BigDecimal latitude,
        @NonNull BigDecimal longitude,
        int freeSlots
    ) {
        this.objectUuid = objectUuid;
        this.companyUuid = companyUuid;
        this.thirtyMinutePrice = thirtyMinutePrice;
        this.address = address;
        this.name = name;
        this.capacity = capacity;
        this.latitude = latitude;
        this.longitude = longitude;
        this.freeSlots = freeSlots;
    }

    @NonNull
    public String getObjectUuid() {
        return objectUuid;
    }

    @NonNull
    public String getCompanyUuid() {
        return companyUuid;
    }

    public int getFreeSlots() {
        return freeSlots;
    }

    public int getThirtyMinutePrice() {
        return thirtyMinutePrice;
    }

    public int getCapacity() {return capacity;}

    @NonNull
    public String getAddress() {
        return address;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    public BigDecimal getLatitude() {
        return latitude;
    }

    @NonNull
    public BigDecimal getLongitude() {
        return longitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        ParkingObject that = (ParkingObject) o;
        return objectUuid.equals(that.objectUuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(objectUuid);
    }

}
