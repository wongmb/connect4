package com.wongmb.connect4.controller;

import com.wongmb.connect4.model.Game;
import com.wongmb.connect4.service.GameService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/games")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping
    public Game createGame() {
        return gameService.createGame();
    }

    @GetMapping("/{id}")
    public Game getGame(@PathVariable Long id) {
        return gameService.getGame(id);
    }

    public record MoveRequest(int column) {}

    @PostMapping("/{id}/moves")
    public Game makeMove(
        @PathVariable Long id,
        @RequestBody MoveRequest request
    ) {
        return gameService.makeMove(id, request.column());
    }
}