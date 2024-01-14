package com.shafeeq.cricket_scoreboard_lld.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shafeeq.cricket_scoreboard_lld.model.Game;
import com.shafeeq.cricket_scoreboard_lld.model.GameScoreboard;
import com.shafeeq.cricket_scoreboard_lld.model.Team;
import com.shafeeq.cricket_scoreboard_lld.model.request.AddTeamRequest;
import com.shafeeq.cricket_scoreboard_lld.model.request.RunRequest;
import com.shafeeq.cricket_scoreboard_lld.model.response.RunResult;
import com.shafeeq.cricket_scoreboard_lld.service.GameService;
import com.shafeeq.cricket_scoreboard_lld.service.RunGameService;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
@RequestMapping("game")
public class GameController {
    
    @Autowired
    private GameService gameService;

    @Autowired
    private RunGameService runGameService;

    @PostMapping("runMock")
    public RunResult runMockGame(@RequestBody RunRequest runRequest) {
        log.info("running game... {}", runRequest);
        return runGameService.runGame(runRequest);
    }

    @GetMapping("scoreboard")
    public GameScoreboard getGameScoreboard() {
        return null;
    }

    @PutMapping("start")
    public Game startGame(@RequestBody Game game) {
        log.info("starting game: {}", game);
        Game startedGame = gameService.saveOrUpdateGame(game);
        log.info("started game: {}", startedGame);
        return startedGame;
    }

    @PostMapping("addTeam")
    public Team addTeamToGame(@RequestBody AddTeamRequest addTeamRequest) {
        Team team = gameService.addTeam(addTeamRequest);
        return team;
    }
    
}
