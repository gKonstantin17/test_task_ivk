package org.ivk.resourceserver.dto;

import lombok.Data;

import java.util.List;

@Data
public class ResultMove {
    String playerMove;
    String nextMove;
    String result;
    List<int[]> winSquare;
}
