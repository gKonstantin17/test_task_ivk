package org.ivk.service;

import org.ivk.entity.Board;
import org.ivk.entity.Game;
import org.ivk.entity.player.Player;

public class MoveExecutor {
    private final Game game;
    private final Board board;

    public MoveExecutor(Game game) {
        this.game = game;
        this.board = game.getBoard();
    }

    public boolean execute(int x, int y) {
        if (!isValidCoordinates(x, y)) return false;
        int[][] field = board.getField();
        if (field[x][y] != 0) return false;

        Player current = game.getCurrentPlayer();
        int value = playerValue(current);
        field[x][y] = value;
        return true;
    }

    private boolean isValidCoordinates(int x, int y) {
        int size = board.getSize();
        return x >= 0 && x < size && y >= 0 && y < size;
    }

    private int playerValue(Player p) {
        return "W".equals(p.getColor()) ? 1 : 2;
    }
}
