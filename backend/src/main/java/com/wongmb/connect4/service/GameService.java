package com.wongmb.connect4.service;

import com.wongmb.connect4.GameRules;
import com.wongmb.connect4.model.Game;
import com.wongmb.connect4.repository.GameRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GameService {

    public static final String IN_PROGRESS = "IN_PROGRESS";
    public static final String DRAW = "DRAW";

    private final GameRepository gameRepository;

    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Transactional
    public Game createGame() {
        return gameRepository.save(new Game(encodeBoard(GameRules.emptyBoard()), 'R', IN_PROGRESS));
    }

    @Transactional(readOnly = true)
    public Game getGame(Long id) {
        return gameRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Game not found: " + id));
    }

    @Transactional
    public Game makeMove(Long id, int column) {
        Game game = getGame(id);
        if (!IN_PROGRESS.equals(game.getStatus())) {
            throw new IllegalStateException("Game is already over");
        }

        char[][] board = decodeBoard(game.getBoard());
        char player = game.getCurrentPlayer();
        GameRules.MoveResult result = GameRules.dropPiece(board, column, player);

        game.setBoard(encodeBoard(result.board()));
        if (result.won()) {
            game.setStatus(player == 'R' ? "RED_WON" : "YELLOW_WON");
        } else if (result.draw()) {
            game.setStatus(DRAW);
        } else {
            game.setCurrentPlayer(player == 'R' ? 'Y' : 'R');
        }

        return gameRepository.save(game);
    }

    private String encodeBoard(char[][] board) {
        StringBuilder encoded = new StringBuilder(GameRules.ROWS * GameRules.COLS);
        for (char[] row : board) {
            encoded.append(row);
        }
        return encoded.toString();
    }

    private char[][] decodeBoard(String encoded) {
        if (encoded == null || encoded.length() != GameRules.ROWS * GameRules.COLS) {
            throw new IllegalStateException("Stored game board is invalid");
        }

        char[][] board = new char[GameRules.ROWS][GameRules.COLS];
        for (int row = 0; row < GameRules.ROWS; row++) {
            for (int column = 0; column < GameRules.COLS; column++) {
                char cell = encoded.charAt(row * GameRules.COLS + column);
                if (cell != '.' && cell != 'R' && cell != 'Y') {
                    throw new IllegalStateException("Stored game board is invalid");
                }
                board[row][column] = cell;
            }
        }
        return board;
    }
}
