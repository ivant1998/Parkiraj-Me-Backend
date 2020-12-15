package hr.fer.littlegreen.parkirajme.webservice.data.database;

import hr.fer.littlegreen.parkirajme.webservice.restapi.register.company.RegisterCompanyRequestBody;
import hr.fer.littlegreen.parkirajme.webservice.restapi.register.user.RegisterUserRequestBody;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public interface DatabaseManager {

    @Nullable
    String checkLoginCredentials(@NonNull String email, @NonNull String password);

    @Nullable
    String registerUser(@NonNull RegisterUserRequestBody user);

    @Nullable
    String registerCompany(@NonNull RegisterCompanyRequestBody registerCompanyRequestBody);
}
