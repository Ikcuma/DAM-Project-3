e
package football_manager;

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


    public void showStandings() {
        // Sort teams by points then goal difference
        teams.sort((t1, t2) -> {
            int pointsCompare = Integer.compare(getPoints(t2), getPoints(t1));
            if (pointsCompare != 0) return pointsCompare;

            int gdCompare = Integer.compare(getGoalDifference(t2), getGoalDifference(t1));
            if (gdCompare != 0) return gdCompare;

            return Integer.compare(getGoalsFor(t2), getGoalsFor(t1));
        });

        System.out.println("\n🏆 " + name + " Standings 🏆");
        System.out.println("====================================================");
        System.out.printf("%-20s %-6s %-6s %-6s %-6s%n",
                "Team", "Pts", "Pld", "GF", "GA");
        System.out.println("----------------------------------------------------");

        for (Team team : teams) {
            System.out.printf("%-20s %-6d %-6d %-6d %-6d%n",
                    team.getName(),
                    getPoints(team),
                    getMatchesPlayed(team),
                    getGoalsFor(team),
                    getGoalsAgainst(team));
        }
        System.out.println("====================================================\n");
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


    public static League createNewLeague(ArrayList<Team> teams, Scanner scanner) {
        System.out.println("\n⚽ Create New League ⚽");
        System.out.println("======================");

        System.out.println("Enter league name:");
        String name = scanner.nextLine();

        System.out.println("Enter number of teams (must be even):");
        int teamCount;
        do {
            teamCount = scanner.nextInt();
            scanner.nextLine(); // consume newline
            if (teamCount % 2 != 0 || teamCount < 2 || teamCount > teams.size()) {
                System.out.println("Invalid number. Must be even and between 2 and " + teams.size());
            }
        } while (teamCount % 2 != 0 || teamCount < 2 || teamCount > teams.size());

        League league = new League(name, teamCount);

        System.out.println("Select teams to participate:");
        for (int i = 0; i < teamCount; i++) {
            System.out.println("Available teams:");
            for (int j = 0; j < teams.size(); j++) {
                if (!league.getTeams().contains(teams.get(j))) {
                    System.out.println((j+1) + ". " + teams.get(j).getName());
                }
            }

            System.out.println("Enter team number to add:");
            int teamNum;
            do {
                teamNum = scanner.nextInt();
                scanner.nextLine(); // consume newline
                if (teamNum < 1 || teamNum > teams.size() || league.getTeams().contains(teams.get(teamNum-1))) {
                    System.out.println("Invalid selection. Try again.");
                }
            } while (teamNum < 1 || teamNum > teams.size() || league.getTeams().contains(teams.get(teamNum-1)));

            league.addTeam(teams.get(teamNum-1));
            System.out.println("Added " + teams.get(teamNum-1).getName() + " to the league.");
        }

        return league;
    }


    public void showAllMatches() {
        System.out.println("\n⚽ " + name + " Matches ⚽");
        System.out.println("====================================================");
        for (Match match : matches) {
            System.out.println(match);
        }
        System.out.println("====================================================\n");
    }


    public void showMatchResults() {
        System.out.println("\n⚽ " + name + " Match Results ⚽");
        System.out.println("====================================================");
        for (Match match : matches) {
            if (match.isPlayed()) {
                System.out.println(match.getResult());
            }
        }
        System.out.println("====================================================\n");
    }
}