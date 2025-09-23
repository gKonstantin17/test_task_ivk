package org.ivk.service;

public class WinResult {
    private final boolean isWin;
    private final int[][] squareCoords; // 4 вершины квадрата: {{xA,yA}, {xB,yB}, {xC,yC}, {xD,yD}}

    public WinResult(boolean isWin, int[][] squareCoords) {
        this.isWin = isWin;
        this.squareCoords = squareCoords;
    }

    public boolean isWin() { return isWin; }
    public int[][] getSquareCoords() { return squareCoords; }
}