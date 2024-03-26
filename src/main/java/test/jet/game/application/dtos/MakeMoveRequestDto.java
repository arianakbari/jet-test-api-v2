package test.jet.game.application.dtos;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.UUID;

@Data
public class MakeMoveRequestDto {
    @NotNull(message = "Invalid game id!")
    private UUID gameId;

    @Min(-1)
    @Max(1)
    private Integer choice;
}
