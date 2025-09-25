package org.ivk.resourceserver.service;

import org.ivk.resourceserver.dto.ResultMove;
import org.ivk.view.MessageView;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MoveResultParser {
    private final MessageView messageView;

    public MoveResultParser(MessageView messageView) {
        this.messageView = messageView;
    }

    public ResultMove parse(String moveResult) {
        ResultMove resultMove = new ResultMove();
        if (moveResult == null) return resultMove;

        String[] parts = moveResult.split(":", 2);
        String type = parts[0];
        String data = parts.length > 1 ? parts[1] : "";

        switch (type) {
            case "WIN" -> parseWin(resultMove, data);
            case "DRAW" -> {
                parseDraw(resultMove, data);
                resultMove.setNextMove(data);
            }
            case "ERROR" -> {
                resultMove.setResult("error");
                resultMove.setNextMove(data);
            }
            case "MOVE" -> parseMove(resultMove, data);
        }
        return resultMove;
    }

    private void parseWin(ResultMove resultMove, String data) {
        String[] winParts = data.split(":", 2);
        resultMove.setPlayerMove(winParts[0]);

        if (winParts.length > 1) {
            resultMove.setWinSquare(parseSquareCoords(winParts[1]));
        }

        // Здесь подключаем MessageView для корректного сообщения
        String color = winParts[0].substring(0, 1); // первый символ хода = цвет игрока
        resultMove.setResult(messageView.winGame(color));
    }

    private void parseMove(ResultMove resultMove, String data) {
        String[] moveParts = data.split(":", 2);
        resultMove.setPlayerMove(moveParts[0]);
        if (moveParts.length >= 2) {
            resultMove.setNextMove(moveParts[1]);
        }
    }
    private void parseDraw(ResultMove resultMove, String data) {
        resultMove.setPlayerMove(data);
        resultMove.setResult(messageView.drawGame());
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