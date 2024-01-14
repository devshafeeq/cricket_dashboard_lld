package com.shafeeq.cricket_scoreboard_lld.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Player {
    
    private int playerId;
    private String playerName;
    private int totalRun;
    private int totalBalls;
    private int fours;
    private int sixes;
    private boolean isOut;
    private boolean isPlaying;

}
