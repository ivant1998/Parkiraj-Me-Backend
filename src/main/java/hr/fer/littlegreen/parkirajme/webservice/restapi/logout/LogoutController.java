package hr.fer.littlegreen.parkirajme.webservice.restapi.logout;

import hr.fer.littlegreen.parkirajme.webservice.domain.session.TokenManager;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LogoutController {
    @NonNull
    TokenManager tokenManager;

    public LogoutController(
        @NonNull TokenManager tokenManager
    ) {
        this.tokenManager = tokenManager;
    }

    @PostMapping("/logout")
    public HttpStatus addParking(@RequestHeader("Authentication-Token") String token) {
        tokenManager.removeByToken(token);
        return HttpStatus.NO_CONTENT;
    }

}
