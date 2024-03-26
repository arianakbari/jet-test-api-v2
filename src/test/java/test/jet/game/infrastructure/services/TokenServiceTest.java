package test.jet.game.infrastructure.services;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TokenServiceTest {

    private final TokenService tokenService = new TokenService();
    private final Faker faker = new Faker();

    @Test
    public void givenEmail_whenSign_thenReturnJwt() {
        var result = tokenService.sign(faker.internet().emailAddress());

        Assertions.assertNotNull(result);
        Assertions.assertNotEquals(result, "");
    }

    @Test
    public void givenToken_whenExtractEmail_thenReturnEmail() {
        var email = faker.internet().emailAddress();

        var token = tokenService.sign(email);

        var result = tokenService.extractEmail(token);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result, email);
    }

    @Test
    public void givenTokenAnd_whenIsTokenValid_thenReturnTrue() {
        var email = faker.internet().emailAddress();

        var token = tokenService.sign(email);

        var result = tokenService.isTokenValid(token, email);

        Assertions.assertTrue(result);
    }

    @Test
    public void givenTokenAnd_whenIsTokenValid_thenReturnFalse() {
        var token = tokenService.sign(faker.internet().emailAddress());

        var result = tokenService.isTokenValid(token, faker.internet().emailAddress());

        Assertions.assertFalse(result);
    }
}
