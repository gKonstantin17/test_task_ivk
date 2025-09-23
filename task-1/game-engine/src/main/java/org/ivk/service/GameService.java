package org.ivk.service;

import org.ivk.entity.Board;
import org.ivk.entity.Game;
import org.ivk.entity.player.Player;
import org.ivk.view.BoardView;

public class GameService {
    private Game game;
    private BoardView boardView;
    private Board board;

    public void createNewGame(Integer n, Player first, Player second) {
        game = new Game(new Board(n), first,second);
        board = game.getBoard();
        boardView = new BoardView(game.getBoard());
        game.setStatus("started");
        game.setCurrentPlayer(game.getFirst());
        startGame();
    }

    private void startGame() {
        boardView.printBoard();
        System.out.println("Ход игрока: " + game.getCurrentPlayer().getColor());
    }
    public void makeMove(int x, int y) {
        if (game == null) {
            System.out.println("Ошибка: Игра не начата. Сначала выполните команду GAME");
            return;
        }
        Player currentPlayer = game.getCurrentPlayer();
        String color = currentPlayer.getColor();
        makeMoveOnBoard(x,y,color);
        changePlayer();

        boardView.printBoard();
    }
    public Game getGame() {
        return game;
    }
    public void makeMoveOnBoard(int x, int y, String color) {
        int[][] field = board.getField();
        if (color.equals("W"))
            field[x][y] = 2;  // W = 2 (как в текущем BoardView)
        if (color.equals("B"))
            field[x][y] = 1;  // B = 1 (как в текущем BoardView)
    }
    public void changePlayer() {
        Player currentPlayer = game.getCurrentPlayer();
        if (game.getFirst().equals(currentPlayer))
            currentPlayer = game.getSecond();
        else currentPlayer = game.getFirst();

        game.setCurrentPlayer(currentPlayer);
    }

}
