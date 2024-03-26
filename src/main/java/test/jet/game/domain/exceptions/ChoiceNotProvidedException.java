package test.jet.game.domain.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import test.jet.game.domain.models.value_objects.GameId;

@Getter
@AllArgsConstructor
public class ChoiceNotProvidedException extends RuntimeException {
    private GameId gameId;
}
