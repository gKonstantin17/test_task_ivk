package org.ivk.service;

import org.ivk.entity.Board;
import org.ivk.entity.Game;
import org.ivk.entity.player.Player;
import org.ivk.view.BoardView;

public class GameService {
    private Game game;
    private BoardView boardView;
    private Board board;

    private MoveLogger moveLogger;
    private MoveExecutor moveExecutor;
    private IWinChecker winChecker;

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
        render();
    }

    public void makeMove(int x, int y) {
        if (game == null) {
            System.out.println("Ошибка: Игра не начата. Сначала выполните команду GAME");
            return;
        }

        if (!moveExecutor.execute(x, y)) {
            System.out.println("Ход невозможен: клетка занята или координаты неверны");
            return;
        }

        // логируем ход текущего игрока
        moveLogger.log(game.getCurrentPlayer().getColor() + " (" + x + "," + y + ")");

        render();

        // проверка победы
        WinResult result = winChecker.checkWin(game.getCurrentPlayer(), x, y);
        if (result.isWin()) {
            System.out.println("Победил игрок " + game.getCurrentPlayer().getColor());
            System.out.println("Координаты квадрата:");
            for (int[] coord : result.getSquareCoords()) {
                System.out.println("  (" + coord[0] + "," + coord[1] + ")");
            }
            game.setStatus("finished");
            return;
        }

        if (checkDraw()) {
            System.out.println("Ничья!");
            game.setStatus("finished");
            return;
        }

        changePlayer();
    }

    private void render() {
        // если в CMD/терминале поддерживаются ANSI, очищаем по-умному; иначе печатаем много строк
        try {
            System.out.print("\033[H\033[2J");
            System.out.flush();
        } catch (Exception ignored) {
            for (int i = 0; i < 30; i++) System.out.println();
        }

        System.out.println("Ходы:");
        for (String m : moveLogger.getMoves()) {
            System.out.println("  " + m);
        }
        System.out.println();
        boardView.printBoard();
        System.out.println("Ход игрока: " + game.getCurrentPlayer().getColor());
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

    // геттеры для тестов/интеграции
    public Game getGame() { return game; }
    public MoveLogger getMoveLogger() { return moveLogger; }
}
