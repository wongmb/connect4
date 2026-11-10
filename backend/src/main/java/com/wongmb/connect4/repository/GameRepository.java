package com.wongmb.connect4.repository;

import com.wongmb.connect4.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, Long> {
}