package test.jet.game.domain.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import test.jet.game.application.dtos.GameResponseDto;
import test.jet.game.domain.models.value_objects.PlayerId;

@Data
@AllArgsConstructor
public class GameEvent {
    public static String JOINED = "JOINED";
    public static String FINISHED = "FINISHED";
    public static String TURN_PLAYED = "TURN_PLAYED";

    private GameResponseDto game;

    private PlayerId playerId;

    private String type;
}
