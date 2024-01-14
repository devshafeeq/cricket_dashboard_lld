package com.shafeeq.cricket_scoreboard_lld.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Team {
    private int teamId;

    @Builder.Default
    private TeamScore teamScore = TeamScore.builder().build();

    @Builder.Default
    private List<Player> players = new ArrayList<>();
    private Player captain;
}
