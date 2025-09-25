package org.ivk.resourceserver.controller;

import org.ivk.resourceserver.dto.CommandDto;
import org.ivk.resourceserver.dto.ResultCreateGame;
import org.ivk.resourceserver.dto.ResultMove;
import org.ivk.resourceserver.service.GameServerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/game")
public class GameController {
    private final GameServerService service;

    public GameController(GameServerService service) {
        this.service = service;
    }

    @PostMapping("/start-game")
    public ResponseEntity<ResultCreateGame> create(@RequestBody CommandDto dto) {
        return ResponseEntity.ok(service.startGame(dto.getCmd()));
    }
    @PostMapping("/move")
    public  ResponseEntity<ResultMove> move(@RequestBody CommandDto dto) {
        return ResponseEntity.ok(service.move(dto.getCmd()));
    }
}
