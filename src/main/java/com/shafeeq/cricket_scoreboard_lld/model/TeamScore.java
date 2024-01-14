package com.shafeeq.cricket_scoreboard_lld.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TeamScore {
    private Player strikeBatsman;
    private Player nonStrikeBatsman;
    private int totalScore;
    private int totalWickets;
    private int totalBalls;
    private int extras;

    public void changeStrike() {
        Player tmPlayer = this.nonStrikeBatsman;
        this.nonStrikeBatsman = strikeBatsman;
        strikeBatsman = tmPlayer;
    }

}
