package com.shafeeq.cricket_scoreboard_lld.model;

import java.util.Map;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GameScoreboard {
    private Map<Integer, TeamScore> teamScoreMap;
    private boolean isChasing;
    private int targetRun;
    private int totalWickets;
    private Team winner;
    private int wonByRun;
    private int wonByWickets;
}
