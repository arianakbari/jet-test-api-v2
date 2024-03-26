package test.jet.game.domain.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import test.jet.game.domain.models.value_objects.GameId;
import test.jet.game.domain.models.value_objects.PlayerId;

@Getter
@AllArgsConstructor
public class PlayerDoesNotBelongToGameException extends RuntimeException {

    private GameId id;
    private PlayerId playerId;

}
