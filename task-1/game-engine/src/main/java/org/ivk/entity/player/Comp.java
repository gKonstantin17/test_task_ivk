package org.ivk.entity.player;

import org.ivk.entity.Board;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Comp extends Player {
    private final Random random = new Random();

    public int[] makeMove(Board board, int myValue, int oppValue) {
        int size = board.getSize();
        int[][] field = board.getField();

        //  выигрышный ход
        int[] winMove = findCriticalMove(field, size, myValue);
        if (winMove != null) return winMove;

        //  блокирующий ход
        int[] blockMove = findCriticalMove(field, size, oppValue);
        if (blockMove != null) return blockMove;

        //  Любая свободная клетка, выбираем случайно
        List<int[]> emptyCells = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (field[i][j] == 0) {
                    emptyCells.add(new int[]{i, j});
                }
            }
        }

        if (!emptyCells.isEmpty()) {
            return emptyCells.get(random.nextInt(emptyCells.size()));
        }

        return null; // доска полна
    }

    private int[] findCriticalMove(int[][] field, int size, int colorValue) {
        int maxSide = size;

        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                if (field[x][y] != colorValue) continue;

                for (int dx = -maxSide; dx <= maxSide; dx++) {
                    for (int dy = -maxSide; dy <= maxSide; dy++) {
                        if (dx == 0 && dy == 0) continue;

                        int[][] missing = checkAlmostSquare(field, x, y, dx, dy, colorValue, size);
                        if (missing != null) return missing[0];
                    }
                }
            }
        }
        return null;
    }

    /**
     * Проверяет квадрат: 3 точки уже заняты, возвращает массив пустых клеток (1 или несколько), чтобы замкнуть квадрат
     */
    private int[][] checkAlmostSquare(int[][] field, int x, int y, int dx, int dy, int colorValue, int size) {
        int xA = x, yA = y;
        int xB = x + dx, yB = y + dy;
        int xC = x + dx - dy, yC = y + dy + dx;
        int xD = x - dy, yD = y + dx;

        int[][] pts = {{xA, yA}, {xB, yB}, {xC, yC}, {xD, yD}};
        int emptyCount = 0;
        int[][] emptyPts = new int[1][2]; // возвращаем только первую пустую клетку

        for (int[] p : pts) {
            int px = p[0], py = p[1];
            if (px < 0 || px >= size || py < 0 || py >= size) return null;

            if (field[px][py] == 0) {
                emptyPts[0] = new int[]{px, py};
                emptyCount++;
            } else if (field[px][py] != colorValue) {
                return null; // чужая фигура мешает
            }
        }

        return emptyCount == 1 ? emptyPts : null;
    }
}
