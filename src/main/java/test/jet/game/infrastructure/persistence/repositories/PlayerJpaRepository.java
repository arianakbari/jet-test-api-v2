package test.jet.game.infrastructure.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import test.jet.game.infrastructure.persistence.entities.Player;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PlayerJpaRepository extends JpaRepository<Player, UUID> {

    Optional<Player> findByEmail(String email);
}
