package football_manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import static football_manager.Main.capitalizeFirstLetterNames;


public class Team {
    private String name;
    private String birthDate;
    private String city;
    private Person coach;
    private Person owner;
    private List<Person> players;

    // Constructor
    public Team(String name, String birthDate, String city, Person coach, Person owner, List<Person> players) {
        int playerNumber = players.size();
        if (playerNumber < 1) {
            throw new IllegalArgumentException("Debe haber al menos un jugador");
        }
        this.name = name;
        this.birthDate = birthDate;
        this.city = city;
        this.coach = coach;
        this.owner = owner;
        this.players = players;
    }


    // Getters
    public String getName() {
        return name;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public String getCity() {
        return city;
    }

    public List<Person> getPlayers() {
        return players;
    }


    public Coach getCoach() {
        return (coach instanceof Coach) ? (Coach) coach : null;
    }

    //public Person getCoach() {return this.coach;}

    public Person getOwner() {
        return owner;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setPlayers(List<Person> players) {
        this.players = players;
    }

    public void setCoach(Coach coach) {
        this.coach = coach;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }

    // Methods
    public Player getSpecificPlayer(String player) {
        for (Person p : players){
            if (p.getName().equals(player) && p instanceof Player) {
                return (Player) p;
            }
        }
        return null;
    }

    public String toFileFormat() {
        StringBuilder sb = new StringBuilder();

        sb.append(this.name).append(";")
                .append(this.birthDate).append(";")
                .append(this.city).append(";");

        // Manejar coach null
        if (coach != null) {
            sb.append(coach.toFileFormatTeam()).append(";");
        } else {
            sb.append("null;");
        }

        sb.append(owner.toFileFormatTeam()).append(";");

        for (Person player : players) {
            sb.append(player.toFileFormatTeam()).append(";");
        }

        // Eliminar el último ";" si hay elementos
        if (!players.isEmpty()) {
            sb.deleteCharAt(sb.length() - 1);
        }

        return sb.toString();
    }

    public static void registerTeam(HashMap<String, Person> hashMapPeople, ArrayList<Team> teams, Scanner sc) {
        System.out.println("What would be the name of the team?");
        String teamName = capitalizeFirstLetterNames(sc.nextLine());
        System.out.println("What would be the birth date of the team?");
        String birthDate = sc.nextLine();

        System.out.println("What city is the team located in?");
        String city = sc.nextLine();

        Person coach = null;
        boolean exit = false;
        do {
            System.out.println("Who is the coach?");
            String coachName = sc.nextLine();
            coach = hashMapPeople.get(coachName);
            if (coach == null) {
                System.out.println("The coach doesn't exist. Please try again.");
            } else {
                exit = true;
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