package org.ivk.resourceserver.controller;

import org.ivk.resourceserver.service.GameService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/game")
public class GameController {
    private final GameService service;

    public GameController(GameService service) {
        this.service = service;
    }

    @PostMapping("/start-game")
    public ResponseEntity<?> create() {
        return ResponseEntity.ok("create");
    }
    @PostMapping("/move")
    public ResponseEntity<?> move() {
        return ResponseEntity.ok("create");
    }
}
