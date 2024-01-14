package com.shafeeq.cricket_scoreboard_lld.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shafeeq.cricket_scoreboard_lld.model.Game;
import com.shafeeq.cricket_scoreboard_lld.model.GameScoreboard;
import com.shafeeq.cricket_scoreboard_lld.model.Player;
import com.shafeeq.cricket_scoreboard_lld.model.Team;
import com.shafeeq.cricket_scoreboard_lld.model.TeamScore;
import com.shafeeq.cricket_scoreboard_lld.model.request.RunRequest;
import com.shafeeq.cricket_scoreboard_lld.model.response.RunResult;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RunGameService {
    
    @Autowired
    private GameService gameService;

    public RunResult runGame(RunRequest runRequest) {
        RunResult runResult = RunResult
            .builder()
            .build();
        Game game = Game.builder().build();
        List<String> strCommands = runRequest.getRequests();
        int totalCommands = strCommands.size();
        String playerCountStr = strCommands.get(0).replaceAll("[^0-9]", "");
        String overCountStr = strCommands.get(1).replaceAll("[^0-9]", "");
        try {
            game.setTotalPlayers(Integer.parseInt(playerCountStr));
            game.setTotalOvers(Integer.parseInt(overCountStr));
        } catch(NumberFormatException nfe) {
            log.error("unable to parse player count and overs from input: {}, {}", playerCountStr, overCountStr, nfe);
        }
        game = gameService.saveOrUpdateGame(game);
        // log.info("game created!! Game: {}", game);
        List<String> team1str = strCommands.subList(2, totalCommands);
        Team team = buildTeam(game, team1str);
        // log.info("built game and team: {}", game);
        List<String> oversStr = strCommands.subList(game.getTotalPlayers() + 4, totalCommands);
        int battingOrderTeam2Idx = 4 + game.getTotalPlayers() + playInning(game, team, oversStr);
        GameScoreboard gameScoreBoard = game.getGameScoreboard();
        gameScoreBoard.setChasing(true);
        gameScoreBoard.setTargetRun(team.getTeamScore().getTotalScore() + 1);
        gameScoreBoard.setTotalWickets(team.getTeamScore().getTotalWickets());
        strCommands = strCommands.subList(battingOrderTeam2Idx, strCommands.size());
        team = buildTeam(game, strCommands);
        // log.info("game created!! Game: {}", game);
        playInning(game, team, strCommands.subList(game.getTotalPlayers() + 2, strCommands.size()));
        return runResult;
    }

    private Team buildTeam(Game game, List<String> strCommands) {
        // log.info("creating team.....: {}", strCommands);
        String teamIdStr = strCommands.get(0).replaceAll("[^0-9]", "");
        Team team = Team.builder().build();
        try {
            int teamId = Integer.parseInt(teamIdStr);
            team.setTeamId(teamId);
            game.getTeams().put(teamId, team);
        } catch(NumberFormatException nfe) {
            log.error("unable to parse teamId from input: {}", teamIdStr, nfe);
        }
        List<String> teamPlayers = strCommands.subList(1, game.getTotalPlayers() + 1);
        // log.info("creating team players.....: {}", teamPlayers);
        for (String str : teamPlayers) {
            team.getPlayers().add(Player.builder().playerName(str).build());
        }
        return team;
    }

    private int playInning(Game game, Team team, List<String> oversStr) {
        TeamScore currTeamScore = team.getTeamScore();
        Player strikeOpener = team.getPlayers().get(0);
        strikeOpener.setPlaying(true);
        currTeamScore.setStrikeBatsman(strikeOpener);
        Player nonStrikeOpener = team.getPlayers().get(1);
        nonStrikeOpener.setPlaying(true);
        currTeamScore.setNonStrikeBatsman(nonStrikeOpener);
        int lineNmbr = 0;
        for (; lineNmbr < oversStr.size() + 1; lineNmbr++) {
            GameScoreboard gameScoreboard = game.getGameScoreboard();
            if (gameScoreboard.isChasing()) {
                if (gameScoreboard.getTargetRun() <= currTeamScore.getTotalScore()) {
                    printScoreBoard(team);
                    log.info("Result: Team 2 won by {} wickets", 11 - currTeamScore.getTotalWickets());
                    break;
                } else if (currTeamScore.getStrikeBatsman() == null) {
                    printScoreBoard(team);
                    log.info("Result: Team 1 won by {} runs", game.getTeams().get(1).getTeamScore().getTotalScore() - currTeamScore.getTotalScore());
                    break;
                } else if (currTeamScore.getTotalBalls() >= game.getTotalOvers() * 6) {
                    printScoreBoard(team);
                    log.info("Result: Team 1 won by {} runs", game.getTeams().get(1).getTeamScore().getTotalScore() - currTeamScore.getTotalScore());
                    break;
                } else if (lineNmbr >= oversStr.size()) {
                    printScoreBoard(team);
                    log.info("Result: Team 1 won by {} runs", game.getTeams().get(1).getTeamScore().getTotalScore() - currTeamScore.getTotalScore());
                    break;
                }
            }
            String str = oversStr.get(lineNmbr);
            log.info("iterating over each over  {}", str);
            if (str.contains("Over") || str.contains("Batting Order for team")) {
                // log.info("New Over Start. Printing score. Over: {}", str);
                printScoreBoard(team);
                if (str.contains("Batting Order for team")) {
                    break;
                } else {
                    continue;
                }
            }
            if ("W".equals(str)) {
                // log.info("wicket!!! {}", currTeamScore);
                Player strikeBatsMan = currTeamScore.getStrikeBatsman();
                strikeBatsMan.setTotalBalls(strikeBatsMan.getTotalBalls() + 1);
                strikeBatsMan.setOut(true);
                strikeBatsMan.setPlaying(false);
                currTeamScore.setTotalBalls(currTeamScore.getTotalBalls() + 1);
                currTeamScore.setTotalWickets(currTeamScore.getTotalWickets() + 1);
                currTeamScore.setStrikeBatsman(getNextBatsMan(team));
            } else if ("Wd".equals(str)) {
                // log.info("wide!!! {}", currTeamScore);
                currTeamScore.setExtras(currTeamScore.getExtras() + 1);
                currTeamScore.setTotalScore(currTeamScore.getTotalScore() + 1);
            } else {
                // log.info("run!!! {}", currTeamScore);
                int run = Integer.parseInt(str);
                Player strikePlayer = currTeamScore.getStrikeBatsman();
                strikePlayer.setTotalRun(strikePlayer.getTotalRun() + run);
                strikePlayer.setTotalBalls(strikePlayer.getTotalBalls() + 1);
                currTeamScore.setTotalScore(currTeamScore.getTotalScore() + run);
                currTeamScore.setTotalBalls(currTeamScore.getTotalBalls() + 1);
                if (1 == run || 3 == run) {
                    currTeamScore.changeStrike();
                } else if (4 == run) {
                    strikePlayer.setFours(strikePlayer.getFours() + 1);
                } else if (6 == run) {
                    strikePlayer.setSixes(strikePlayer.getSixes() + 1);
                }
            }
        }
        return lineNmbr;
    }

    private void printScoreBoard(Team team) {
        TeamScore currTeamScore = team.getTeamScore();
        log.info("Player Name | Score | 4s | 6s | Balls");
        for (Player player : team.getPlayers()) {
            log.info("{} | {} | {} | {} | {}", player.isPlaying() ? player.getPlayerName() + "*"  : player.getPlayerName(), player.getTotalRun(), player.getFours(), player.getSixes(), player.getTotalBalls());
        }
        log.info("Total: {}/{}", currTeamScore.getTotalScore(), currTeamScore.getTotalWickets());
        if (currTeamScore.getTotalBalls() % 6 != 0) {
            log.info("Overs: {}.{}", currTeamScore.getTotalBalls() / 6, currTeamScore.getTotalBalls() % 6);
        } else {
            log.info("Overs: {}", currTeamScore.getTotalBalls() / 6);
        }
        currTeamScore.changeStrike();
    }

    private Player getNextBatsMan(Team team) {
        for (Player player : team.getPlayers()) {
            if (!player.isOut() && !player.isPlaying()) {
                player.setPlaying(true);
                return player;
            }
        }
        return null;
    }
}
