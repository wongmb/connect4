package com.wongmb.connect4;
import java.util.Arrays;

public class GameRules {
    public static final int ROWS = 6;
    public static final int COLS = 7;

    public static char[][] emptyBoard() {
        char[][] board = new char[ROWS][COLS];

        for (int r = 0; r < ROWS; r++){
            Arrays.fill(board[r], '.');
        }
        
        return board;
    }

    public record MoveResult(char[][] board, boolean won, boolean draw) {}

    public static MoveResult dropPiece(char[][] board, int col, char player) {
        if (col < 0 || col >= COLS) {
            throw new IllegalArgumentException("Invalid column");
        }

        if (player != 'R' && player != 'C') {
            throw new IllegalArgumentException("Invalid player");
        }

        if (board == null || board.length != ROWS) {
            throw new IllegalArgumentException("Invalid board");
        }

        int placedRow = -1;
        
        for (int r = ROWS - 1; r >= 0; r--){
            if (board[r][col] == '.') {
                board[r][col] = player;
                placedRow = r;
                break;
            }
        }
        
        if (placedRow == -1) {
            throw new IllegalArgumentException("Column is full");
        }

        boolean won = checkWin(board, placedRow, col, player);
        boolean draw = !won && isBoardFull(board);

        return new MoveResult(board, won, draw);
    }

    public static boolean checkWin(char[][] board, int row, int col, char player) {
        int[][] directions = {
            {0, 1},
            {1, 0},
            {1, 1},
            {1, -1}
        };
        
        for (int[] direction : directions) {
            int dr = direction[0];
            int dc = direction[1];

            int count = 1 + countPieces(board, row, col, player, dr, dc) + countPieces(board, row, col, player, dr*-1, dc*-1);

            if (count >= 4) {
                return true;
            }
        }

        return false;
    }

    public static int countPieces(char[][] board, int row, int col, char player, int dr, int dc) {        
        int count = 0;
        int r = row + dr;
        int c = col + dc;

        while (r >= 0 && c >= 0 && r < ROWS && c < COLS) {
            if (board[r][c] != player) {
                break;
            }

            count++; 
            
            r += dr;
            c += dc;
        }

        return count;
    }

    public static boolean isBoardFull(char[][] board) {
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                if (board[r][c] == '.') {
                    return false;
                }
            }
        }

        return true;
    }
}
