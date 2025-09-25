package org.ivk.resourceserver.service;

import org.ivk.resourceserver.dto.ResultMove;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MoveResultParser {

    public ResultMove parse(String moveResult) {
        ResultMove resultMove = new ResultMove();
        if (moveResult == null) return resultMove;

        String[] parts = moveResult.split(":", 2);
        String type = parts[0];
        String data = parts.length > 1 ? parts[1] : "";

        switch (type) {
            case "WIN" -> parseWin(resultMove, data);
            case "DRAW" -> {
                resultMove.setResult("draw");
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
        resultMove.setResult("win");
        resultMove.setNextMove(winParts[0]);
        if (winParts.length > 1) {
            resultMove.setWinSquare(parseSquareCoords(winParts[1]));
        }
    }

    private void parseMove(ResultMove resultMove, String data) {
        String[] moveParts = data.split(":", 2);
        if (moveParts.length >= 2) {
            resultMove.setNextMove(moveParts[1]);
        } else {
            resultMove.setNextMove(moveParts[0]);
        }
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