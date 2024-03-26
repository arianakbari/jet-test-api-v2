package test.jet.game.infrastructure.persistence.entities;

import jakarta.persistence.*;
import lombok.*;
import test.jet.game.domain.models.entities.Game.GAME_MODE;
import test.jet.game.domain.models.entities.Game.GAME_STATUS;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
@EqualsAndHashCode
@Entity
@Table(name = "games")
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private Integer number;

    @Column(nullable = false)
    private GAME_STATUS status;

    @Column(nullable = false)
    private GAME_MODE mode;

    @OneToOne
    @JoinColumn(name = "currentTurnPlayerId", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_game_current_player"))
    private Player currentTurnPlayer;

    @OneToOne
    @JoinColumn(name = "winnerId", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_game_winner"))
    private Player winner;

    @OneToMany(mappedBy = "activeGame", fetch = FetchType.EAGER)
    private List<Player> players = new ArrayList<>();

    public void addPlayer(Player player) {
        if (players == null) {
            players = new ArrayList<>();
        }

        players.add(player);
    }
}
