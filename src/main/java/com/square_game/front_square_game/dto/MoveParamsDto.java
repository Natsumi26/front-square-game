package com.square_game.front_square_game.dto;

public class MoveParamsDto {
    private CellPositionDto from;
    private CellPositionDto to;

    public CellPositionDto getFrom() {
        return from;
    }

    public void setFrom(CellPositionDto from) {
        this.from = from;
    }

    public CellPositionDto getTo() {
        return to;
    }

    public void setTo(CellPositionDto to) {
        this.to = to;
    }
}
