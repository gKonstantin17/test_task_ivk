package org.ivk.resourceserver.service;

import org.ivk.controller.ConsoleController;
import org.ivk.entity.player.Comp;
import org.ivk.resourceserver.dto.ResultCreateGame;
import org.ivk.resourceserver.dto.ResultMove;
import org.ivk.service.GameService;
import org.ivk.view.MessageView;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GameServerService {
    private final GameService gameService;
    private final ConsoleController consoleController;
    private final MessageView messageView;

    public GameServerService() {
        this.gameService = new GameService();
        this.consoleController = new ConsoleController(gameService);
        this.messageView = new MessageView();
    }

    public ResultCreateGame startGame(String cmd) {
        ResultCreateGame result = new ResultCreateGame();

        String[] parts = cmd.toUpperCase().trim().split("\\s+");
        consoleController.handleGameCommand(parts);

        result.setResult(messageView.startGame());

        if (isFirstPlayerComputer()) {
            String computerMoveResult = gameService.makeFirstComputerMove();
            result.setFirstMove(extractMoveFromResult(computerMoveResult));
        }

        return result;
    }

    private boolean isFirstPlayerComputer() {
        return gameService.getCurrentPlayer() instanceof Comp;
    }

    private String extractMoveFromResult(String moveResult) {
        if (moveResult == null) return "No move";

        String[] parts = moveResult.split(":", 2);
        String type = parts[0];
        String data = parts.length > 1 ? parts[1] : "";

        switch (type) {
            case "WIN":
            case "DRAW":
            case "MOVE":
                String[] moveParts = data.split(":", 2);
                return moveParts[0]; // возвращаем сам ход
            case "ERROR":
                return "Error: " + data;
            default:
                return moveResult;
        }
    }

    public ResultMove move(String cmd) {
        String[] parts = cmd.toUpperCase().trim().split("\\s+");
        String moveResult = consoleController.handleMoveCommand(parts);
        return parseMoveResult(moveResult);
    }
    private ResultMove parseMoveResult(String moveResult) {
        ResultMove resultMove = new ResultMove();

        if (moveResult == null) {
            return resultMove;
        }

        String[] parts = moveResult.split(":", 2);
        String type = parts[0];
        String data = parts.length > 1 ? parts[1] : "";

        switch (type) {
            case "WIN":
                String[] winParts = data.split(":", 2);
                resultMove.setResult("win");
                resultMove.setNextMove(winParts[0]);
                if (winParts.length > 1) {
                    resultMove.setWinSquare(parseSquareCoords(winParts[1]));
                }
                break;

            case "DRAW":
                resultMove.setResult("draw");
                resultMove.setNextMove(data);
                break;

            case "ERROR":
                resultMove.setResult("error");
                resultMove.setNextMove(data);
                break;

            case "MOVE":
                String[] moveParts = data.split(":", 2);
                resultMove.setResult(null);
                if (moveParts.length >= 2) {
                    resultMove.setNextMove(moveParts[1]);
                } else {
                    resultMove.setNextMove(moveParts[0]);
                }
                break;
        }

        return resultMove;
    }

    private List<int[]> parseSquareCoords(String coordsStr) {
        List<int[]> square = new ArrayList<>();
        String[] pairs = coordsStr.split(",");
        for (int i = 0; i < pairs.length; i += 2) {
            if (i + 1 < pairs.length) {
                int x = Integer.parseInt(pairs[i].trim());
                int y = Integer.parseInt(pairs[i + 1].trim());
                square.add(new int[]{x, y});
            }
        }
        return square;
    }
}
