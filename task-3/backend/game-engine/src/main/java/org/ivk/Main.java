package org.ivk;

import org.ivk.controller.ConsoleController;
import org.ivk.service.GameService;

public class Main {
    public static void main(String[] args) {
        GameService gameService = new GameService();
        ConsoleController cc = new ConsoleController(gameService);
        cc.run();
    }
}