package org.ivk.service;

import org.ivk.entity.Board;
import org.ivk.entity.Game;
import org.ivk.entity.player.Comp;
import org.ivk.entity.player.Player;
import org.ivk.view.BoardView;
import org.ivk.view.MessageView;

public class GameService {
    private Game game;
    private BoardView boardView;

    public Board getBoard() {
        return board;
    }

    private Board board;

    private MoveLogger moveLogger;
    private MoveExecutor moveExecutor;
    private IWinChecker winChecker;

    private final MessageView messageView = new MessageView();

    public GameService() {
        this.moveLogger = new MoveLogger();
    }

    public void createNewGame(Integer n, Player first, Player second) {
        game = new Game(new Board(n), first, second);
        board = game.getBoard();
        boardView = new BoardView(board);

        // компоненты, зависящие от board/game
        moveExecutor = new MoveExecutor(game);
        winChecker = new WinChecker(board);

        game.setStatus("started");
        game.setCurrentPlayer(game.getFirst());
        moveLogger.clear();
        messageView.startGame();
        render();

    }

    public String makeMove(int x, int y) {
        if (game == null)
            return "ERROR:" + messageView.errorGameNotStarted();

        if (game.getStatus().equals("finished"))
            return "ERROR:" + messageView.errorGameOver();

        String playerMove = game.getCurrentPlayer().getColor() + " (" + x + "," + y + ")";

        if (!moveExecutor.execute(x, y)) {
            return "ERROR:" + messageView.errorInvalidMove();
        }

        moveLogger.log(playerMove);
        render();

        WinResult result = winChecker.checkWin(game.getCurrentPlayer(), x, y);
        if (result.isWin()) {
            game.setStatus("finished");
            String squareCoords = formatSquareCoords(result.getSquareCoords());
            return "WIN:" + playerMove + ":" + squareCoords;
        }

        if (checkDraw()) {
            game.setStatus("finished");
            return "DRAW:" + playerMove;
        }

        changePlayer();

        Player next = game.getCurrentPlayer();
        String suggestedMove = getSuggestedMove(next);

        return "MOVE:" + playerMove + ":" + suggestedMove;
    }

    private String getSuggestedMove(Player player) {
        if (player instanceof Comp comp) {
            int myValue = "W".equals(comp.getColor()) ? 1 : 2;
            int oppValue = myValue == 1 ? 2 : 1;

            int[] move = comp.makeMove(board, myValue, oppValue);
            if (move != null) {
                return comp.getColor() + " (" + move[0] + "," + move[1] + ")";
            }
        }

        Comp virtualComp = new Comp();
        virtualComp.setColor(player.getColor());
        int myValue = "W".equals(player.getColor()) ? 1 : 2;
        int oppValue = myValue == 1 ? 2 : 1;

        int[] move = virtualComp.makeMove(board, myValue, oppValue);

        return player.getColor() + " (" + move[0] + "," + move[1] + ")";
    }

    private String formatSquareCoords(int[][] coords) {
        if (coords == null || coords.length == 0) return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < coords.length; i++) {
            if (i > 0) sb.append(",");
            sb.append(coords[i][0]).append(",").append(coords[i][1]);
        }
        return sb.toString();
    }

    private void render() {
        try {
            System.out.print("\033[H\033[2J");
            System.out.flush();
        } catch (Exception ignored) {
            for (int i = 0; i < 30; i++) System.out.println();
        }

        messageView.showMoves(String.join("\n", moveLogger.getMoves()));
        boardView.printBoard();
        messageView.showCurrentPlayer(game.getCurrentPlayer().getColor());
    }

    private boolean checkDraw() {
        int[][] field = board.getField();
        int size = board.getSize();
        for (int i = 0; i < size; i++) for (int j = 0; j < size; j++) if (field[i][j] == 0) return false;
        return true;
    }
    public Player getCurrentPlayer() {
        return game != null ? game.getCurrentPlayer() : null;
    }
    public String makeFirstComputerMove() {
        if (game == null || !(game.getCurrentPlayer() instanceof Comp)) {
            return "ERROR:Not computer's turn";
        }

        Comp comp = (Comp) game.getCurrentPlayer();
        int myValue = "W".equals(comp.getColor()) ? 1 : 2;
        int oppValue = myValue == 1 ? 2 : 1;

        int[] move = comp.makeMove(board, myValue, oppValue);
        if (move != null) {
            return makeMove(move[0], move[1]);
        }

        return "ERROR:Computer cannot make move";
    }


    public void changePlayer() {
        Player currentPlayer = game.getCurrentPlayer();
        if (game.getFirst().equals(currentPlayer))
            game.setCurrentPlayer(game.getSecond());
        else
            game.setCurrentPlayer(game.getFirst());
    }
}
