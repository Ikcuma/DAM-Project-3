package football_manager.controladores;

import football_manager.modulos.Coach;
import football_manager.modulos.Team;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Coach_controller {
    public static void dismissCoach(ArrayList<Team> teams, Scanner sc) {
        System.out.println("Enter the name of the team whose coach you want to dismiss:");
        String teamName = sc.nextLine();

        Team teamToModify = Coach.findTeamByName(teams, teamName);
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
            option = sc.nextInt();
            sc.nextLine();
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter 1 or 2.");
            sc.nextLine();
            return;
        }

        String newCoachName = null;
        if (option == 2) {
            System.out.println("Enter the name of the new coach:");
            newCoachName = sc.nextLine();
        }

        String result = Coach.dismissOrReplaceCoach(teams, teamName, option, newCoachName);
        System.out.println(result);
    }

    public static void printPersonData(Coach coach) {
        System.out.println("════════════════════════════════════════════");
        System.out.println("  Coach Information: " + coach.getName());
        System.out.println("════════════════════════════════════════════");
        System.out.println("Name: " + coach.getName());
        System.out.println("Surname: " + coach.getSurName());
        System.out.println("Date of Birth: " + coach.getBirthDay());
        System.out.println("Motivation: " + coach.getMotivation() + " points");
        System.out.println("Annual Salary: $" + coach.getAnualSalary());
        System.out.println("Victories: " + coach.getVictories());
        System.out.println("Is he nacional?: " + coach.isNacional());
        System.out.println("════════════════════════════════════════════");
    }

    public void train(Coach coach) {
        Coach.train(coach);

        System.out.printf("🏆 %s %s trained! New salary: $%,d\n",
                coach.getName(),
                coach.getSurName(),
                coach.getAnualSalary());
    }
}