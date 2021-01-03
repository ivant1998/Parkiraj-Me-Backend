package hr.fer.littlegreen.parkirajme.webservice.restapi.addparkingobject;

import org.springframework.lang.NonNull;

import java.math.BigDecimal;
import java.util.Objects;


public class CompanyParkingObjectRequestBody {

    @NonNull
    private final int free_slots;

    @NonNull
    private final int price;

    @NonNull
    private final String address;

    @NonNull
    private final String name;

    @NonNull
    private final int capacity;

    @NonNull
    private final BigDecimal latitude;

    @NonNull
    private final BigDecimal longitude;

    public CompanyParkingObjectRequestBody(
        int free_slots,
        int price,
        @NonNull String address,
        @NonNull String name,
        int capacity,
        @NonNull BigDecimal latitude,
        @NonNull BigDecimal longitude
    ) {
        this.free_slots = free_slots;
        this.price = price;
        this.address = address;
        this.name = name;
        this.capacity = capacity;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getFree_slots() {
        return free_slots;
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

    public int getCapacity() {
        return capacity;
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
        CompanyParkingObjectRequestBody that = (CompanyParkingObjectRequestBody) o;
        return free_slots == that.free_slots && price == that.price && capacity == that.capacity
            && address.equals(that.address) && name.equals(that.name) && latitude.equals(that.latitude)
            && longitude.equals(
            that.longitude);
    }

    @Override
    public int hashCode() {
        return Objects.hash(free_slots, price, address, name, capacity, latitude, longitude);
    }
}
