package org.ivk.resourceserver.service;

import org.ivk.controller.ConsoleController;
import org.ivk.entity.player.Comp;
import org.ivk.entity.player.Player;
import org.ivk.resourceserver.dto.ResultCreateGame;
import org.ivk.resourceserver.dto.ResultMove;
import org.ivk.resourceserver.service.MoveResultParser;
import org.ivk.resourceserver.service.SuggestedMoveService;
import org.ivk.service.GameService;
import org.ivk.view.MessageView;
import org.springframework.stereotype.Service;

@Service
public class GameServerService {
    private final GameService gameService;
    private final ConsoleController consoleController;
    private final MessageView messageView;
    private final MoveResultParser moveResultParser;
    private final SuggestedMoveService suggestedMoveService;

    public GameServerService(GameService gameService,
                             ConsoleController consoleController,
                             MessageView messageView,
                             MoveResultParser moveResultParser,
                             SuggestedMoveService suggestedMoveService) {
        this.gameService = gameService;
        this.consoleController = consoleController;
        this.messageView = messageView;
        this.moveResultParser = moveResultParser;
        this.suggestedMoveService = suggestedMoveService;
    }

    public ResultCreateGame startGame(String cmd) {
        ResultCreateGame result = new ResultCreateGame();

        String[] parts = cmd.toUpperCase().trim().split("\\s+");
        consoleController.handleGameCommand(parts);

        Player current = gameService.getCurrentPlayer();
        result.setResult(messageView.startGame());

        if (current instanceof Comp) {
            String moveResult = gameService.makeFirstComputerMove();
            ResultMove parsed = moveResultParser.parse(moveResult);

            result.setFirstMove(parsed.getPlayerMove());
            result.setNextMove(parsed.getNextMove());
        } else {
            result.setFirstMove("First player: " + current.getColor());
            result.setNextMove(suggestedMoveService.getSuggestedMove(current, gameService.getBoard()));
        }

        return result;
    }

    public ResultMove move(String cmd) {
        String[] parts = cmd.toUpperCase().trim().split("\\s+");
        String moveResult = consoleController.handleMoveCommand(parts);
        return moveResultParser.parse(moveResult);
    }
}