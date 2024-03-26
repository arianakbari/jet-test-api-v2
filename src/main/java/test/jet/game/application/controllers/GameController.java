package test.jet.game.application.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import test.jet.game.application.dtos.*;
import test.jet.game.domain.models.entities.Game;
import test.jet.game.domain.models.entities.Player;
import test.jet.game.domain.models.value_objects.GameId;
import test.jet.game.domain.models.value_objects.PlayerId;
import test.jet.game.domain.services.IGameService;
import test.jet.game.domain.services.INotificationService;
import test.jet.game.domain.services.ITokenService;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/game")
@AllArgsConstructor
public class GameController {
    private IGameService gameService;
    private INotificationService notificationService;
    private ITokenService tokenService;

    private PlayerResponseDto toDto(Player player) {
        return PlayerResponseDto.builder()
                .id(player.getId().getValue())
                .email(player.getEmail().getValue())
                .build();
    }

    private GameResponseDto toDto(Game game) {
        return GameResponseDto.builder()
                .id(game.getId().getValue())
                .number(game.getNumber())
                .status(game.getStatus())
                .mode(game.getMode())
                .currentTurnPlayerId(game.getCurrentTurnPlayer() != null ? game.getCurrentTurnPlayer().getId().getValue() : null)
                .winnerId(game.getWinner() != null ? game.getWinner().getId().getValue() : null)
                .players(game.getPlayers().stream().map(this::toDto).collect(Collectors.toList()))
                .build();
    }

    @PostMapping("/join")
    public ResponseEntity<JoinGameResponseDto> joinGame(@RequestBody @Valid JoinGameRequestDto dto) {
        var response = JoinGameResponseDto.builder()
                .game(toDto(gameService.joinGame(dto.getEmail(), dto.getMode(), dto.getInputType())))
                .token(tokenService.sign(dto.getEmail()))
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/make-move")
    public ResponseEntity<GameResponseDto> makeMove(@RequestBody @Valid MakeMoveRequestDto dto) {
        test.jet.game.infrastructure.persistence.entities.Player player =
                (test.jet.game.infrastructure.persistence.entities.Player)
                        SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var response = gameService.makeMove(new GameId(dto.getGameId().toString()), new PlayerId(player.getId().toString()), dto.getChoice());
        return ResponseEntity.ok(toDto(response));
    }

    @PostMapping("/subscribe")
    public ResponseEntity<String> subscribe(@RequestBody @Valid SubscribeRequestDto dto) {
        test.jet.game.infrastructure.persistence.entities.Player player =
                (test.jet.game.infrastructure.persistence.entities.Player)
                        SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var response = notificationService.subscribe(player.getId().toString(), dto.getSocketId());
        return ResponseEntity.ok(response);
    }
}
