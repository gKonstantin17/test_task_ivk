package org.ivk.view;

public class MessageView {
    public void startApp() {
        String logo =
        " .|'''.|                                                   \n" +
        " ||..  '    ... .  ... ...   ....   ... ..    ....   ....  \n" +
        "  ''|||.  .'   ||   ||  ||  '' .||   ||' '' .|...|| ||. '  \n" +
        ".     '|| |.   ||   ||  ||  .|' ||   ||     ||      . '|.. \n" +
        "|'....|'  '|..'||   '|..'|. '|..'|' .||.     '|...' |'..|' \n" +
        "               ||                                           \n" +
        "              ''''                                          \n";
        String message = "Команды:\n" +
                "HELP - как играть\n" +
                "EXIT - выйти";
        System.out.println(logo);
        System.out.println(message);
    }
    public String startGame() {
        String message = "New game started";
        System.out.println(message);
        return message;
    }
    public void help() {
        String message = "Игра \"Квадраты\"\n" +
                "Дано поле NxN клеток. Есть два игрока, которые играют белыми и черными фишками (у каждого игрока свой цвет). Все фишки одинаковы.\n" +
                "Игроки ходят по очереди, ставя фишку своего цвета в любую свободную клетку. Первый ход по договоренности. Выигрывает тот игрок, который первым выставит на поле квадрат из фишек своего цвета. Квадрат может быть ориентирован как угодно относительно доски. В случае, если все поля заполнены, но ни один игрок не смог построить квадрат, объявляется ничья. \n" +
                "\n" +
                "Команды:\n" +
                "GAME - начать новую игру.\n" +
                "   Формат команды: \"GAME N, U1, U2\", где\n" +
                "   - N - размер доски (целое число > 2),\n" +
                "   - U1 и U2 - параметры 1го и 2го игрока.\n" +
                "   Формат параметров игрока: \"TYPE C\", где\n" +
                "   TYPE - тип игрока, возможные значения: 'user' (пользователь) или 'comp' (компьютер),\n" +
                "   C - цвет фишек, возможные значения: 'W' (белые), или 'B' (черные).\n" +
                "Например, GAME 7 USER W COMP B\n" +
                "\n" +
                "MOVE - сделать ход.\n" +
                "   Формат команды: \"MOVE X, Y\", где\n" +
                "   X и Y - целые числа, координаты клетки.\n" +
                "Например, MOVE 4, 3\n";
        System.out.println(message);
    }
    public String incorrect() {
        String message = "Incorrect command";
        System.out.println(message);
        return message;
    }
    public String winGame(String color) {
        String message = "Game finished."+ color + " wins!";
        System.out.println(message);
        return message;
    }
    public String drawGame() {
        String message = "Game finished. Draw";
        System.out.println(message);
        return message;
    }

    public String errorGameNotStarted() {
        String message = "Ошибка: Игра не начата. Сначала выполните команду GAME";
        System.out.println(message);
        return message;
    }

    public String errorInvalidMove() {
        String message = "Ход невозможен: клетка занята или координаты неверны";
        System.out.println(message);
        return message;
    }

    public void showMoves(String moves) {
        System.out.println("Ходы:\n" + moves);
    }

    public void showCurrentPlayer(String color) {
        System.out.println("Ход игрока: " + color);
    }
    public void showSquareCoords(int[][] coords) {
        String message = "";
        System.out.println("\nКоординаты квадрата:");
        for (int[] coord : coords) {
            System.out.println("  (" + coord[0] + "," + coord[1] + ")");
        }
    }

    public void errorSameColor() {
        System.out.println("Ошибка: игроки не могут быть одного цвета!");
    }

    public String errorGameOver() {
        String message = "Ошибка: Игра уже закончена";
        System.out.println(message);
        return message;
    }
}
