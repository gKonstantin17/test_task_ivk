package org.ivk.service;

import java.util.ArrayList;
import java.util.List;

public class MoveLogger {
    private final List<String> moves = new ArrayList<>();

    public void log(String move) {
        moves.add(move);
    }

    public List<String> getMoves() {
        return List.copyOf(moves);
    }

    public void clear() {
        moves.clear();
    }
}
