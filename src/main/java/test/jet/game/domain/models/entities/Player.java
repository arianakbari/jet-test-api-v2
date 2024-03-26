package test.jet.game.domain.models.entities;

import lombok.*;
import test.jet.game.domain.models.value_objects.PlayerEmail;
import test.jet.game.domain.models.value_objects.PlayerId;

@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Builder
public class Player {
    public static enum PLAYER_INPUT_TYPE {
        MANUAL,
        AUTOMATIC
    }

    private PlayerId id;
    private PlayerEmail email;
    private PLAYER_INPUT_TYPE inputType;
}
