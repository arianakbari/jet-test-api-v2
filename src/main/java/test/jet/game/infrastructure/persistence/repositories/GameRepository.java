package test.jet.game.infrastructure.persistence.repositories;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import test.jet.game.domain.models.entities.Game;
import test.jet.game.domain.models.entities.Player;
import test.jet.game.domain.models.value_objects.GameId;
import test.jet.game.domain.models.value_objects.PlayerEmail;
import test.jet.game.domain.repositories.IGameRepository;
import test.jet.game.infrastructure.persistence.Mapper;
import test.jet.game.infrastructure.persistence.entities.Role;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@AllArgsConstructor
@Repository
public class GameRepository implements IGameRepository {

    private GameJpaRepository gameJpaRepository;

    private PlayerJpaRepository playerJpaRepository;

    @Override
    public Optional<Game> getPlayerActiveGame(PlayerEmail email) {
        var player = playerJpaRepository.findByEmail(email.getValue());
        if (player.isPresent() && player.get().getActiveGame() != null) return Optional.of(Mapper.toDomain(player.get().getActiveGame()));
        return Optional.empty();
    }

    @Override
    public Optional<Game> findGameById(GameId id) {
        return gameJpaRepository.findGameByIdAndStatus(UUID.fromString(id.getValue()), Game.GAME_STATUS.IN_PROGRESS).map(Mapper::toDomain);
    }

    @Override
    public Game save(Game game, Boolean resetActiveGame) {
        var gameEntity = Mapper.fromDomain(game);

        if (
                gameEntity.getPlayers().size() == 2 &&
                gameEntity.getCurrentTurnPlayer() != null &&
                gameEntity.getCurrentTurnPlayer().getId() == null
        ) {
            test.jet.game.infrastructure.persistence.entities.Player playerEntity = findOrCreatePlayer(
                    gameEntity.getCurrentTurnPlayer().getEmail(),
                    gameEntity.getCurrentTurnPlayer().getInputType()
            );

            gameEntity.setCurrentTurnPlayer(playerEntity);
            gameEntity.getPlayers().stream().filter(p -> p.getEmail().equals(playerEntity.getEmail())).forEach(p -> {
                p.setId(playerEntity.getId());
                p.setActiveGame(gameEntity);
                playerJpaRepository.save(p);
            });
        }

        if (resetActiveGame) {
            gameEntity.getPlayers().forEach(p -> {
                p.setActiveGame(null);
                p.setInputType(null);
            });
            playerJpaRepository.saveAll(gameEntity.getPlayers());
            gameEntity.setPlayers(new ArrayList<>());
        }

        return Mapper.toDomain(gameJpaRepository.save(gameEntity));
    }

    @Override
    @Transactional
    public Game createGame(Game game, Player player) {
        var gameEntity = new test.jet.game.infrastructure.persistence.entities.Game();
        gameEntity.setMode(game.getMode());
        gameEntity.setStatus(game.getStatus());
        gameEntity.setNumber(game.getNumber());
        var playerEntity = this.findOrCreatePlayer(player.getEmail().getValue(), player.getInputType());

        gameEntity.addPlayer(playerEntity);

        if (game.getCurrentTurnPlayer() != null) gameEntity.setCurrentTurnPlayer(playerEntity);

        gameEntity = gameJpaRepository.save(gameEntity);

        playerEntity.setActiveGame(gameEntity);

        return Mapper.toDomain(gameEntity);
    }

    @Override
    public Optional<Game> getJoinableGame() {
        var gameOptional = gameJpaRepository.findGameByModeAndStatus(Game.GAME_MODE.PLAYER_VS_PLAYER, Game.GAME_STATUS.WAITING_FOR_PLAYER);
        return gameOptional.map(Mapper::toDomain);
    }

    private test.jet.game.infrastructure.persistence.entities.Player findOrCreatePlayer(String email, Player.PLAYER_INPUT_TYPE inputType) {
        Optional<test.jet.game.infrastructure.persistence.entities.Player> playerOptional = playerJpaRepository.findByEmail(email);
        test.jet.game.infrastructure.persistence.entities.Player player;

        if (playerOptional.isPresent()) {
            player = playerOptional.get();
        } else {
            player = new test.jet.game.infrastructure.persistence.entities.Player();
            player.setEmail(email);
        }

        player.setInputType(inputType);
        player.setRole(Role.USER);

        return playerJpaRepository.save(player);
    }
}
