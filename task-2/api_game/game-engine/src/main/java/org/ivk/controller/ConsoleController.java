package org.ivk.controller;

import org.ivk.entity.player.Player;
import org.ivk.factory.PlayerFactory;
import org.ivk.service.GameService;
import org.ivk.view.MessageView;

import java.util.Scanner;

public class ConsoleController {
    private final GameService gameService;

    private final MessageView messageView = new MessageView();

    public ConsoleController(GameService gameService) {
        this.gameService = gameService;
    }

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

            String[] parts = cmd.toUpperCase().trim().split("\\s+");
            String command = parts[0];

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

    public void handleGameCommand(String[] parts) {
        if (parts.length < 5) {
            messageView.incorrect();
            return;
        }

        try {
            String paramsStr = String.join(" ", java.util.Arrays.copyOfRange(parts, 1, parts.length))
                    .replace(",", " ")
                    .trim();

            String[] params = paramsStr.split("\\s+");
            if (params.length != 5) { // size + 2 игрока по type/color = 5 элементов
                messageView.incorrect();
                return;
            }

            int size = Integer.parseInt(params[0]);
            String type1 = params[1];
            String color1 = params[2];
            String type2 = params[3];
            String color2 = params[4];

            if (color1.equalsIgnoreCase(color2)) {
                messageView.errorSameColor();
                return;
            }

            Player player1 = PlayerFactory.createPlayer(type1, color1);
            Player player2 = PlayerFactory.createPlayer(type2, color2);

            gameService.createNewGame(size, player1, player2);
        } catch (Exception e) {
            messageView.incorrect();
        }
    }

    public String handleMoveCommand(String[] parts) {
        if (parts.length < 2) {
            return messageView.incorrect();
        }

        try {
            String coordsStr = String.join(" ", java.util.Arrays.copyOfRange(parts, 1, parts.length))
                    .replace(",", " ")
                    .trim();

            String[] coords = coordsStr.split("\\s+");
            if (coords.length != 2) {
                return messageView.incorrect();
            }

            int x = Integer.parseInt(coords[0]);
            int y = Integer.parseInt(coords[1]);

            return gameService.makeMove(x, y);
        } catch (NumberFormatException e) {
            messageView.incorrect();
            System.out.println(e.getMessage());
        }
        return null;
    }


}
