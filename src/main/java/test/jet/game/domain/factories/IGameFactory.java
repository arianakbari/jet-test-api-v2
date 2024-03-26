package test.jet.game.domain.factories;

import test.jet.game.domain.models.entities.Game;
import test.jet.game.domain.models.entities.Player;

public interface IGameFactory {
    Game createGame(String email, Game.GAME_MODE mode, Player.PLAYER_INPUT_TYPE inputType);
}
