package com.shafeeq.cricket_scoreboard_lld.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shafeeq.cricket_scoreboard_lld.model.Game;
import com.shafeeq.cricket_scoreboard_lld.model.Player;
import com.shafeeq.cricket_scoreboard_lld.model.Team;
import com.shafeeq.cricket_scoreboard_lld.model.request.AddTeamRequest;
import com.shafeeq.cricket_scoreboard_lld.repository.GameRepository;

@Service
public class GameService {
    
    @Autowired
    private GameRepository gameRepository;
    
    public Game saveOrUpdateGame(Game game) {
        return gameRepository.createOrUpdateGame(game);
    }

    public Team addTeam(AddTeamRequest addTeamRequest) {
        Game game = gameRepository.getgameById(addTeamRequest.getGameId());
        Team team = Team.builder().teamId(addTeamRequest.getTeamId()).build();
        game.getTeams().put(addTeamRequest.getTeamId(), team);
        int playerId = 0;
        List<Player> playerOrder = new ArrayList<>();
        for (String str: addTeamRequest.getPlayerOrder()) {
            Player player = Player
                .builder()
                .playerId(playerId++)
                .playerName(str)
                .build();
            playerOrder.add(player);
        }
        team.setPlayers(playerOrder);
        return team;
    }
}
