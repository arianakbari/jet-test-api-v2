package test.jet.game.application.dtos;

import lombok.Builder;
import lombok.Data;
import test.jet.game.domain.models.entities.Game.GAME_MODE;
import test.jet.game.domain.models.entities.Game.GAME_STATUS;

import java.util.List;

@Data
@Builder
public class GameResponseDto {
    private String id;
    private Integer number;
    private String currentTurnPlayerId;
    private String winnerId;
    private GAME_STATUS status;
    private GAME_MODE mode;
    private List<PlayerResponseDto> players;
}
