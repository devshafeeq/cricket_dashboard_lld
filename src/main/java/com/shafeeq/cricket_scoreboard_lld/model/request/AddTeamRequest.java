package com.shafeeq.cricket_scoreboard_lld.model.request;

import java.util.List;

import lombok.Data;

@Data
public class AddTeamRequest {
    private List<String> playerOrder;
    private int gameId;
    private int teamId;
}
