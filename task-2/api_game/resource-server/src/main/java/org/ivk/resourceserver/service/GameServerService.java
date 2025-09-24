package org.ivk.resourceserver.service;

import org.ivk.controller.ConsoleController;
import org.ivk.service.GameService;
import org.ivk.view.MessageView;
import org.springframework.stereotype.Service;

@Service
public class GameServerService {
    private final GameService gameService;
    private final ConsoleController consoleController;
    private final MessageView messageView;

    public GameServerService() {
        this.gameService = new GameService();
        this.consoleController = new ConsoleController();
        this.messageView = new MessageView();
    }

    public String startGame(String cmd) {
        String[] parts = cmd.toUpperCase().trim().split("\\s+");
        consoleController.handleGameCommand(parts);
        return messageView.startGame();
    }

    public String move(String cmd) {
        String[] parts = cmd.toUpperCase().trim().split("\\s+");
        return consoleController.handleMoveCommand(parts);
    }
}
