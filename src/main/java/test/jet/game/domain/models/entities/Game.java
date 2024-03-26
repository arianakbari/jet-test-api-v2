package test.jet.game.domain.models.entities;

import lombok.*;
import org.jetbrains.annotations.Nullable;
import test.jet.game.domain.exceptions.ChoiceNotProvidedException;
import test.jet.game.domain.exceptions.NotPlayersTurnException;
import test.jet.game.domain.exceptions.PlayerDoesNotBelongToGameException;
import test.jet.game.domain.models.value_objects.GameId;
import test.jet.game.domain.models.value_objects.PlayerEmail;
import test.jet.game.domain.models.value_objects.PlayerId;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Builder
public class Game {
    public static enum GAME_STATUS {
        WAITING_FOR_PLAYER,
        IN_PROGRESS,
        FINISHED,
    }

    public static enum GAME_MODE {
        PLAYER_VS_PLAYER,
        PLAYER_VS_COMPUTER
    }

    private GameId id;
    private Integer number;
    private GAME_STATUS status;
    private GAME_MODE mode;
    private Player currentTurnPlayer;
    private Player winner;
    private List<Player> players = new ArrayList<>();

    public List<Player> getPlayers() {
        if (players == null) {
            players = new ArrayList<>();
        }
        return players;
    }

    public void startGame(String email, Player.PLAYER_INPUT_TYPE inputType) {
        Player player = Player.builder()
                .email(new PlayerEmail(email))
                .inputType(inputType)
                .build();
        setStatus(GAME_STATUS.IN_PROGRESS);
        setCurrentTurnPlayer(player);
        addPlayer(player);
    }

    public void setInitialStatus() {
        if (mode == GAME_MODE.PLAYER_VS_COMPUTER) {
            setStatus(GAME_STATUS.IN_PROGRESS);
        } else {
            setStatus(GAME_STATUS.WAITING_FOR_PLAYER);
        }
    }

    public void setInitialNumber() {
        setNumber(new Random().nextInt(100) + 1);
    }

    public void addPlayer(Player player) {
        if (getPlayers().size() == 2) {
            throw new IllegalArgumentException("Game is full. Can't add player!");
        }

        getPlayers().add(player);
    }

    public void setInitialCurrentTurnPlayer(Player player) {
        if (mode == GAME_MODE.PLAYER_VS_COMPUTER) {
            setCurrentTurnPlayer(player);
        } else {
            setCurrentTurnPlayer(null);
        }
    }

    public void checkPlayerBelongsToThisGame(PlayerId playerId) {
        if (getPlayers() != null && getPlayers().stream().noneMatch(p -> p.getId().equals(playerId))) {
            throw new PlayerDoesNotBelongToGameException(getId(), playerId);
        }
    }

    public void checkForPlayerTurn(PlayerId playerId) {
        if (getCurrentTurnPlayer() == null || !getCurrentTurnPlayer().getId().equals(playerId)) {
            throw new NotPlayersTurnException();
        }
    }

    public void playTurn(PlayerId playerId, @Nullable Integer choice) {
        var player = getPlayers().stream().filter(p -> p.getId().equals(playerId)).findFirst().orElseThrow(() -> new PlayerDoesNotBelongToGameException(getId(), playerId));
        if (player.getInputType() == Player.PLAYER_INPUT_TYPE.MANUAL && choice == null) {
            throw new ChoiceNotProvidedException(getId());
        }
        // for players turn
        calculateNumber(player.getInputType() == Player.PLAYER_INPUT_TYPE.AUTOMATIC ? getRandomChoice() : choice);
    }

    public void playComputerTurnIfApplicable() {
        if (getMode() == GAME_MODE.PLAYER_VS_COMPUTER) {
            calculateNumber(getRandomChoice());
        }
    }

    public Boolean canBeFinished() {
        return getNumber() <= 1;
    }

    public void finishGame(Boolean isWinnerComputer) {
        setStatus(GAME_STATUS.FINISHED);
        if (getMode() == GAME_MODE.PLAYER_VS_COMPUTER && isWinnerComputer) {
            setWinner(null);
        } else {
            setWinner(getCurrentTurnPlayer());
        }
        setCurrentTurnPlayer(null);
    }

    public PlayerId getOtherPlayersId(PlayerId playerId) {
        return getPlayers().stream().filter(p -> !p.getId().equals(playerId)).findFirst().get().getId();
    }

    public void swapTurn() {
        if (getMode() == GAME_MODE.PLAYER_VS_PLAYER) {
            setCurrentTurnPlayer(getPlayers().stream().filter(p -> !p.getId().equals(getCurrentTurnPlayer().getId())).findFirst().get());
        }
    }

    private Integer getRandomChoice() {
        return new Random().nextInt(3) - 1;
    }

    private void calculateNumber(Integer choice) {
        Integer newNumber = (getNumber() + choice) / 3;
        setNumber(newNumber);
    }
}
