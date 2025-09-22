package org.ivk.service;

import org.ivk.entity.Board;
import org.ivk.entity.Game;
import org.ivk.entity.player.Comp;
import org.ivk.entity.player.Player;
import org.ivk.entity.player.User;
import org.ivk.view.BoardView;

public class GameService {
    private Game game;
    private BoardView boardView;
    public void receiveCommand(String cmd) {
        if (cmd.startsWith("GAME") || cmd.startsWith("game") ) {
            // GAME N TYPE C TYPE C
            String[] parts = cmd.split(" ");
            Integer size = Integer.parseInt(parts[1]);
            String typeFirstPlayer = parts[2];
            String colorFirstPlayer = parts[3];
            String typeSecondPlayer = parts[4];
            String colorSecondPlayer = parts[5];
            Player player1 = createPlayer(typeFirstPlayer,colorFirstPlayer);
            Player player2 = createPlayer(typeSecondPlayer,colorSecondPlayer);
            createNewGame(size,player1,player2);
        }
        // MOVE X, Y

    }
    public void createNewGame(Integer n, Player first, Player second) {
        game = new Game(new Board(n), first,second);
        boardView = new BoardView(new Board(n));
        game.setStatus("started");
        startGame();
    }

    private void startGame() {
        boardView.printBoard();
    }

    public Player createPlayer(String type, String color) {
        if (type.equals("user")) {
            User user = new User();
            user.setType(type);
            user.setColor(color);
            return user;
        }
        if (type.equals("comp")) {
            Comp comp = new Comp();
            comp.setType(type);
            comp.setColor(color);
            return comp;
        }
        return null;
    }


}
