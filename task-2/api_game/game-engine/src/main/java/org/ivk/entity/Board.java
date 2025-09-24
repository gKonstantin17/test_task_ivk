package org.ivk.entity;

public class Board {
    private final Integer size;
    private int[][] field;

    public Board(Integer size) {
        this.size = size;
        createField();
    }
    private void createField() {
        this.field = new int[size][size];
    }

    public int[][] getField() {
        return field;
    }

    public Integer getSize() {
        return size;
    }
}
