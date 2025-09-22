package org.ivk.controller;

import org.ivk.service.GameService;
import org.ivk.view.MessageView;

import java.util.Scanner;

public class ConsoleController {
    private final GameService gameService = new GameService();

    private final MessageView messageView = new MessageView();
    public void run() {
        messageView.startApp();
        consumeCommands();
    }
    public void consumeCommands() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String message = scanner.nextLine();
            if (message.startsWith("GAME") || message.startsWith("game"))
                gameService.receiveCommand(message);

            else if (message.equals("MOVE") || message.equals("move"))
                gameService.receiveCommand(message);

            else if (message.equals("HELP") || message.equals("help")) {
                scanner.close();
                break;
            }
            else if (message.equals("EXIT") || message.equals("exit")) {
                messageView.help();
            }
            else {
                messageView.incorrect();
            }
        }
    }
}
