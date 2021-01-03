package hr.fer.littlegreen.parkirajme.webservice.domain.models;

import org.springframework.lang.NonNull;

public final class Company extends User {

    @NonNull
    private final String headquarterAddress;

    @NonNull
    private final String name;

    public Company(
        @NonNull String userUuid,
        @NonNull String email,
        @NonNull String role,
        @NonNull String oib,
        @NonNull String headquarterAddress,
        @NonNull String name
    ) {
        super(userUuid, email, role, oib);
        this.headquarterAddress = headquarterAddress;
        this.name = name;
    }

    @NonNull
    public String getHeadquarterAddress() {
        return headquarterAddress;
    }

    @NonNull
    public String getName() {
        return name;
    }
}
