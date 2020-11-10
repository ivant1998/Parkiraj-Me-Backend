package hr.fer.littlegreen.parkirajme.webservice.di;

import hr.fer.littlegreen.parkirajme.webservice.database.DatabaseManager;
import hr.fer.littlegreen.parkirajme.webservice.session.TokenManager;
import hr.fer.littlegreen.parkirajme.webservice.session.TokenManagerImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;

@Configuration
public class AppConfiguration {

    @Bean
    public TokenManager provideTokenManager() {
        return new TokenManagerImpl(new HashMap<>(), new SecureRandom(), Base64.getEncoder());
    }

    @Bean
    public DatabaseManager provideDatabaseManager() {
        return null;
    }
}
