package hr.fer.littlegreen.parkirajme.webservice.restapi.edit.editcompany;

import org.springframework.lang.NonNull;

import java.util.Objects;

public class EditCompanyRequestBody {

    @NonNull
    private final String name;

    @NonNull
    private final String headquarterAddress;

    public EditCompanyRequestBody(@NonNull String name, @NonNull String headquarterAddress) {
        this.name = name;
        this.headquarterAddress = headquarterAddress;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    public String getHeadquarterAddress() {
        return headquarterAddress;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        EditCompanyRequestBody that = (EditCompanyRequestBody) o;
        return name.equals(that.name) && headquarterAddress.equals(that.headquarterAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, headquarterAddress);
    }
}
