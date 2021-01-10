package hr.fer.littlegreen.parkirajme.webservice.restapi.edit.editparkingobject;

import org.springframework.lang.NonNull;

import java.util.Objects;

public class EditParkingObjectRequestBody {

    @NonNull
    private final int capacity;

    @NonNull
    private final int price;

    @NonNull
    private final int freeSlots;

    public EditParkingObjectRequestBody(
        @NonNull int capacity,
        @NonNull int price,
        @NonNull int freeSlots
    ) {
        this.capacity = capacity;
        this.price = price;
        this.freeSlots = freeSlots;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getPrice() {
        return price;
    }

    public int getFreeSlots() { return freeSlots; }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (!(o instanceof EditParkingObjectRequestBody)) { return false; }
        EditParkingObjectRequestBody that = (EditParkingObjectRequestBody) o;
        return getCapacity() == that.getCapacity() &&
            getPrice() == that.getPrice() &&
            getFreeSlots() == that.getFreeSlots();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCapacity(), getPrice(), getFreeSlots());
    }
}
