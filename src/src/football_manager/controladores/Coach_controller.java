/**
 * Controlador para gestionar las operaciones relacionadas con los entrenadores (Coaches).
 */
package football_manager.controladores;

import football_manager.modulos.Coach;
import football_manager.modulos.Team;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Coach_controller {

    /**
     * Despide o reemplaza al entrenador de un equipo.
     *
     * @param teams Lista de equipos disponibles
     * @param scanner Scanner para entrada de usuario
     */
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

    /**
     * Imprime los datos de un entrenador en formato legible.
     *
     * @param coach Entrenador cuyos datos se mostrarán
     */
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

    /**
     * Entrena a un entrenador, aumentando su salario anual en un 5%.
     *
     * @param coach Entrenador que será entrenado
     */
    public static void train(Coach coach) {
        coach.setAnualSalary((int) (coach.getAnualSalary() * 1.05));

        System.out.printf("🏆 %s %s trained! New salary: $%,d\n",
                coach.getName(),
                coach.getSurName(),
                coach.getAnualSalary());
    }
}