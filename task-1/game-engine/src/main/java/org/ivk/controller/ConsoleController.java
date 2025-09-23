package org.ivk.controller;

import org.ivk.entity.player.Player;
import org.ivk.factory.PlayerFactory;
import org.ivk.service.GameService;
import org.ivk.view.MessageView;

import java.util.Scanner;

public class ConsoleController {
    private final GameService gameService = new GameService();

    private final MessageView messageView = new MessageView();
    public void run() {
        messageView.startApp();
        receiveCommands();
    }
    public void receiveCommands() {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            String cmd = scanner.nextLine();

            if (cmd == null || cmd.trim().isEmpty()) {
                continue;
            }

            String[] parts = cmd.trim().split("\\s+");
            String command = parts[0].toUpperCase();

            switch (command) {
                case "GAME" -> handleGameCommand(parts);
                case "MOVE" -> handleMoveCommand(parts);
                case "HELP" -> messageView.help();
                case "EXIT" -> running = false;
                default -> messageView.incorrect();
            }
        }
        scanner.close();
    }

    private void handleGameCommand(String[] parts) {
        if (parts.length != 6) {
            messageView.incorrect();
            return;
        }

        try {
            int size = Integer.parseInt(parts[1]);
            String type1 = parts[2];
            String color1 = parts[3];
            String type2 = parts[4];
            String color2 = parts[5];

            Player player1 = PlayerFactory.createPlayer(type1, color1);
            Player player2 = PlayerFactory.createPlayer(type2, color2);

            gameService.createNewGame(size, player1, player2);
        } catch (Exception e) {
            messageView.incorrect();
        }
    }

    private void handleMoveCommand(String[] parts) {
        if (parts.length != 3) {
            messageView.incorrect();
            return;
        }

        try {
            int x = Integer.parseInt(parts[1]);
            int y = Integer.parseInt(parts[2]);
            gameService.makeMove(x, y);
        } catch (NumberFormatException e) {
            messageView.incorrect();
        }
    }

}
