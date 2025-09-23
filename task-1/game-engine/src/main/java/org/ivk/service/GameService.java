package org.ivk.service;

import org.ivk.entity.Board;
import org.ivk.entity.Game;
import org.ivk.entity.player.Player;
import org.ivk.view.BoardView;

import java.util.ArrayList;
import java.util.List;

public class GameService {
    private Game game;
    private BoardView boardView;
    private Board board;
    private final List<String> moveLog = new ArrayList<>(); // ✅ Лог ходов


    public void createNewGame(Integer n, Player first, Player second) {
        game = new Game(new Board(n), first, second);
        board = game.getBoard();
        boardView = new BoardView(game.getBoard());
        game.setStatus("started");
        game.setCurrentPlayer(game.getFirst());
        moveLog.clear();
        startGame();
    }

    private void startGame() {
        render();
    }
    public void makeMove(int x, int y) {
        if (game == null) {
            System.out.println("Ошибка: Игра не начата. Сначала выполните команду GAME");
            return;
        }
        if (!makeMoveOnBoard(x, y)) return;

        // Добавляем запись в лог
        moveLog.add(game.getCurrentPlayer().getColor() + " -> (" + x + "," + y + ")");

        render();

        if (checkWin(x, y)) {
            System.out.println("Игра окончена");
            return;
        }
        if (checkDraw()) {
            System.out.println("Ничья!");
            game.setStatus("finished");
            return;
        }
        changePlayer();
    }
    private void render() {
        // Очистка консоли и возврат курсора наверх
        System.out.print("\033[H\033[2J");
        System.out.flush();

        // Секция логов
        System.out.println("Ходы:");
        for (String log : moveLog) {
            System.out.println("  " + log);
        }
        System.out.println();

        // Секция поля
        boardView.printBoard();
        System.out.println("Ход игрока: " + game.getCurrentPlayer().getColor());
    }

    public boolean makeMoveOnBoard(int x, int y) {
        if (!checkBoard(x,y))
            return false;
        Player currentPlayer = game.getCurrentPlayer();
        String color = currentPlayer.getColor();

        int[][] field = board.getField();
        if (color.equals("W"))
            field[x][y] = 1;  // W = 1
        if (color.equals("B"))
            field[x][y] = 2;  // B = 2
        return true;
    }
    public boolean checkBoard(int x, int y) {
        int[][] field = board.getField();
        return field[x][y] == 0;
    }
    public boolean checkWin(int lastX, int lastY) {
        Player currentPlayer = game.getCurrentPlayer();
        String color = currentPlayer.getColor();
        int[][] field = board.getField();
        int size = board.getSize();

        int colorValue = color.equals("W") ? 1 : 2; // ✅ исправлено

        int maxSide = size;

        for (int dx = -maxSide; dx <= maxSide; dx++) {
            for (int dy = -maxSide; dy <= maxSide; dy++) {
                if (dx == 0 && dy == 0) continue;
                if (checkSquareWithVectors(field, lastX, lastY, dx, dy, colorValue, size)) {
                    System.out.println("Победил игрок " + color);
                    game.setStatus("finished");
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkSquareWithVectors(int[][] field, int x, int y, int dx, int dy,
                                           int colorValue, int boardSize) {
        // Вершины квадрата: A, B, C, D
        // A(x, y)
        // B(x + dx, y + dy)
        // C(x + dx - dy, y + dy + dx)
        // D(x - dy, y + dx)

        int xA = x, yA = y;
        int xB = x + dx, yB = y + dy;
        int xC = x + dx - dy, yC = y + dy + dx;
        int xD = x - dy, yD = y + dx;

        // Проверяем все вершины
        return isValidPoint(xA, yA, boardSize) && field[xA][yA] == colorValue &&
                isValidPoint(xB, yB, boardSize) && field[xB][yB] == colorValue &&
                isValidPoint(xC, yC, boardSize) && field[xC][yC] == colorValue &&
                isValidPoint(xD, yD, boardSize) && field[xD][yD] == colorValue;
    }

    private boolean isValidPoint(int x, int y, int boardSize) {
        return x >= 0 && x < boardSize && y >= 0 && y < boardSize;
    }

    private boolean checkDraw() {
        int[][] field = board.getField();
        int size = board.getSize();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (field[i][j] == 0) {
                    return false; // Есть пустая клетка - не ничья
                }
            }
        }
        return true; // Все клетки заполнены - ничья
    }
    public void changePlayer() {
        Player currentPlayer = game.getCurrentPlayer();
        if (game.getFirst().equals(currentPlayer))
            currentPlayer = game.getSecond();
        else currentPlayer = game.getFirst();

        game.setCurrentPlayer(currentPlayer);
    }

}
