package com.square_game.front_square_game.dto;

import java.util.List;
import java.util.UUID;

public class TokenDto {

    private String name;
    private CellPositionDto position;
    private List<CellPositionDto> allowedMoves;
    private UUID ownerId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CellPositionDto getPosition() {
        return position;
    }

    public void setPosition(CellPositionDto position) {
        this.position = position;
    }

    public List<CellPositionDto> getAllowedMoves() {
        return allowedMoves;
    }

    public void setAllowedMoves(List<CellPositionDto> allowedMoves) {
        this.allowedMoves = allowedMoves;
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(UUID ownerId) {
        this.ownerId = ownerId;
    }
}
