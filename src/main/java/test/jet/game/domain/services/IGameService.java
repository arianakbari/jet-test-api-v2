package test.jet.game.domain.services;

import test.jet.game.domain.models.entities.Game;
import test.jet.game.domain.models.entities.Player.PLAYER_INPUT_TYPE;
import test.jet.game.domain.models.entities.Game.GAME_MODE;
import test.jet.game.domain.models.value_objects.GameId;
import test.jet.game.domain.models.value_objects.PlayerId;
import org.jetbrains.annotations.Nullable;

public interface IGameService {
    Game joinGame(String email, @Nullable GAME_MODE mode, @Nullable PLAYER_INPUT_TYPE inputType);
    Game makeMove(GameId gameId, PlayerId playerId, @Nullable Integer choice);
}
