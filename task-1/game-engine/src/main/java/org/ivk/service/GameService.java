package org.ivk.service;

import org.ivk.entity.Board;
import org.ivk.entity.Game;
import org.ivk.entity.player.Player;
import org.ivk.view.BoardView;

public class GameService {
    private Game game;
    private BoardView boardView;

    public void createNewGame(Integer n, Player first, Player second) {
        game = new Game(new Board(n), first,second);
        boardView = new BoardView(new Board(n));
        game.setStatus("started");
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

        // Здесь логика выполнения хода
        // game.makeMove(x, y);
        boardView.printBoard();
    }
    public Game getGame() {
        return game;
    }


}
