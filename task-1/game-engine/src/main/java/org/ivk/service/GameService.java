package org.ivk.service;

import org.ivk.entity.Board;
import org.ivk.entity.Game;
import org.ivk.entity.player.Player;

public class GameService {
    private Game game;
    public void receiveCommand(String cmd) {

    }
    public void createNewGame(Integer n, Player first, Player second) {
        game = new Game(new Board(n), first,second);
        game.setStatus("started");
    }


}
