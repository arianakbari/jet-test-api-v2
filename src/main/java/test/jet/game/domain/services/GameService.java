package test.jet.game.domain.services;

import lombok.AllArgsConstructor;
import org.jetbrains.annotations.Nullable;
import test.jet.game.domain.events.GameEvent;
import test.jet.game.domain.events.IEventPublisher;
import test.jet.game.domain.exceptions.GameNotFoundException;
import test.jet.game.domain.factories.IGameFactory;
import test.jet.game.domain.models.entities.Game;
import test.jet.game.domain.models.entities.Game.GAME_MODE;
import test.jet.game.domain.models.entities.Player.PLAYER_INPUT_TYPE;
import test.jet.game.domain.models.value_objects.GameId;
import test.jet.game.domain.models.value_objects.PlayerEmail;
import test.jet.game.domain.models.value_objects.PlayerId;
import test.jet.game.domain.repositories.IGameRepository;

import java.util.Optional;

@AllArgsConstructor
public class GameService implements IGameService {

    private IGameRepository repository;
    private IGameFactory gameFactory;
    private IEventPublisher eventPublisher;

    @Override
    public Game joinGame(String email, GAME_MODE mode, PLAYER_INPUT_TYPE inputType) {
        // Check if player has an in progress game or not
        Optional<Game> existingGame = this.repository.getPlayerActiveGame(new PlayerEmail(email));
        // return game if exists
        if (existingGame.isPresent()) return existingGame.get();
        // if game mode is PLAYER_VS_COMPUTER
        if (mode == GAME_MODE.PLAYER_VS_COMPUTER) {
            // create new game
            return gameFactory.createGame(email, mode, inputType);
        }
        // else check for available games to join
        Optional<Game> gameOptional = repository.getJoinableGame();
        if (gameOptional.isPresent()) {
            // join if a joinable game exists
            Game game = gameOptional.get();
            game.startGame(email, inputType);
            game = repository.save(game, false);
            // notify other player that another player joined
            eventPublisher.publish(new GameEvent(game, game.getOtherPlayersId(game.getCurrentTurnPlayer().getId()), GameEvent.JOINED));
            // return game
            return game;
        }
        // else create new one
        return gameFactory.createGame(email, mode, inputType);
    }

    @Override
    public Game makeMove(GameId gameId, PlayerId playerId, @Nullable Integer choice) {
        // get the game
        var gameOptional = repository.findGameById(gameId);
        if (gameOptional.isEmpty()) {
            throw new GameNotFoundException(gameId);
        }
        var game = gameOptional.get();
        // check if this player belongs to this game
        game.checkPlayerBelongsToThisGame(playerId);
        // check if player's turn to play
        game.checkForPlayerTurn(playerId);
        // let player plays his turn
        game.playTurn(playerId, choice);
        // check if the game is finished or not
        if (game.canBeFinished()) {
            game.finishGame(false);
            eventPublisher.publish(new GameEvent(game, game.getOtherPlayersId(playerId), GameEvent.FINISHED));
            repository.save(game, true);
            return game;
        }

        // if opponent is computer, play its turn
        game.playComputerTurnIfApplicable();

        // check if game is finished after computers turn
        if (game.canBeFinished()) {
            game.finishGame(true);
            eventPublisher.publish(new GameEvent(game, playerId, GameEvent.FINISHED));
            repository.save(game, true);
            return game;
        }

        // swap turn
        game.swapTurn();
        // persist the game and return
        game = repository.save(game, false);
        // notify the other player
        eventPublisher.publish(new GameEvent(game, game.getOtherPlayersId(playerId), GameEvent.TURN_PLAYED));
        return game;
    }
}
