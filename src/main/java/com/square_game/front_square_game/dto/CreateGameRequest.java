package com.square_game.front_square_game.dto;

import java.util.Set;
import java.util.UUID;

public class CreateGameRequest {

    private String type;
    private Integer playerCount;
    private Integer boardSize;
    private Set<UUID> opponentIds;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getPlayerCount() {
        return playerCount;
    }

    public void setPlayerCount(Integer playerCount) {
        this.playerCount = playerCount;
    }

    public Integer getBoardSize() {
        return boardSize;
    }

    public void setBoardSize(Integer boardSize) {
        this.boardSize = boardSize;
    }

    public Set<UUID> getOpponentIds() {
        return opponentIds;
    }

    public void setOpponentIds(Set<UUID> opponentIds) {
        this.opponentIds = opponentIds;
    }
}
