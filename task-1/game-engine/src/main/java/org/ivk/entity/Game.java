package org.ivk.entity;

import org.ivk.entity.player.Player;

public class Game {
    private Board board;
    private Player first;
    private Player second;
    private String status;
    public Game(Board board, Player first, Player second) {
        this.board = board;
        this.first = first;
        this.second = second;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
