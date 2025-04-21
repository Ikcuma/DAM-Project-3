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

        // Mostrar y obtener el nombre de la liga
        System.out.println("Enter league name:");
        String name = scanner.nextLine();

        // Llamar a la lógica del módulo para obtener el número de equipos
        int teamCount = League.getTeamCount(teams, scanner);

        // Crear la liga con los datos obtenidos
        League league = new League(name, teamCount);

        System.out.println("Select teams to participate:");

        // Llamar al módulo para asignar equipos a la liga
        League.assignTeamsToLeague(league, teams, teamCount, scanner);

        return league;
    }

    public static void showStandings(League league) {
        // Llamar al módulo para ordenar los equipos
        League.sortTeamsByStandings(league);

        // Mostrar la tabla con la clasificación
        System.out.println("\n🏆 " + league.getName() + " Standings 🏆");
        System.out.println("====================================================");
        System.out.printf("%-20s %-6s %-6s %-6s %-6s%n",
                "Team", "Pts", "Pld", "GF", "GA");
        System.out.println("----------------------------------------------------");

        // Llamar al módulo para obtener la información de los equipos
        League.displayTeamStats(league);

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