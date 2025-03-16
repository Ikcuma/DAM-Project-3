package football_manager;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Coach extends Person {
    private int victories;
    private boolean nacional;

    // Constructors
    public Coach(String name, String surName, String birthDay, int motivation, int anualSalary, int victories, boolean nacional) {
        super(name, surName, birthDay, motivation, anualSalary);
        this.victories = victories;
        this.nacional = nacional;
    }

    // Getters
    public int getVictories() {
        return victories;
    }

    public boolean isNacional() {
        return nacional;
    }

    // Setters
    public void setVictories(int victories) {
        this.victories = victories;
    }

    public void setNacional(boolean nacional) {
        this.nacional = nacional;
    }

    // Methods
    public static void increaseSalary(String coachName, HashMap<String, Coach> hashCoaches, ArrayList<Coach> coaches) {
        Coach coach = hashCoaches.get(coachName);
        if (coach != null) {
            int newSalary = (int) (coach.getAnualSalary() * 1.05);
            coach.setAnualSalary(newSalary);
            for (int i = 0; i < coaches.size(); i++) {
                if (coaches.get(i).getName().equals(coachName)) {
                    break;
                }
            }
            System.out.println("Salary increased for coach: " + coachName);
        } else {
            System.out.println("Coach not found.");
        }
    }

    public static void printCoachData(String coachName, HashMap<String, Coach> coaches) {
        Coach coach = coaches.get(coachName);
        if (coach != null) {
            System.out.println("════════════════════════════════════════════");
            System.out.println("  Coach Information: " + coachName);
            System.out.println("════════════════════════════════════════════");
            System.out.println("Name: " + coach.getName());
            System.out.println("Surname: " + coach.getSurName());
            System.out.println("Date of Birth: " + coach.getBirthDay());
            System.out.println("Motivation: " + coach.getMotivation() + " points");
            System.out.println("Annual Salary: $" + coach.getAnualSalary());
            System.out.println("Victories: " + coach.getVictories());
            System.out.println("Is he nacional?: " + coach.isNacional());
            System.out.println("════════════════════════════════════════════");
        } else {
            System.out.println("Coach not found.");
        }
    }
    public static void dismissCoach(ArrayList<Team> teams, Scanner scanner) {
        System.out.println("Enter the name of the team whose coach you want to dismiss:");
        String teamName = scanner.nextLine();

        Team teamToModify = null;
        for (Team team : teams) {
            if (team.getName().equalsIgnoreCase(teamName)) {
                teamToModify = team;
                break;
            }
        }

        if (teamToModify != null) {
            System.out.println("Enter the name of the new coach:");
            String newCoachName = scanner.nextLine();

            Coach newCoach = teamToModify.getCoach(); // Default to current coach
            for (Coach coach : teams.stream().map(Team::getCoach).toList()) {
                if (coach.getName().equalsIgnoreCase(newCoachName)) {
                    newCoach = coach;
                    break;
                }
            }

            if (newCoach != null) {
                teamToModify.setCoach(newCoach);
                System.out.println("Coach of team '" + teamName + "' has been updated to " + newCoach.getName());
            } else {
                System.out.println("Coach '" + newCoachName + "' not found.");
            }
        } else {
            System.out.println("Team '" + teamName + "' not found.");
        }
    }
}