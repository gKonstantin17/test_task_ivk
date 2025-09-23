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

        autoPlayForComp();
    }

    public void makeMove(int x, int y) {
        if (game == null) {
            messageView.errorGameNotStarted();
            return;
        }

        if (!moveExecutor.execute(x, y)) {
            messageView.errorInvalidMove();
            return;
        }

        // логирование хода текущего игрока
        moveLogger.log(game.getCurrentPlayer().getColor() + " (" + x + "," + y + ")");

        render();

        WinResult result = winChecker.checkWin(game.getCurrentPlayer(), x, y);
        if (result.isWin()) {
            messageView.winGame(game.getCurrentPlayer().getColor());
            messageView.showSquareCoords(result.getSquareCoords());
            for (int[] coord : result.getSquareCoords()) {
                System.out.println("  (" + coord[0] + "," + coord[1] + ")");
            }
            game.setStatus("finished");
            return;
        }

        if (checkDraw()) {
            messageView.drawGame();
            game.setStatus("finished");
            return;
        }

        changePlayer();

        Player next = game.getCurrentPlayer();
        if (next instanceof Comp comp) {
            int myValue = "W".equals(comp.getColor()) ? 1 : 2;
            int oppValue = myValue == 1 ? 2 : 1;

            int[] move = comp.makeMove(board, myValue, oppValue); // метод из класса Comp
            if (move != null) {
                // рекурсивно делаем ход компьютера
                makeMove(move[0], move[1]);
            }
        }
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

    public void changePlayer() {
        Player currentPlayer = game.getCurrentPlayer();
        if (game.getFirst().equals(currentPlayer))
            game.setCurrentPlayer(game.getSecond());
        else
            game.setCurrentPlayer(game.getFirst());
    }

    private void autoPlayForComp() {
        while (game.getStatus().equals("started") && game.getCurrentPlayer() instanceof Comp) {
            Comp comp = (Comp) game.getCurrentPlayer();

            int myValue = "W".equals(comp.getColor()) ? 1 : 2;
            int oppValue = myValue == 1 ? 2 : 1;
            int[] move = comp.makeMove(board, myValue, oppValue);

            if (move != null) {
                if (!moveExecutor.execute(move[0], move[1])) break; // на случай ошибки
                moveLogger.log(comp.getColor() + " (" + move[0] + "," + move[1] + ")");
                render();

                WinResult result = winChecker.checkWin(comp, move[0], move[1]);
                if (result.isWin()) {
                    messageView.winGame(game.getCurrentPlayer().getColor());
                    messageView.showSquareCoords(result.getSquareCoords());
                    game.setStatus("finished");
                    return;
                }

                if (checkDraw()) {
                    messageView.drawGame();
                    game.setStatus("finished");
                    return;
                }

                changePlayer();
            }

            try {
                Thread.sleep(500); // задержка 0.5 секунд
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
