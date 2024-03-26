package test.jet.game.application.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import test.jet.game.application.dtos.*;
import test.jet.game.domain.models.entities.Game;
import test.jet.game.domain.models.entities.Player;
import test.jet.game.domain.models.value_objects.GameId;
import test.jet.game.domain.models.value_objects.PlayerId;
import test.jet.game.domain.services.IGameService;
import test.jet.game.domain.services.INotificationService;
import test.jet.game.domain.services.ITokenService;
import test.jet.game.infrastructure.persistence.Mapper;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/game")
@AllArgsConstructor
@CrossOrigin("*")
public class GameController {
    private IGameService gameService;
    private INotificationService notificationService;
    private ITokenService tokenService;

    @PostMapping("/join")
    public ResponseEntity<JoinGameResponseDto> joinGame(@RequestBody @Valid JoinGameRequestDto dto) {
        var response = JoinGameResponseDto.builder()
                .game(Mapper.toDto(gameService.joinGame(dto.getEmail(), dto.getMode(), dto.getInputType())))
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
        return ResponseEntity.ok(Mapper.toDto(response));
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
