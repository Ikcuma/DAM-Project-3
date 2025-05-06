
package football_manager.modulos;

import football_manager.controladores.Coach_controller;

import java.util.ArrayList;
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
    public static void train(Coach coach) {
        int newSalary = (int) (coach.getAnualSalary() * 1.05);
        coach.setAnualSalary(newSalary);
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



    public static Team findTeamByName(ArrayList<Team> teams, String teamName) {
        for (Team team : teams) {
            if (team.getName().equalsIgnoreCase(teamName)) {
                return team;
            }
        }
        return null;
    }

    public static Coach findCoachByNameInTeams(ArrayList<Team> teams, String coachName) {
        for (Team team : teams) {
            Coach coach = team.getCoach();
            if (coach != null && coach.getName().equalsIgnoreCase(coachName)) {
                return coach;
            }
        }
        return null;
    }

    public static String dismissOrReplaceCoach(ArrayList<Team> teams, String teamName, int option, String newCoachName) {
        Team teamToModify = findTeamByName(teams, teamName);
        if (teamToModify == null) {
            return "Team '" + teamName + "' not found.";
        }

        if (teamToModify.getCoach() == null) {
            return "Team '" + teamName + "' doesn't have a coach to dismiss.";
        }

        if (option == 1) {
            teamToModify.setCoach(null);
            return "Coach has been dismissed from team '" + teamName + "'.";
        } else if (option == 2) {
            Coach newCoach = findCoachByNameInTeams(teams, newCoachName);
            if (newCoach != null) {
                teamToModify.setCoach(newCoach);
                return "Coach of team '" + teamName + "' has been replaced with " + newCoach.getName();
            } else {
                return "Coach '" + newCoachName + "' not found in any team.";
            }
        } else {
            return "Invalid option. Please choose 1 or 2.";
        }
    }
}