
package football_manager.modulos;

import football_manager.controladores.League_controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

public class League {
    private String name;
    private int teamCount;
    private ArrayList<Team> teams;
    private ArrayList<Match> matches;

    // Constructor
    public League(String name, int teamCount) {
        this.name = name;
        this.teamCount = teamCount;
        this.teams = new ArrayList<>();
        this.matches = new ArrayList<>();
    }

    // Getters
    public String getName() {
        return name;
    }

    public int getTeamCount() {
        return teamCount;
    }

    public ArrayList<Team> getTeams() {
        return teams;
    }

    public ArrayList<Match> getMatches() {
        return matches;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setTeamCount(int teamCount) {
        this.teamCount = teamCount;
    }

    // Methods
    public boolean addTeam(Team team) {
        if (teams.contains(team)) {
            return false;
        }
        if (teams.size() < teamCount) {
            teams.add(team);
            return true;
        }
        return false;
    }

    public void playAllMatches() {
        if (matches.isEmpty()) {
            generateMatches();
        }

        for (Match match : matches) {
            if (!match.isPlayed()) {
                match.playMatch();
            }
        }
    }

    private void generateMatches() {
        matches.clear();

        for (int i = 0; i < teams.size(); i++) {
            for (int j = i + 1; j < teams.size(); j++) {
                // Home and away matches
                matches.add(new Match(teams.get(i), teams.get(j)));
                matches.add(new Match(teams.get(j), teams.get(i)));
            }
        }
    }

    public Team getTeamWithMostGoalsFor() {
        return Collections.max(teams, Comparator.comparingInt(this::getGoalsFor));
    }

    public Team getTeamWithMostGoalsAgainst() {
        return Collections.max(teams, Comparator.comparingInt(this::getGoalsAgainst));
    }

    public int getGoalsFor(Team team) {
        int goals = 0;
        for (Match match : matches) {
            if (match.isPlayed() && match.getHomeTeam().equals(team)) {
                goals += match.getHomeGoals();
            } else if (match.isPlayed() && match.getAwayTeam().equals(team)) {
                goals += match.getAwayGoals();
            }
        }
        return goals;
    }

    public int getGoalsAgainst(Team team) {
        int goals = 0;
        for (Match match : matches) {
            if (match.isPlayed() && match.getHomeTeam().equals(team)) {
                goals += match.getAwayGoals();
            } else if (match.isPlayed() && match.getAwayTeam().equals(team)) {
                goals += match.getHomeGoals();
            }
        }
        return goals;
    }

    public int getPoints(Team team) {
        int points = 0;
        for (Match match : matches) {
            if (!match.isPlayed()) continue;

            if (match.getHomeTeam().equals(team)) {
                if (match.getHomeGoals() > match.getAwayGoals()) {
                    points += 3;
                } else if (match.getHomeGoals() == match.getAwayGoals()) {
                    points += 1;
                }
            } else if (match.getAwayTeam().equals(team)) {
                if (match.getAwayGoals() > match.getHomeGoals()) {
                    points += 3;
                } else if (match.getAwayGoals() == match.getHomeGoals()) {
                    points += 1;
                }
            }
        }
        return points;
    }

    public int getGoalDifference(Team team) {
        return getGoalsFor(team) - getGoalsAgainst(team);
    }

    public int getMatchesPlayed(Team team) {
        int count = 0;
        for (Match match : matches) {
            if (match.isPlayed() &&
                    (match.getHomeTeam().equals(team) || match.getAwayTeam().equals(team))) {
                count++;
            }
        }
        return count;
    }


    public static int getTeamCount(ArrayList<Team> teams, Scanner scanner) {
        int teamCount;
        do {
            teamCount = scanner.nextInt();
            scanner.nextLine();
            if (teamCount % 2 != 0 || teamCount < 2 || teamCount > teams.size()) {
            }
        } while (teamCount % 2 != 0 || teamCount < 2 || teamCount > teams.size());
        return teamCount;
    }

    public static void assignTeamsToLeague(League league, ArrayList<Team> teams, int teamCount, Scanner scanner) {
        for (int i = 0; i < teamCount; i++) {
            int teamNum = getTeamSelection(scanner, teams, league);
            league.addTeam(teams.get(teamNum - 1));
        }
    }


    private static int getTeamSelection(Scanner scanner, ArrayList<Team> teams, League league) {
        int teamNum;
        do {
            teamNum = scanner.nextInt();
            scanner.nextLine();

            if (teamNum < 1 || teamNum > teams.size() || league.getTeams().contains(teams.get(teamNum - 1))) {

            }
        } while (teamNum < 1 || teamNum > teams.size() || league.getTeams().contains(teams.get(teamNum - 1)));

        return teamNum;
    }



    public static void sortTeamsByStandings(League league) {
        league.getTeams().sort((t1, t2) -> {
            int pointsCompare = Integer.compare(league.getPoints(t2), league.getPoints(t1));
            if (pointsCompare != 0) return pointsCompare;

            int gdCompare = Integer.compare(league.getGoalDifference(t2), league.getGoalDifference(t1));
            if (gdCompare != 0) return gdCompare;

            return Integer.compare(league.getGoalsFor(t2), league.getGoalsFor(t1));
        });
    }


    public static void displayTeamStats(League league) {
        for (Team team : league.getTeams()) {
            int points = league.getPoints(team);
            int matchesPlayed = league.getMatchesPlayed(team);
            int goalsFor = league.getGoalsFor(team);
            int goalsAgainst = league.getGoalsAgainst(team);
        }
    }


}