package test.jet.game.application.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JoinGameResponseDto {


    private String token;

    private GameResponseDto game;
}
