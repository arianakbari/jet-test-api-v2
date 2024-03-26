package test.jet.game.application.dtos;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class PlayerResponseDto {
    private String id;
    private String email;
}
