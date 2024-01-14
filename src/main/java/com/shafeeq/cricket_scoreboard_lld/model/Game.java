package com.shafeeq.cricket_scoreboard_lld.model;

import java.util.HashMap;
import java.util.Map;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Game {
    private int gameId;

    @Builder.Default
    private GameScoreboard gameScoreboard = GameScoreboard.builder().build();

    @Builder.Default
    private Map<Integer, Team> teams = new HashMap<>();
    private int totalPlayers;
    private int totalOvers;
}
