package test.jet.game.infrastructure.persistence;


import test.jet.game.application.dtos.GameResponseDto;
import test.jet.game.application.dtos.PlayerResponseDto;
import test.jet.game.domain.models.entities.Game;
import test.jet.game.domain.models.entities.Player;
import test.jet.game.domain.models.value_objects.GameId;
import test.jet.game.domain.models.value_objects.PlayerEmail;
import test.jet.game.domain.models.value_objects.PlayerId;
import test.jet.game.infrastructure.persistence.entities.Role;

import java.util.UUID;
import java.util.stream.Collectors;

public class Mapper {
    public static Player toDomain(test.jet.game.infrastructure.persistence.entities.Player player) {
        return Player.builder()
                .id(new PlayerId(player.getId().toString()))
                .email(new PlayerEmail(player.getEmail()))
                .inputType(player.getInputType())
                .build();
    }

    public static Game toDomain(test.jet.game.infrastructure.persistence.entities.Game game) {
        return Game.builder()
                .id(new GameId(game.getId().toString()))
                .number(game.getNumber())
                .status(game.getStatus())
                .mode(game.getMode())
                .currentTurnPlayer(game.getCurrentTurnPlayer() != null ? toDomain(game.getCurrentTurnPlayer()) : null)
                .winner(game.getWinner() != null ? toDomain(game.getWinner()) : null)
                .players(game.getPlayers().stream().map(Mapper::toDomain).collect(Collectors.toList()))
                .build();
    }

    public static test.jet.game.infrastructure.persistence.entities.Game fromDomain(Game game) {
        return test.jet.game.infrastructure.persistence.entities.Game.builder()
                .id(game.getId() != null ? UUID.fromString(game.getId().getValue()) : null)
                .number(game.getNumber())
                .status(game.getStatus())
                .mode(game.getMode())
                .currentTurnPlayer(game.getCurrentTurnPlayer() != null ? fromDomain(game.getCurrentTurnPlayer()) : null)
                .winner(game.getWinner() != null ? fromDomain(game.getWinner()) : null)
                .players(game.getPlayers().stream().map(Mapper::fromDomain).collect(Collectors.toList()))
                .build();
    }

    public static test.jet.game.infrastructure.persistence.entities.Player fromDomain (Player player) {
        return test.jet.game.infrastructure.persistence.entities.Player.builder()
                .id(player.getId() != null ? UUID.fromString(player.getId().getValue()) : null)
                .email(player.getEmail() != null ? player.getEmail().getValue() : null)
                .inputType(player.getInputType())
                .role(Role.USER)
                .build();
    }

    public static PlayerResponseDto toDto(Player player) {
        return PlayerResponseDto.builder()
                .id(player.getId().getValue())
                .email(player.getEmail().getValue())
                .inputType(player.getInputType())
                .build();
    }

    public static GameResponseDto toDto(Game game) {
        return GameResponseDto.builder()
                .id(game.getId().getValue())
                .number(game.getNumber())
                .status(game.getStatus())
                .mode(game.getMode())
                .currentTurnPlayerId(game.getCurrentTurnPlayer() != null ? game.getCurrentTurnPlayer().getId().getValue() : null)
                .winnerId(game.getWinner() != null ? game.getWinner().getId().getValue() : null)
                .players(game.getPlayers().stream().map(Mapper::toDto).collect(Collectors.toList()))
                .build();
    }
}
