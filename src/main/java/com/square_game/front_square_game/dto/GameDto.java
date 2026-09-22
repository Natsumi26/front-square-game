package com.square_game.front_square_game.dto;

public class GameDto {
    private String gameId;
    private String gameFactoryId;
    private String status;

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getGameFactoryId() {
        return gameFactoryId;
    }

    public void setGameFactoryId(String gameFactoryId) {
        this.gameFactoryId = gameFactoryId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
