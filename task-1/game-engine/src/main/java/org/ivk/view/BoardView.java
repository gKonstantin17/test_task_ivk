package org.ivk.view;

import org.ivk.entity.Board;

public class BoardView {
    private Board board;
    public BoardView(Board board) {
        this.board = board;
    }
    public void printBoard() {
        board.getField();
    }
}
