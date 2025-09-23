package org.ivk.factory;

import org.ivk.entity.player.Comp;
import org.ivk.entity.player.Player;
import org.ivk.entity.player.User;

public class PlayerFactory {
    public static Player createPlayer(String type, String color) {
        if (type == null || color == null) {
            throw new IllegalArgumentException();
        }
        Player player = switch (type.toLowerCase()) {
            case "user" -> new User();
            case "comp" -> new Comp();
            default -> throw new IllegalArgumentException();
        };

        player.setType(type);
        player.setColor(color);
        return player;
    }
}
