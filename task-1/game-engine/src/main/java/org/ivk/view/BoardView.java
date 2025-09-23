package org.ivk.view;

import org.ivk.entity.Board;

public class BoardView {
    private final String W = "@";
    private final String B = "O";
    private final String EMPTY = "·";
    private Board board;
    public BoardView(Board board) {
        this.board = board;
    }
    public void printBoard() {
        int[][] field = board.getField();
        int size = board.getSize();

        // Заголовок с номерами столбцов
        System.out.print("  ");
        for (int col = 0; col < size; col++) {
            System.out.printf("%2d ", col);
        }
        System.out.println();

        // Само поле
        for (int row = 0; row < size; row++) {
            System.out.printf("%2d ", row);
            for (int col = 0; col < size; col++) {
                String symbol;
                switch (field[row][col]) {
                    case 1 -> symbol = W;  // 1 = Белый (W)
                    case 2 -> symbol = B;  // 2 = Черный (B)
                    default -> symbol = EMPTY;
                }
                System.out.printf("%-2s ", symbol);
            }
            System.out.println();
        }
    }
}
