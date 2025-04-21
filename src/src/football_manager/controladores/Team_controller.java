package football_manager.controladores;

import football_manager.modulos.Coach;
import football_manager.modulos.Person;
import football_manager.modulos.Team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import static football_manager.controladores.Person_controller.capitalizeFirstLetterNames;

public class Team_controller {
    public static void registerTeam(HashMap<String, Person> hashMapPeople, ArrayList<Team> teams, Scanner sc) {
        System.out.println("What would be the name of the team?");
        String teamName = capitalizeFirstLetterNames(sc.nextLine());
        System.out.println("What would be the birth date of the team?");
        String birthDate = sc.nextLine();

        System.out.println("What city is the team located in?");
        String city = sc.nextLine();

        Coach coach = null;
        boolean exit = false;
        do {
            System.out.println("Who is the coach?");
            String coachName = sc.nextLine();
            Person coachPerson = hashMapPeople.get(coachName); // Usamos Person porque hashMapPeople almacena personas genéricas

            if (coachPerson == null) {
                System.out.println("The coach doesn't exist. Please try again.");
            } else {
                // Verificamos si coachPerson es realmente un Coach
                if (coachPerson instanceof Coach) {
                    coach = (Coach) coachPerson; // Hacemos el casting a Coach solo si es una instancia de Coach
                    exit = true; // Si el casting es exitoso, terminamos el loop
                } else {
                    System.out.println("The selected person is not a coach. Please try again.");
                }
            }
        } while (!exit);

        Person owner = null;
        exit = false;
        do {
            System.out.println("Who is the owner?");
            String ownerName = sc.nextLine();
            owner = hashMapPeople.get(ownerName);
            if (owner == null) {
                System.out.println("The owner doesn't exist. Please try again.");
            } else {
                exit = true;
            }
        } while (!exit);

        List<Person> teamPlayers = new ArrayList<>();

        boolean addMorePlayers = true;
        while (addMorePlayers) {
            System.out.println("Enter the name of a player to add (or press Enter to finish):");
            String playerName = sc.nextLine();

            if (playerName.isEmpty()) {
                addMorePlayers = false;
            } else {
                Person player = hashMapPeople.get(playerName);
                if (player != null) {
                    teamPlayers.add(player);
                    System.out.println("Player " + playerName + " added to the team.");
                } else {
                    System.out.println("Player not found. Please try again.");
                }
            }
        }

        Team newTeam = new Team(teamName, birthDate, city, coach, owner, teamPlayers);
        teams.add(newTeam);
        System.out.println("Team registered and saved to file successfully!");
    }

    public static void deregisterTeam(ArrayList<Team> teams, Scanner scanner) {
        System.out.println("Enter the name of the team you want to deregister:");
        String teamName = scanner.nextLine();

        Team teamToRemove = null;
        for (Team team : teams) {
            if (team.getName().equalsIgnoreCase(teamName)) {
                teamToRemove = team;
                break;
            }
        }

        if (teamToRemove != null) {
            teams.remove(teamToRemove);
            System.out.println("Team '" + teamName + "' has been deregistered.");
        } else {
            System.out.println("Team '" + teamName + "' not found.");
        }
    }
}