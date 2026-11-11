package com.wongmb.connect4;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class GameRulesTest {

    @Test
    void dropPieceUsesGravityInSingleColumn() {
        char[][] board = GameRules.emptyBoard();

        GameRules.MoveResult firstMove = GameRules.dropPiece(board, 3, 'R');
        board = firstMove.board();

        assertEquals('R', board[5][3]);
        assertEquals('.', board[4][3]);
        assertFalse(firstMove.won());
        assertFalse(firstMove.draw());

        GameRules.MoveResult secondMove = GameRules.dropPiece(board, 3, 'Y');
        board = secondMove.board();

        assertEquals('R', board[5][3]);
        assertEquals('Y', board[4][3]);
        assertFalse(secondMove.won());
        assertFalse(secondMove.draw());
    }

    @Test
    void dropPieceDetectsHorizontalWin() {
        char[][] board = GameRules.emptyBoard();

        for (int col = 0; col < 4; col++) {
            GameRules.MoveResult result = GameRules.dropPiece(board, col, 'R');
            board = result.board();
            if (col == 3) {
                assertTrue(result.won());
                assertFalse(result.draw());
            } else {
                assertFalse(result.won());
            }
        }
    }

    @Test
    void dropPieceDetectsVerticalWin() {
        char[][] board = GameRules.emptyBoard();

        for (int i = 0; i < 3; i++) {
            GameRules.MoveResult result = GameRules.dropPiece(board, 3, 'R');
            board = result.board();
            assertFalse(result.won());
        }

        GameRules.MoveResult winningMove = GameRules.dropPiece(board, 3, 'R');
        board = winningMove.board();

        assertTrue(winningMove.won());
        assertFalse(winningMove.draw());
        assertEquals('R', board[2][3]);
    }

    @Test
    void checkWinDetectsDiagonalWins() {
        char[][] risingDiagonal = GameRules.emptyBoard();
        risingDiagonal[5][0] = 'R';
        risingDiagonal[4][1] = 'R';
        risingDiagonal[3][2] = 'R';
        risingDiagonal[2][3] = 'R';

        assertTrue(GameRules.checkWin(risingDiagonal, 2, 3, 'R'));

        char[][] fallingDiagonal = GameRules.emptyBoard();
        fallingDiagonal[5][6] = 'R';
        fallingDiagonal[4][5] = 'R';
        fallingDiagonal[3][4] = 'R';
        fallingDiagonal[2][3] = 'R';

        assertTrue(GameRules.checkWin(fallingDiagonal, 2, 3, 'R'));
    }

    @Test
    void dropPieceRejectsInvalidMovesAndBoards() {
        char[][] board = GameRules.emptyBoard();

        assertThrows(IllegalArgumentException.class, () -> GameRules.dropPiece(board, -1, 'R'));
        assertThrows(IllegalArgumentException.class, () -> GameRules.dropPiece(board, GameRules.COLS, 'R'));
        assertThrows(IllegalArgumentException.class, () -> GameRules.dropPiece(board, 0, 'X'));
        assertThrows(IllegalArgumentException.class, () -> GameRules.dropPiece(null, 0, 'R'));

        char[][] invalidBoard = new char[GameRules.ROWS - 1][GameRules.COLS];
        assertThrows(IllegalArgumentException.class, () -> GameRules.dropPiece(invalidBoard, 0, 'R'));
    }

    @Test
    void dropPieceRejectsFullColumns() {
        char[][] board = GameRules.emptyBoard();

        for (int i = 0; i < GameRules.ROWS; i++) {
            board = GameRules.dropPiece(board, 0, 'R').board();
        }

        char[][] fullBoard = board;
        assertThrows(IllegalArgumentException.class, () -> GameRules.dropPiece(fullBoard, 0, 'Y'));
    }

    @Test
    void drawIsDetectedWhenTheFinalMoveFillsTheBoardWithoutWinning() {
        char[][] board = new char[][] {
            {'Y', 'R', 'R', 'Y', 'R', 'R', 'Y'},
            {'Y', 'Y', 'R', 'Y', 'R', 'Y', 'Y'},
            {'R', 'R', 'Y', 'R', 'Y', 'R', 'R'},
            {'R', 'Y', 'Y', 'R', 'Y', 'R', 'Y'},
            {'R', 'R', 'Y', 'Y', 'R', 'R', 'R'},
            {'Y', 'Y', 'R', 'Y', 'R', 'Y', 'R'}
        };

        board[0][0] = '.';

        GameRules.MoveResult result = GameRules.dropPiece(board, 0, 'R');
        board = result.board();

        assertFalse(result.won());
        assertTrue(result.draw());
        assertFalse(GameRules.checkWin(board, 0, 0, 'R'));
        assertTrue(GameRules.isBoardFull(board));
    }
}
