package test.jet.game.infrastructure.factories;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import test.jet.game.domain.factories.IGameFactory;
import test.jet.game.domain.models.entities.Game;
import test.jet.game.domain.models.entities.Player;
import test.jet.game.domain.models.value_objects.PlayerEmail;
import test.jet.game.domain.repositories.IGameRepository;

@Service
@AllArgsConstructor
public class GameFactory implements IGameFactory {

    private IGameRepository gameRepository;
    @Override
    public Game createGame(String email, Game.GAME_MODE mode, Player.PLAYER_INPUT_TYPE inputType) {
        var player = Player.builder()
                .email(new PlayerEmail(email))
                .inputType(inputType)
                .build();

        var game = new Game();
        game.setMode(mode);
        game.setInitialStatus();
        game.setInitialNumber();
        game.addPlayer(player);
        game.setInitialCurrentTurnPlayer(player);
        return gameRepository.createGame(game, player);
    }
}
