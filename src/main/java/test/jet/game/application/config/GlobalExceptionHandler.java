package test.jet.game.application.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import test.jet.game.domain.exceptions.ChoiceNotProvidedException;
import test.jet.game.domain.exceptions.GameNotFoundException;
import test.jet.game.domain.exceptions.NotPlayersTurnException;
import test.jet.game.domain.exceptions.PlayerDoesNotBelongToGameException;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(GameNotFoundException.class)
    public ProblemDetail handleGameNotFoundException(GameNotFoundException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problemDetail.setTitle(e.getClass().getCanonicalName());
        problemDetail.setDetail("Game not found with id " + e.getId().getValue());
        return problemDetail;
    }

    @ExceptionHandler(NotPlayersTurnException.class)
    public ProblemDetail handleNotPlayersTurnException(NotPlayersTurnException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.FORBIDDEN);
        problemDetail.setTitle(e.getClass().getCanonicalName());
        problemDetail.setDetail("It's not this player's turn to play");
        return problemDetail;
    }

    @ExceptionHandler(ChoiceNotProvidedException.class)
    public ProblemDetail handleChoiceNotProvidedException(ChoiceNotProvidedException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setTitle(e.getClass().getCanonicalName());
        problemDetail.setDetail("Choice should be provided for game with id " + e.getGameId().getValue());
        return problemDetail;
    }

    @ExceptionHandler(PlayerDoesNotBelongToGameException.class)
    public ProblemDetail handlePlayerDoesNotBelongToGameException(PlayerDoesNotBelongToGameException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.FORBIDDEN);
        problemDetail.setTitle(e.getClass().getCanonicalName());
        problemDetail.setDetail("Player with id " + e.getPlayerId().getValue() + " does not belong to game with id " + e.getId().getValue());
        return problemDetail;
    }
}
