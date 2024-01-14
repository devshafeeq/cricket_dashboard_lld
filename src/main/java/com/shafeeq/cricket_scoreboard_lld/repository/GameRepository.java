package com.shafeeq.cricket_scoreboard_lld.repository;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.shafeeq.cricket_scoreboard_lld.model.Game;

@Repository
public class GameRepository {
    private Map<Integer, Game> gameMap = new HashMap<>();
    private Game lastGame = Game.builder().gameId(-1).build();

    public Game createOrUpdateGame(Game game) {
        game.setGameId(lastGame.getGameId() + 1);
        lastGame = game;
        gameMap.put(game.getGameId(), game);
        return game;
    }

    public Game getgameById(int gameId) {
        return gameMap.getOrDefault(gameId, Game.builder().gameId(-2).build());
    }

}
