package football_manager.controladores;

import football_manager.modulos.Coach;
import football_manager.modulos.Person;
import football_manager.modulos.Team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import static football_manager.modulos.Person.capitalizeFirstLetterNames;

public class Team_controller {
    public static void registerTeam(HashMap<String, Person> hashMapPeople, ArrayList<Team> teams, Scanner sc) {
        System.out.println("What would be the name of the team?");
        String teamName = Team.capitalizeFirstLetterNames(sc.nextLine()); // Pone en mayúsula la primera letra

        System.out.println("What would be the birth date of the team?");
        String birthDate = sc.nextLine();

        System.out.println("What city is the team located in?");
        String city = sc.nextLine();

        Coach coach = null;
        while (coach == null) {
            System.out.println("Who is the coach?");
            String coachName = sc.nextLine();
            coach = Team.getCoachByName(hashMapPeople, coachName); // Busca en el HashMap una persona con ese nombre y comprueba si es instancia de Coach
            if (coach == null) {
                System.out.println("The coach doesn't exist or is not a coach. Please try again.");
            }
        }

        Person owner = null;
        while (owner == null) {
            System.out.println("Who is the owner?");
            String ownerName = sc.nextLine();
            owner = Team.getOwnerByName(hashMapPeople, ownerName); // Busca en el HashMap una persona con ese nombre
            if (owner == null) {
                System.out.println("The owner doesn't exist. Please try again.");
            }
        }

        List<Person> teamPlayers = new ArrayList<>();
        while (true) {
            System.out.println("Enter the name of a player to add (or press Enter to finish):");
            String playerName = sc.nextLine();
            if (playerName.isEmpty()) break;

            Person player = Team.getPlayerByName(hashMapPeople, playerName);// Busca en el HashMap si hay una persona con ese nombre
            if (player != null) {
                teamPlayers.add(player);
                System.out.println("Player " + playerName + " added to the team.");
            } else {
                System.out.println("Player not found. Please try again.");
            }
        }

        Team newTeam = Team.createTeam(teamName, birthDate, city, coach, owner, teamPlayers);//recoge informacion
        teams.add(newTeam);
        System.out.println("✅ Team registered and saved to file successfully!");
    }

    public static void deregisterTeam(ArrayList<Team> teams, Scanner scanner) {
        System.out.println("Enter the name of the team you want to deregister:");
        String teamName = scanner.nextLine();
        //busca un equipo
        Team teamToRemove = Team.findTeamByName(teams, teamName);

        if (teamToRemove != null) {
            boolean removed = Team.removeTeam(teams, teamToRemove); // Elimina el equipo y devuelve true o false
            if (removed) {
                System.out.println("✅ Team '" + teamName + "' has been deregistered.");
            } else {
                System.out.println("❌ Error while removing the team.");
            }
        } else {
            System.out.println("❌ Team '" + teamName + "' not found.");
        }
    }
}