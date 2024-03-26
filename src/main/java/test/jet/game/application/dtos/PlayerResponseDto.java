package test.jet.game.application.dtos;

import lombok.Builder;
import lombok.Data;
import test.jet.game.domain.models.entities.Player;

import java.util.UUID;

@Data
@Builder
public class PlayerResponseDto {
    private String id;
    private String email;
    private Player.PLAYER_INPUT_TYPE inputType;
}
