package test.jet.game.domain.services;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import test.jet.game.domain.events.IEventPublisher;
import test.jet.game.domain.factories.IGameFactory;
import test.jet.game.domain.models.entities.Game;
import test.jet.game.domain.models.entities.Player;
import test.jet.game.domain.models.value_objects.GameId;
import test.jet.game.domain.models.value_objects.PlayerEmail;
import test.jet.game.domain.models.value_objects.PlayerId;
import test.jet.game.domain.repositories.IGameRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class GameServiceTest {

    private final Faker faker = new Faker();

    @Mock
    private IGameRepository repository;
    @Mock
    private IGameFactory gameFactory;
    @Mock
    private IEventPublisher eventPublisher;

    @InjectMocks
    private GameService gameService;

    @Test
    public void givenEmail_whenJoinGame_thenOptionalExistingGame() {
        var player = Player
                .builder()
                .id(new PlayerId(faker.internet().uuid()))
                .email(new PlayerEmail(faker.internet().emailAddress()))
                .inputType(Player.PLAYER_INPUT_TYPE.AUTOMATIC)
                .build();

        var game = Game
                .builder()
                .id(new GameId(faker.internet().uuid()))
                .mode(Game.GAME_MODE.PLAYER_VS_COMPUTER)
                .currentTurnPlayer(player)
                .number(50)
                .players(List.of(player))
                .build();

        given(repository.getPlayerActiveGame(any())).willReturn(Optional.of(game));

        var result = gameService.joinGame(player.getEmail().getValue(), null, null);

        Assertions.assertEquals(result, game);

    }

    @Test
    public void givenEmailAndModeAndInputType_whenJoinGame_thenCreatedGame() {
        var player = Player
                .builder()
                .id(new PlayerId(faker.internet().uuid()))
                .email(new PlayerEmail(faker.internet().emailAddress()))
                .inputType(Player.PLAYER_INPUT_TYPE.AUTOMATIC)
                .build();

        var game = Game
                .builder()
                .id(new GameId(faker.internet().uuid()))
                .mode(Game.GAME_MODE.PLAYER_VS_COMPUTER)
                .currentTurnPlayer(player)
                .number(50)
                .players(List.of(player))
                .build();

        given(repository.getPlayerActiveGame(any())).willReturn(Optional.empty());
        given(gameFactory.createGame(any(), any(), any())).willReturn(game);

        var result = gameService.joinGame(player.getEmail().getValue(), Game.GAME_MODE.PLAYER_VS_COMPUTER, null);

        Assertions.assertEquals(result, game);

    }

    @Test
    public void givenEmailAndModeAndInputType_whenJoinGame_thenJoinableGame() {
        var player = Player
                .builder()
                .id(new PlayerId(faker.internet().uuid()))
                .email(new PlayerEmail(faker.internet().emailAddress()))
                .inputType(Player.PLAYER_INPUT_TYPE.AUTOMATIC)
                .build();

        var game = Game
                .builder()
                .id(new GameId(faker.internet().uuid()))
                .mode(Game.GAME_MODE.PLAYER_VS_COMPUTER)
                .number(50)
                .players(new ArrayList<>(List.of(player)))
                .build();

        given(repository.getPlayerActiveGame(any())).willReturn(Optional.empty());
        given(repository.getJoinableGame()).willReturn(Optional.of(game));
        given(repository.save(any(), any())).willReturn(game);
        willDoNothing().given(eventPublisher).publish(any());

        var result = gameService.joinGame(player.getEmail().getValue(), Game.GAME_MODE.PLAYER_VS_PLAYER, Player.PLAYER_INPUT_TYPE.AUTOMATIC);


        Assertions.assertEquals(result.getStatus(), Game.GAME_STATUS.IN_PROGRESS);
        Assertions.assertEquals(result.getPlayers().size(), 2);

    }

    @Test
    public void givenEmailAndVsPlayerModeAndInputType_whenJoinGame_thenCreatedGame() {
        var player = Player
                .builder()
                .id(new PlayerId(faker.internet().uuid()))
                .email(new PlayerEmail(faker.internet().emailAddress()))
                .inputType(Player.PLAYER_INPUT_TYPE.AUTOMATIC)
                .build();

        var game = Game
                .builder()
                .id(new GameId(faker.internet().uuid()))
                .mode(Game.GAME_MODE.PLAYER_VS_COMPUTER)
                .number(50)
                .players(new ArrayList<>(List.of(player)))
                .build();

        given(repository.getPlayerActiveGame(any())).willReturn(Optional.empty());
        given(repository.getJoinableGame()).willReturn(Optional.empty());
        given(gameFactory.createGame(any(), any(), any())).willReturn(game);

        var result = gameService.joinGame(player.getEmail().getValue(), Game.GAME_MODE.PLAYER_VS_PLAYER, Player.PLAYER_INPUT_TYPE.AUTOMATIC);


        Assertions.assertEquals(result, game);

    }
}
