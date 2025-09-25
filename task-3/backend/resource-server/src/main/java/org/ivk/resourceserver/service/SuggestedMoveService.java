package org.ivk.resourceserver.service;

import org.ivk.entity.Board;
import org.ivk.entity.player.Comp;
import org.ivk.entity.player.Player;
import org.springframework.stereotype.Component;

@Component
public class SuggestedMoveService {

    public String getSuggestedMove(Player player, Board board) {
        Comp comp = player instanceof Comp ? (Comp) player : createVirtualComp(player);
        int myValue = "W".equals(player.getColor()) ? 1 : 2;
        int oppValue = myValue == 1 ? 2 : 1;

        int[] move = comp.makeMove(board, myValue, oppValue);
        return move != null ? player.getColor() + " (" + move[0] + "," + move[1] + ")" : "No move";
    }

    private Comp createVirtualComp(Player player) {
        Comp comp = new Comp();
        comp.setColor(player.getColor());
        return comp;
    }
}
