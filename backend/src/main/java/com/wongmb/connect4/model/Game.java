
package com.wongmb.connect4.model;

import jakarta.persistence.*;

@Entity
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String board;
    private char currentPlayer;
    private String status;

    public Game() {}

    public Game(String board, char currentPlayer, String status) {
        this.board = board;
        this.currentPlayer = currentPlayer;
        this.status = status;
    }

    public String getBoard() {
        return this.board;
    }

    public void setBoard(String board) {
        this.board = board;
    }

    public Long getId() {
        return this.id;
    }

    public char getCurrentPlayer() {
        return this.currentPlayer;
    }

    public void setCurrentPlayer(char currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
