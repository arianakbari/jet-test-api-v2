package test.jet.game.application.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import test.jet.game.domain.models.entities.Game.GAME_MODE;
import test.jet.game.domain.models.entities.Player.PLAYER_INPUT_TYPE;

@Data
@Builder
public class JoinGameRequestDto {

    @Email(message = "Invalid email!")
    @NotBlank(message = "Invalid email!")
    @NotNull(message = "Invalid email!")
    private String email;

    private GAME_MODE mode;

    private PLAYER_INPUT_TYPE inputType;
}
