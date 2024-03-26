package test.jet.game.infrastructure.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import test.jet.game.infrastructure.persistence.entities.Game;
import test.jet.game.domain.models.entities.Game.*;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface GameJpaRepository extends JpaRepository<Game, UUID> {
    Optional<Game> findGameByModeAndStatus(GAME_MODE mode, GAME_STATUS status);

    Optional<Game> findGameByIdAndStatus(UUID id, GAME_STATUS status);
}
