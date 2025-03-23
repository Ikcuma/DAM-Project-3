package football_manager;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    public void train() {
            int newSalary = (int) (super.anualSalary * 1.05);
            System.out.println("Salary increased for coach: " + super.name);
    }

    public void printPersonData() {
            System.out.println("════════════════════════════════════════════");
            System.out.println("  Coach Information: " + name);
            System.out.println("════════════════════════════════════════════");
            System.out.println("Name: " + super.name);
            System.out.println("Surname: " + super.surName);
            System.out.println("Date of Birth: " + super.birthDay);
            System.out.println("Motivation: " + super.motivation + " points");
            System.out.println("Annual Salary: $" + super.anualSalary);
            System.out.println("Victories: " + this.victories);
            System.out.println("Is he nacional?: " + this.nacional);
            System.out.println("════════════════════════════════════════════");

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
                if (coach != null && coach.getName().equalsIgnoreCase(newCoachName)) {
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
    public String toFileFormat() {
        return String.format("E;%s;%s;%s;%d;%d;%d;%b",
                super.name, super.surName, super.birthDay, super.motivation, super.anualSalary, this.victories, this.nacional);
    }
    public String toFileFormatTeam() {
        return "Coach{name='" + this.name +
                "', surName='" + this.surName +
                "', birthDay='" + this.birthDay +
                "', motivation=" + this.motivation +
                ", anualSalary=" + this.anualSalary +
                ", victories=" + this.victories +
                ", nacional=" + this.nacional + "}";
    }
    public static String extractCoachData(String data) {
        Pattern pattern = Pattern.compile("Coach\\{[^}]+\\}");
        Matcher matcher = pattern.matcher(data);
        return matcher.find() ? matcher.group(0) : "";
    }

    public static Coach parse(String data) {
        if (data.isEmpty()) {
            return null;
        }

        Pattern pattern = Pattern.compile(
                "Coach\\{name='(.*?)', surName='(.*?)', birthDay='(.*?)', motivation=(\\d+), anualSalary=(\\d+), victories=(\\d+), nacional=(true|false)\\}"
        );
        Matcher matcher = pattern.matcher(data);

        if (matcher.find()) {
            return new Coach(
                    matcher.group(1),  // name
                    matcher.group(2),  // surName
                    matcher.group(3),  // birthDay
                    Integer.parseInt(matcher.group(4)),  // motivation
                    Integer.parseInt(matcher.group(5)),  // anualSalary
                    Integer.parseInt(matcher.group(6)),  // victories
                    Boolean.parseBoolean(matcher.group(7))  // nacional
            );
        }
        return null;
    }

}