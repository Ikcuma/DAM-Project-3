package football_manager;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Team {
    private String name;
    private String birthDate;
    private String city;
    private Coach coach;
    private Person owner;
    private List<Player> players;

    // Constructor
    public Team(String name, String birthDate, String city, Coach coach, Person owner, List<Player> players) {
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

    public List<Player> getPlayers() {
        return players;
    }

    public Coach getCoach() {
        return coach;
    }

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

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public void setCoach(Coach coach) {
        this.coach = coach;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }

    // Methods
    public static void loadTeams(ArrayList<String> bruteTeamData, ArrayList<Team> teams,
                                 HashMap<String, Player> hashMapPlayers,
                                 HashMap<String, Coach> hashMapCoaches,
                                 HashMap<String, Person> hashMapOwners) throws IOException {
        String filePath = "C:\\Users\\dunkl\\IdeaProjects\\DAM-Project-3\\src\\src\\football_manager\\resources\\team_files.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                bruteTeamData.add(line);
            }
        }

        for (String line : bruteTeamData) {
            String[] teamData = line.split(";");

            String coachName = teamData[3];
            String ownerName = teamData[4];

            Coach coach = hashMapCoaches.get(coachName);
            Person owner = hashMapOwners.get(ownerName);

            if (coach == null || owner == null) {
                System.out.println("Error: Entrenador o dueño no encontrado para el equipo: " + teamData[0]);
                continue;
            }

            List<Player> teamPlayers = new ArrayList<>();
            for (int i = 5; i < teamData.length; i++) {
                Player player = hashMapPlayers.get(teamData[i]);
                if (player != null) {
                    teamPlayers.add(player);
                } else {
                    System.out.println("Error: Jugador no encontrado: " + teamData[i]);
                }
            }

            Team team = new Team(teamData[0], teamData[1], teamData[2], coach, owner, teamPlayers);
            teams.add(team);
        }
    }

    public static void registerTeam(HashMap<String, Player> players, HashMap<String, Coach> coaches,
                                    HashMap<String, Person> owners, ArrayList<Team> teams) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("What would be the name of the team?");
        String teamName = scanner.nextLine();
        System.out.println("What would be the birth date of the team?");
        String birthDate = scanner.nextLine();
        scanner.nextLine();

        System.out.println("What city is the team located in?");
        String city = scanner.nextLine();

        Coach coach = null;
        boolean exit = false;
        do {
            System.out.println("Who is the coach?");
            String coachName = scanner.nextLine();
            coach = coaches.get(coachName);
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
            String ownerName = scanner.nextLine();
            owner = owners.get(ownerName);
            if (owner == null) {
                System.out.println("The owner doesn't exist. Please try again.");
            } else {
                exit = true;
            }
        } while (!exit);

        List<Player> teamPlayers = new ArrayList<>();

        boolean addMorePlayers = true;
        while (addMorePlayers) {
            System.out.println("Enter the name of a player to add (or press Enter to finish):");
            String playerName = scanner.nextLine();

            if (playerName.isEmpty()) {
                addMorePlayers = false;
            } else {
                Player player = players.get(playerName);
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
e