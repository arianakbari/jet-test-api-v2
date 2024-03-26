package test.jet.game.domain.services;

public interface ITokenService {
    String sign(String email);
    String extractEmail(String token);

    Boolean isTokenValid(String token, String id);
}
