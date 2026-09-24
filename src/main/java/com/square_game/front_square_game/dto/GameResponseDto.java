package com.square_game.front_square_game.dto;

import java.util.UUID;

public class GameResponseDto {

    private GameDto game;
    private UUID winnerId;

    public GameDto getGame() {
        return game;
    }

    public void setGame(GameDto game) {
        this.game = game;
    }

    public UUID getWinnerId() {
        return winnerId;
    }

    public void setWinnerId(UUID winnerId) {
        this.winnerId = winnerId;
    }
}
