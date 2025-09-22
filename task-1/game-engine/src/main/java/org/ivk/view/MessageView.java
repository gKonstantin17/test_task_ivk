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
        System.out.println(logo);
        System.out.println("Для начала игры пишите 'GAME N, U1, U2'");
        System.out.println("например, 'GAME user W comp B'");
        System.out.println("N - размер квадрата игрового поля");
        System.out.println("U1, U2 - игроки  'user' (пользователь) или 'comp' (компьютер)");
        System.out.println("С цветами  'W' (белые), или 'B' (черные)");
        System.out.println("\n Для выхода пишите 'EXIT'");
    }
    public void startGame() {
        System.out.println("New game started");
    }
    public void help() {

    }
    public void incorrect() {
        System.out.println("Incorrect command");
    }
}
