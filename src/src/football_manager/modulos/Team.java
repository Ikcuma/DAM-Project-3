
package football_manager.modulos;

import football_manager.controladores.Team_controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;


public class Team {
    private String name;
    private String birthDate;
    private String city;
    private Coach coach;  // Cambiado a Coach, no más Person
    private Person owner;
    private List<Person> players;

    // Constructor
    public Team(String name, String birthDate, String city, Coach coach, Person owner, List<Person> players) {  // Cambiado el tipo de coach
        int playerNumber = players.size();
        if (playerNumber < 1) {
            throw new IllegalArgumentException("Debe haber al menos un jugador");
        }
        this.name = name;
        this.birthDate = birthDate;
        this.city = city;
        this.coach = coach;  // Asignación de coach
        this.owner = owner;
        this.players = players;
    }

    // Constructor sin coach (por ejemplo, para cuando se crea un equipo sin entrenador)
    public Team(String name, String birthDate, String city) {
        this.name = name;
        this.birthDate = birthDate;
        this.city = city;
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
        return coach;  // No hace falta el instanceof ya que coach es de tipo Coach directamente
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

    public void setPlayers(List<Person> players) {
        this.players = players;
    }

    public void setCoach(Coach coach) {
        this.coach = coach;  // Cambio en setter
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }

    // Métodos adicionales
    public Player getSpecificPlayer(String name) {
        for (Person p : players) {
            if (p.getName().equals(name) && p instanceof Player) {
                return (Player) p;
            }
        }
        return null;
    }

    public void addSpecificPlayer(Player p) {
        Person newPlayer = (Person) p;
        this.players.add(newPlayer);
    }

    // Método para convertir a formato de archivo
    public String toFileFormat() {
        StringBuilder sb = new StringBuilder();

        sb.append(this.name).append(";")
                .append(this.birthDate).append(";")
                .append(this.city).append(";");

        // Manejar coach null
        if (coach != null) {
            sb.append(coach.toFileFormatTeam()).append(";");  // Serializa el coach correctamente
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

    public static void registerTeam(HashMap<String, Person> hashMapPeople, ArrayList<Team> teams, Scanner sc){
        Team_controller.registerTeam(hashMapPeople, teams, sc);
    }

    public static void deregisterTeam(ArrayList<Team> teams, Scanner scanner){
        Team_controller.deregisterTeam(teams, scanner);
    }
}