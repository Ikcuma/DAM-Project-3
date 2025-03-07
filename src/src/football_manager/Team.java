package football_manager;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Team {
    private String name;
    private int birthDate;
    private String city;
    private List<Player> players;
    private Coach coach;
    private Person owner;


    //Constructor

    public Team(String name, int birthDate, String city, List<Player> players, Coach coach, Person owner) {
        int playerNumber = players.size();
        if (playerNumber < 1) {
            throw new IllegalArgumentException("Debe haber al menos un jugador");
        }
        this.name = name;
        this.birthDate = birthDate;
        this.city = city;
        this.players = players;
        this.coach = coach;
        this.owner = owner;
    }



    //Getters


    public String getName() {
        return name;
    }

    public int getBirthDate() {
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

    //Setters

    public void setName(String name) {
        this.name = name;
    }

    public void setBirthDate(int birthDate) {
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

    //Methods
    public static void loadTeams(ArrayList<String> bruteTeamData, ArrayList<Team> teams,
                                 HashMap<String, Player> hashMapPlayers,
                                 HashMap<String, Coach> hashMapCoaches,
                                 HashMap<String, Person> hashMapOwners) {
        String filePath = "C:\\Users\\dunkl\\IdeaProjects\\DAM-Project-3\\src\\src\\football_manager\\resources\\team_files.txt";
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = reader.readLine()) != null) {
                bruteTeamData.add(line);
            }
        } catch (FileNotFoundException e) {
            System.out.println("file not found...");
        } catch (IOException e) {
            throw new RuntimeException(e);
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

            Team team = new Team(teamData[0], Integer.parseInt(teamData[1]), teamData[2], teamPlayers, coach, owner);
            teams.add(team);

        }
    }

    /*public void printTeamData(String teamName) {
        System.out.println("Team: " + name);
        System.out.println("President: " + owner.getName() + " " + owner.getSurName());
        System.out.println("Players:");
        for (Player player : players) {
            System.out.println(player.getName() + " " + player.getSurName() + " - Motivation: " + player.getMotivation());
        }
        System.out.println("Coach: " + coach.getName() + " " + coach.getSurName() + " - Motivation: " + coach.getMotivation());
    }*/





    @Override
    public String toString() {
        return "Team{" +
                "name='" + name + '\'' +
                ", birthDate=" + birthDate +
                ", city='" + city + '\'' +
                ", players=" + players +
                ", coach=" + coach +
                ", owner=" + owner +
                '}';
    }


}

