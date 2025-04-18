e
package football_manager;

import java.util.ArrayList;
import java.util.InputMismatchException;
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
        this.anualSalary = (int) (this.anualSalary * 1.05);

        System.out.printf("🏆 %s %s trained! New salary: $%,d",
                this.name,
                this.surName,
                this.anualSalary);
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

        if (teamToModify == null) {
            System.out.println("Team '" + teamName + "' not found.");
            return;
        }


        if (teamToModify.getCoach() == null) {
            System.out.println("Team '" + teamName + "' doesn't have a coach to dismiss.");
            return;
        }

        System.out.println("Current coach: " + teamToModify.getCoach().getName());
        System.out.println("Do you want to (1) Remove coach or (2) Replace coach?");
        int option;
        try {
            option = scanner.nextInt();
            scanner.nextLine();
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter 1 or 2.");
            scanner.nextLine();
            return;
        }

        if (option == 1) {

            teamToModify.setCoach(null);
            System.out.println("Coach has been dismissed from team '" + teamName + "'.");
        } else if (option == 2) {

            System.out.println("Enter the name of the new coach:");
            String newCoachName = scanner.nextLine();


            Coach newCoach = null;
            for (Team team : teams) {
                if (team.getCoach() != null && team.getCoach().getName().equalsIgnoreCase(newCoachName)) {
                    newCoach = team.getCoach();
                    break;
                }
            }

            if (newCoach != null) {
                teamToModify.setCoach(newCoach);
                System.out.println("Coach of team '" + teamName + "' has been replaced with " + newCoach.getName());
            } else {
                System.out.println("Coach '" + newCoachName + "' not found in any team.");
            }
        } else {
            System.out.println("Invalid option. Please choose 1 or 2.");
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
                    matcher.group(1),
                    matcher.group(2),
                    matcher.group(3),
                    Integer.parseInt(matcher.group(4)),
                    Integer.parseInt(matcher.group(5)),
                    Integer.parseInt(matcher.group(6)),
                    Boolean.parseBoolean(matcher.group(7))
            );
        }
        return null;
    }

}