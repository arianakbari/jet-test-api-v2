package test.jet.game.infrastructure.persistence.repositories;


import com.github.javafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import test.jet.game.domain.models.value_objects.GameId;
import test.jet.game.domain.models.value_objects.PlayerEmail;
import test.jet.game.infrastructure.persistence.Mapper;
import test.jet.game.infrastructure.persistence.entities.Game;
import test.jet.game.infrastructure.persistence.entities.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class GameRepositoryTest {
    @Autowired
    private GameJpaRepository gameJpaRepository;

    @Autowired
    private PlayerJpaRepository playerJpaRepository;

    @Autowired
    private GameRepository gameRepository;


    private final Faker faker = new Faker();

    private Game createGame(test.jet.game.domain.models.entities.Game.GAME_MODE mode, test.jet.game.domain.models.entities.Game.GAME_STATUS status) {
        return gameJpaRepository.save(
                Game.builder()
                        .number(faker.number().numberBetween(1, 100))
                        .mode(mode)
                        .status(status)
                        .build()
        );
    }

    @AfterEach
    public void cleanUp() {
        playerJpaRepository.deleteAll();
        gameJpaRepository.deleteAll();
    }

    @Test
    public void givenPlayerEmail_whenGetPlayerActiveGame_thenOptionalActiveGame() {
        var game = createGame(test.jet.game.domain.models.entities.Game.GAME_MODE.PLAYER_VS_PLAYER, test.jet.game.domain.models.entities.Game.GAME_STATUS.IN_PROGRESS);

        var player = playerJpaRepository.save(Player.builder().email(faker.internet().emailAddress()).activeGame(game).build());

        var result = gameRepository.getPlayerActiveGame(new PlayerEmail(player.getEmail()));

        Assertions.assertEquals(result.map(g -> g.getId().getValue()), Optional.of(game.getId().toString()));
    }

    @Test
    public void givenPlayerEmail_whenGetPlayerActiveGame_thenOptionalEmpty() {
        var player = playerJpaRepository.save(Player.builder().email(faker.internet().emailAddress()).build());

        var result = gameRepository.getPlayerActiveGame(new PlayerEmail(player.getEmail()));

        Assertions.assertEquals(result, Optional.empty());
    }

    @Test
    public void givenGameId_whenFindGameById_thenOptionalGame() {
        var game = createGame(test.jet.game.domain.models.entities.Game.GAME_MODE.PLAYER_VS_PLAYER, test.jet.game.domain.models.entities.Game.GAME_STATUS.IN_PROGRESS);

        var result = gameRepository.findGameById(new GameId(game.getId().toString()));

        Assertions.assertEquals(result.map(g -> g.getId().getValue()), Optional.of(game.getId().toString()));
    }

    @Test
    public void given_whenGetJoinableGame_thenOptionalGame() {
        var game = createGame(test.jet.game.domain.models.entities.Game.GAME_MODE.PLAYER_VS_PLAYER, test.jet.game.domain.models.entities.Game.GAME_STATUS.WAITING_FOR_PLAYER);

        var result = gameRepository.getJoinableGame();

        Assertions.assertEquals(result.map(g -> g.getId().getValue()), Optional.of(game.getId().toString()));
    }

    @Test
    public void given_whenGetJoinableGame_thenOptionalEmpty() {
        createGame(test.jet.game.domain.models.entities.Game.GAME_MODE.PLAYER_VS_COMPUTER, test.jet.game.domain.models.entities.Game.GAME_STATUS.WAITING_FOR_PLAYER);

        var result = gameRepository.getJoinableGame();

        Assertions.assertEquals(result, Optional.empty());
    }

    @Test
    public void givenGameAndPlayer_whenCreateGame_thenCreatedGame() {
        var player = test.jet.game.domain.models.entities.Player.builder()
                .email(new PlayerEmail(faker.internet().emailAddress()))
                .inputType(test.jet.game.domain.models.entities.Player.PLAYER_INPUT_TYPE.MANUAL)
                .build();

        var game = new test.jet.game.domain.models.entities.Game();
        game.setMode(test.jet.game.domain.models.entities.Game.GAME_MODE.PLAYER_VS_PLAYER);
        game.setInitialStatus();
        game.setInitialNumber();
        game.addPlayer(player);
        game.setInitialCurrentTurnPlayer(player);

        var result = gameRepository.createGame(game, player);

        Assertions.assertNotNull(result.getId().getValue());
        Assertions.assertEquals(result.getPlayers().size(), 1);
        Assertions.assertNotNull(result.getPlayers().stream().findFirst().get().getId().getValue());
    }

    @Test
    public void givenGame_whenSave_thenSavedGame() {
        var game = test.jet.game.domain.models.entities.Game
                .builder()
                .number(faker.number().numberBetween(1, 100))
                .mode(test.jet.game.domain.models.entities.Game.GAME_MODE.PLAYER_VS_PLAYER)
                .status(test.jet.game.domain.models.entities.Game.GAME_STATUS.IN_PROGRESS)
                .players(new ArrayList<>())
                .build();

        var result = gameRepository.save(game, false);

        Assertions.assertEquals(result.getNumber(), game.getNumber());
        Assertions.assertEquals(result.getMode(), game.getMode());
        Assertions.assertEquals(result.getStatus(), game.getStatus());
        Assertions.assertNotNull(result.getId().getValue());
    }

    @Test
    public void givenGameWithReset_whenSave_thenSavedResetGame() {
        var game = test.jet.game.domain.models.entities.Game
                .builder()
                .number(faker.number().numberBetween(1, 100))
                .mode(test.jet.game.domain.models.entities.Game.GAME_MODE.PLAYER_VS_PLAYER)
                .status(test.jet.game.domain.models.entities.Game.GAME_STATUS.IN_PROGRESS)
                .players(
                        List.of(
                                test.jet.game.domain.models.entities.Player.builder()
                                .email(new PlayerEmail(faker.internet().emailAddress()))
                                .inputType(test.jet.game.domain.models.entities.Player.PLAYER_INPUT_TYPE.MANUAL)
                                .build()
                        )
                )
                .build();

        var result = gameRepository.save(game, true);

        Assertions.assertNotNull(result.getId().getValue());
        Assertions.assertEquals(result.getPlayers().size(), 0);
    }
}
