package test.jet.game.infrastructure.factories;


import com.github.javafaker.Faker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import test.jet.game.domain.models.entities.Game;
import test.jet.game.domain.models.entities.Player;
import test.jet.game.domain.repositories.IGameRepository;

import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class GameFactoryTest {

    private final Faker faker = new Faker();

    @Mock
    private IGameRepository gameRepository;

    @InjectMocks
    private GameFactory gameFactory;

    @Test
    public void givenEmailAndModeAndInputType_whenCreateGame_thenReturnCreatedGame() {
        given(gameRepository.createGame(any(), any())).willAnswer((invocation) -> {
            return invocation.getArguments()[0];
        });

        var email = faker.internet().emailAddress();

        var result = gameFactory.createGame(email, Game.GAME_MODE.PLAYER_VS_PLAYER, Player.PLAYER_INPUT_TYPE.MANUAL);

        Assertions.assertEquals(result.getMode(), Game.GAME_MODE.PLAYER_VS_PLAYER);
        Assertions.assertEquals(result.getPlayers().size(), 1);
        Assertions.assertEquals(result.getStatus(), Game.GAME_STATUS.WAITING_FOR_PLAYER);
        Assertions.assertNull(result.getCurrentTurnPlayer());
    }

}
