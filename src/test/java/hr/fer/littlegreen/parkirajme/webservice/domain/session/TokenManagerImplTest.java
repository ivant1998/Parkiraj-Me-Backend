package hr.fer.littlegreen.parkirajme.webservice.domain.session;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TokenManagerImplTest {

    TokenManagerImpl tokenManager;

    @BeforeEach
    void setUp() {
        tokenManager = new TokenManagerImpl(new HashMap<>(), new HashMap<>(), new SecureRandom(), Base64.getEncoder());
    }

    @Test
    void validTokenTest() {
        var token = tokenManager.generateToken("id");
        assertTrue(tokenManager.tokenValid(token));
    }

    @Test
    void invalidTokenTest() {
        assertFalse(tokenManager.tokenValid("token"));
    }

    @Test
    void getIdSuccessTest() {
        var token = tokenManager.generateToken("id");
        assertEquals("id", tokenManager.getId(token));
    }

    @Test
    void getIdFailureTest() {
        assertNull(tokenManager.getId("token"));
    }
}