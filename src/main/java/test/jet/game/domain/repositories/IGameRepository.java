package test.jet.game.domain.repositories;

import test.jet.game.domain.models.entities.Game;
import test.jet.game.domain.models.entities.Player;
import test.jet.game.domain.models.value_objects.GameId;
import test.jet.game.domain.models.value_objects.PlayerEmail;

import java.util.Optional;

public interface IGameRepository {
    Optional<Game> getPlayerActiveGame(PlayerEmail email);
    Optional<Game> findGameById(GameId id);
    Game save(Game game, Boolean resetActiveGame);

    Game createGame(Game game, Player player);

    Optional<Game> getJoinableGame();
}
