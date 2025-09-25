package org.ivk.service;

import org.ivk.entity.player.Player;

public interface IWinChecker {
    WinResult checkWin(Player player, int lastX, int lastY);
}
