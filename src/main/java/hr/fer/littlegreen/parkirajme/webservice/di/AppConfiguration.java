package hr.fer.littlegreen.parkirajme.webservice.di;

import hr.fer.littlegreen.parkirajme.webservice.data.database.DatabaseManager;
import hr.fer.littlegreen.parkirajme.webservice.data.database.DatabaseManagerImpl;
import hr.fer.littlegreen.parkirajme.webservice.domain.session.TokenManager;
import hr.fer.littlegreen.parkirajme.webservice.domain.session.TokenManagerImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Base64;
import java.util.HashMap;

@Configuration
public class AppConfiguration {

    @Bean
    public TokenManager provideTokenManager() {
        return new TokenManagerImpl(new HashMap<>(), new HashMap<>(), new SecureRandom(), Base64.getEncoder());
    }

    @Bean
    public DatabaseManager provideDatabaseManager() {
        return new DatabaseManagerImpl(providePasswordEncoder(), provideDatabaseConnection());
    }

    @Bean
    public Connection provideDatabaseConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(
                "jdbc:postgresql://ec2-54-216-202-161.eu-west-1.compute.amazonaws.com:5432/deg1t0cmdemhn7",
                "icfjttdivtiins",
                "447e4ba24d1cc40dff940de459899fbe24bf4c5999da88ff23508aafe16138dc"
            );
            System.out.println("Connected to the PostgreSQL server successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    @Bean
    public PasswordEncoder providePasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
