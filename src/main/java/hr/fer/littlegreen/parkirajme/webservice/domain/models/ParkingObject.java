package hr.fer.littlegreen.parkirajme.webservice.domain.models;

import org.springframework.lang.NonNull;

import java.math.BigDecimal;
import java.util.Objects;

public final class ParkingObject {

    @NonNull
    private final String id;

    @NonNull
    private final String companyId;

    private final int freeSlots;

    private final int price;

    @NonNull
    private final String address;

    @NonNull
    private final String name;

    @NonNull
    private final BigDecimal latitude;

    @NonNull
    private final BigDecimal longitude;

    public ParkingObject(
        @NonNull String id,
        @NonNull String companyId,
        int freeSlots,
        int price,
        @NonNull String address,
        @NonNull String name,
        @NonNull BigDecimal latitude,
        @NonNull BigDecimal longitude
    ) {
        this.id = id;
        this.companyId = companyId;
        this.freeSlots = freeSlots;
        this.price = price;
        this.address = address;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @NonNull
    public String getId() {
        return id;
    }

    @NonNull
    public String getCompanyId() {
        return companyId;
    }

    public int getFreeSlots() {
        return freeSlots;
    }

    public int getPrice() {
        return price;
    }

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
        return freeSlots == that.freeSlots &&
            price == that.price &&
            id.equals(that.id) &&
            companyId.equals(that.companyId) &&
            address.equals(that.address) &&
            name.equals(that.name) &&
            latitude.equals(that.latitude) &&
            longitude.equals(that.longitude);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, companyId, freeSlots, price, address, name, latitude, longitude);
    }

}
