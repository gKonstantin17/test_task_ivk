package org.ivk.service;

import org.ivk.entity.Board;
import org.ivk.entity.player.Player;

public class WinChecker implements IWinChecker {
    private final Board board;

    public WinChecker(Board board) {
        this.board = board;
    }

    @Override
    public WinResult checkWin(Player player, int lastX, int lastY) {
        int[][] field = board.getField();
        int size = board.getSize();
        int colorValue = player.getColor().equals("W") ? 1 : 2;

        int maxSide = size;
        for (int dx = -maxSide; dx <= maxSide; dx++) {
            for (int dy = -maxSide; dy <= maxSide; dy++) {
                if (dx == 0 && dy == 0) continue;

                int[][] coords = checkSquareWithVectors(field, lastX, lastY, dx, dy, colorValue, size);
                if (coords != null) {
                    return new WinResult(true, coords);
                }
            }
        }
        return new WinResult(false, null);
    }

    /**
     * Проверяет квадрат по вектору. Если найден, возвращает вершины, иначе null.
     */
    private int[][] checkSquareWithVectors(int[][] field, int x, int y, int dx, int dy,
                                           int colorValue, int boardSize) {
        int xA = x, yA = y;
        int xB = x + dx, yB = y + dy;
        int xC = x + dx - dy, yC = y + dy + dx;
        int xD = x - dy, yD = y + dx;

        if (isValidPoint(xA, yA, boardSize) && field[xA][yA] == colorValue &&
                isValidPoint(xB, yB, boardSize) && field[xB][yB] == colorValue &&
                isValidPoint(xC, yC, boardSize) && field[xC][yC] == colorValue &&
                isValidPoint(xD, yD, boardSize) && field[xD][yD] == colorValue) {

            return new int[][]{{xA,yA},{xB,yB},{xC,yC},{xD,yD}};
        }
        return null;
    }

    private boolean isValidPoint(int x, int y, int boardSize) {
        return x >= 0 && x < boardSize && y >= 0 && y < boardSize;
    }
}
