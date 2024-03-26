package test.jet.game.domain.models.entities;


import com.github.javafaker.Faker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import test.jet.game.domain.exceptions.ChoiceNotProvidedException;
import test.jet.game.domain.exceptions.NotPlayersTurnException;
import test.jet.game.domain.exceptions.PlayerDoesNotBelongToGameException;
import test.jet.game.domain.models.value_objects.GameId;
import test.jet.game.domain.models.value_objects.PlayerId;

import java.util.ArrayList;
import java.util.List;

public class GameTest {

    private final Faker faker = new Faker();

    @Test
    public void givenEmailAndInputType_whenStartGame_thenGameStarts() {
        var game = Game.builder().id(new GameId(faker.internet().uuid())).build();

        game.startGame(faker.internet().emailAddress(), Player.PLAYER_INPUT_TYPE.AUTOMATIC);

        var player = game.getPlayers().get(0);

        Assertions.assertEquals(game.getStatus(), Game.GAME_STATUS.IN_PROGRESS);
        Assertions.assertEquals(game.getPlayers().size(), 1);
        Assertions.assertEquals(game.getCurrentTurnPlayer(), player);
    }

    @Test
    public void given_whenSetInitialNumber_thenGameNumberSet() {
        var game = Game.builder().id(new GameId(faker.internet().uuid())).build();

        game.setInitialNumber();


        Assertions.assertNotNull(game.getNumber());
        Assertions.assertTrue(game.getNumber() > 0);
    }

    @Test
    public void givenPlayerVsComputerMode_whenSetInitialStatus_thenGameStatusSetInProgress() {
        var game = Game.builder().mode(Game.GAME_MODE.PLAYER_VS_COMPUTER).build();

        game.setInitialStatus();


        Assertions.assertEquals(game.getStatus(), Game.GAME_STATUS.IN_PROGRESS);
    }

    @Test
    public void givenPlayerVsPlayerMode_whenSetInitialStatus_thenGameStatusSetWaitingForPlayer() {
        var game = Game.builder().mode(Game.GAME_MODE.PLAYER_VS_PLAYER).build();

        game.setInitialStatus();


        Assertions.assertEquals(game.getStatus(), Game.GAME_STATUS.WAITING_FOR_PLAYER);
    }

    @Test
    public void givenPlayer_whenAddPlayer_thenPlayerAdded() {
        var game = Game.builder().id(new GameId(faker.internet().uuid())).build();
        var player = Player.builder().id(new PlayerId(faker.internet().uuid())).build();

        game.addPlayer(player);

        Assertions.assertEquals(game.getPlayers().size(), 1);
        Assertions.assertEquals(game.getPlayers().get(0), player);
    }

    @Test
    public void givenPlayer_whenAddPlayer_thenThrows() {
        var game = Game.builder().id(new GameId(faker.internet().uuid())).build();
        var player = Player.builder().id(new PlayerId(faker.internet().uuid())).build();

        game.addPlayer(player);
        game.addPlayer(player);

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            game.addPlayer(player);
        });
    }

    @Test
    public void givenPlayer_whenSetInitialCurrentTurnPlayer_thenSetPlayer() {
        var game = Game.builder().mode(Game.GAME_MODE.PLAYER_VS_COMPUTER).build();
        var player = Player.builder().id(new PlayerId(faker.internet().uuid())).build();

        game.setInitialCurrentTurnPlayer(player);

        Assertions.assertEquals(game.getCurrentTurnPlayer(), player);
    }

    @Test
    public void givenPlayer_whenSetInitialCurrentTurnPlayer_thenSetNull() {
        var game = Game.builder().mode(Game.GAME_MODE.PLAYER_VS_PLAYER).build();
        var player = Player.builder().id(new PlayerId(faker.internet().uuid())).build();

        game.setInitialCurrentTurnPlayer(player);

        Assertions.assertEquals(game.getCurrentTurnPlayer(), null);
    }

    @Test
    public void givenPlayerId_whenCheckPlayerBelongsToThisGame_thenThrows() {
        var game = Game.builder().id(new GameId(faker.internet().uuid())).players(new ArrayList<>()).build();
        var playerId = new PlayerId(faker.internet().uuid());

        var result = Assertions.assertThrows(PlayerDoesNotBelongToGameException.class, () -> {
            game.checkPlayerBelongsToThisGame(playerId);
        });

        Assertions.assertEquals(result.getId(), game.getId());
        Assertions.assertEquals(result.getPlayerId(), playerId);
    }

    @Test
    public void givenPlayerId_whenCheckForPlayerTurn_thenThrows() {
        var game = Game.builder().id(new GameId(faker.internet().uuid())).build();

        Assertions.assertThrows(NotPlayersTurnException.class, () -> {
            game.checkForPlayerTurn(new PlayerId(faker.internet().uuid()));
        });
    }

    @Test
    public void givenPlayerId_whenPlayTurn_thenThrows() {
        var game = Game.builder().id(new GameId(faker.internet().uuid())).players(new ArrayList<>()).build();
        var playerId = new PlayerId(faker.internet().uuid());

        var result = Assertions.assertThrows(PlayerDoesNotBelongToGameException.class, () -> {
            game.playTurn(playerId, 1);
        });

        Assertions.assertEquals(result.getId(), game.getId());
        Assertions.assertEquals(result.getPlayerId(), playerId);
    }

    @Test
    public void givenPlayerIdAndNull_whenPlayTurn_thenThrows() {
        var player = Player.builder().id(new PlayerId(faker.internet().uuid())).inputType(Player.PLAYER_INPUT_TYPE.MANUAL).build();
        var game = Game.builder().id(new GameId(faker.internet().uuid())).players(List.of(player)).build();

        var result = Assertions.assertThrows(ChoiceNotProvidedException.class, () -> {
            game.playTurn(player.getId(), null);
        });

        Assertions.assertEquals(result.getGameId(), game.getId());
    }

    @Test
    public void givenPlayerIdAndChoice_whenPlayTurn_thenNumberSet() {
        var number = faker.number().numberBetween(1, 100);
        var player = Player.builder().id(new PlayerId(faker.internet().uuid())).inputType(Player.PLAYER_INPUT_TYPE.MANUAL).build();
        var game = Game.builder().id(new GameId(faker.internet().uuid())).players(List.of(player)).number(number).build();

        game.playTurn(player.getId(), 1);

        Assertions.assertNotEquals(game.getNumber(), number);
    }

    @Test
    public void given_whenCanBeFinished_thenReturnTrue() {
        var game = Game.builder().id(new GameId(faker.internet().uuid())).number(1).build();

        var result = game.canBeFinished();

        Assertions.assertTrue(result);
    }

    @Test
    public void given_whenCanBeFinished_thenReturnFalse() {
        var game = Game.builder().id(new GameId(faker.internet().uuid())).number(10).build();

        var result = game.canBeFinished();

        Assertions.assertFalse(result);
    }

    @Test
    public void given_whenPlayComputerTurnIfApplicable_thenNumberSet() {
        var number = faker.number().numberBetween(1, 100);
        var game = Game.builder().id(new GameId(faker.internet().uuid())).mode(Game.GAME_MODE.PLAYER_VS_COMPUTER).number(number).build();

        game.playComputerTurnIfApplicable();

        Assertions.assertNotEquals(game.getNumber(), number);
    }

    @Test
    public void givenPlayerId_whenGetOtherPlayersId_thenReturnOtherPlayerId() {
        var playerOne = Player.builder().id(new PlayerId(faker.internet().uuid())).build();
        var playerTwo = Player.builder().id(new PlayerId(faker.internet().uuid())).build();
        var game = Game.builder().id(new GameId(faker.internet().uuid())).players(List.of(playerOne, playerTwo)).build();

        var result = game.getOtherPlayersId(playerOne.getId());

        Assertions.assertNotEquals(result, playerOne.getId());
        Assertions.assertEquals(result, playerTwo.getId());
    }

    @Test
    public void given_whenSwapTurn_thenSwap() {
        var playerOne = Player.builder().id(new PlayerId(faker.internet().uuid())).build();
        var playerTwo = Player.builder().id(new PlayerId(faker.internet().uuid())).build();
        var game = Game
                .builder()
                .id(new GameId(faker.internet().uuid()))
                .players(List.of(playerOne, playerTwo))
                .mode(Game.GAME_MODE.PLAYER_VS_PLAYER)
                .currentTurnPlayer(playerOne)
                .build();

        game.swapTurn();

        Assertions.assertNotEquals(game.getCurrentTurnPlayer().getId(), playerOne.getId());
        Assertions.assertEquals(game.getCurrentTurnPlayer().getId(), playerTwo.getId());
    }

    @Test
    public void givenIsWinnerComputerTrue_whenFinishGame_thenWinnerIsNull() {
        var playerOne = Player.builder().id(new PlayerId(faker.internet().uuid())).build();
        var game = Game
                .builder()
                .id(new GameId(faker.internet().uuid()))
                .mode(Game.GAME_MODE.PLAYER_VS_COMPUTER)
                .currentTurnPlayer(playerOne)
                .build();

        game.finishGame(true);

        Assertions.assertEquals(game.getStatus(), Game.GAME_STATUS.FINISHED);
        Assertions.assertEquals(game.getWinner(), null);
        Assertions.assertEquals(game.getCurrentTurnPlayer(), null);
    }

    @Test
    public void givenIsWinnerComputerFalse_whenFinishGame_thenWinnerIsPlayer() {
        var playerOne = Player.builder().id(new PlayerId(faker.internet().uuid())).build();
        var game = Game
                .builder()
                .id(new GameId(faker.internet().uuid()))
                .mode(Game.GAME_MODE.PLAYER_VS_COMPUTER)
                .currentTurnPlayer(playerOne)
                .build();

        game.finishGame(false);

        Assertions.assertEquals(game.getStatus(), Game.GAME_STATUS.FINISHED);
        Assertions.assertEquals(game.getWinner(), playerOne);
        Assertions.assertEquals(game.getCurrentTurnPlayer(), null);
    }
}
