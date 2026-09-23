package com.square_game.front_square_game.dto;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class GameDto {
    private UUID id;
    private String status;

    private Map<String, TokenDto> board;

    private List<UUID> playerIds;

    private int boardSize;

    private String factoryId;

    private UUID currentPlayerId;

    private List<TokenDto> remainingTokens;

    private List<TokenDto> removedTokens;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Map<String, TokenDto> getBoard() {
        return board;
    }

    public void setBoard(Map<String, TokenDto> board) {
        this.board = board;
    }

    public List<UUID> getPlayerIds() {
        return playerIds;
    }

    public void setPlayerIds(List<UUID> playerIds) {
        this.playerIds = playerIds;
    }

    public int getBoardSize() {
        return boardSize;
    }

    public void setBoardSize(int boardSize) {
        this.boardSize = boardSize;
    }

    public String getFactoryId() {
        return factoryId;
    }

    public void setFactoryId(String factoryId) {
        this.factoryId = factoryId;
    }

    public UUID getCurrentPlayerId() {
        return currentPlayerId;
    }

    public void setCurrentPlayerId(UUID currentPlayerId) {
        this.currentPlayerId = currentPlayerId;
    }

    public List<TokenDto> getRemainingTokens() {
        return remainingTokens;
    }

    public void setRemainingTokens(List<TokenDto> remainingTokens) {
        this.remainingTokens = remainingTokens;
    }

    public List<TokenDto> getRemovedTokens() {
        return removedTokens;
    }

    public void setRemovedTokens(List<TokenDto> removedTokens) {
        this.removedTokens = removedTokens;
    }
}
