package football_manager.controladores;

import football_manager.modulos.League;
import football_manager.modulos.Match;
import football_manager.modulos.Team;

import java.util.ArrayList;
import java.util.Scanner;

public class League_controller {
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
                    System.out.println((j + 1) + ". " + teams.get(j).getName());
                }
            }

            System.out.println("Enter team number to add:");
            int teamNum;
            do {
                teamNum = scanner.nextInt();
                scanner.nextLine(); // consume newline
                if (teamNum < 1 || teamNum > teams.size() || league.getTeams().contains(teams.get(teamNum - 1))) {
                    System.out.println("Invalid selection. Try again.");
                }
            } while (teamNum < 1 || teamNum > teams.size() || league.getTeams().contains(teams.get(teamNum - 1)));

            league.addTeam(teams.get(teamNum - 1));
            System.out.println("Added " + teams.get(teamNum - 1).getName() + " to the league.");
        }

        return league;
    }

    public static void showStandings(League league) {
        league.getTeams().sort((t1, t2) -> {
            int pointsCompare = Integer.compare(league.getPoints(t2), league.getPoints(t1));
            if (pointsCompare != 0) return pointsCompare;

            int gdCompare = Integer.compare(league.getGoalDifference(t2), league.getGoalDifference(t1));
            if (gdCompare != 0) return gdCompare;

            return Integer.compare(league.getGoalsFor(t2), league.getGoalsFor(t1));
        });

        System.out.println("\n🏆 " + league.getName() + " Standings 🏆");
        System.out.println("====================================================");
        System.out.printf("%-20s %-6s %-6s %-6s %-6s%n",
                "Team", "Pts", "Pld", "GF", "GA");
        System.out.println("----------------------------------------------------");

        for (Team team : league.getTeams()) {
            System.out.printf("%-20s %-6d %-6d %-6d %-6d%n",
                    team.getName(),
                    league.getPoints(team),
                    league.getMatchesPlayed(team),
                    league.getGoalsFor(team),
                    league.getGoalsAgainst(team));
        }

        System.out.println("====================================================\n");
    }

    public static void showAllMatches(League league) {
        System.out.println("\n⚽ " + league.getName() + " Matches ⚽");
        System.out.println("====================================================");
        for (Match match : league.getMatches()) {
            System.out.println(match);
        }
        System.out.println("====================================================\n");
    }

    public static void showMatchResults(League league) {
        System.out.println("\n⚽ " + league.getName() + " Match Results ⚽");
        System.out.println("====================================================");
        for (Match match : league.getMatches()) {
            if (match.isPlayed()) {
                System.out.println(match.getResult());
            }
        }
        System.out.println("====================================================\n");
    }
}