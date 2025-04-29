/**
 * Representa un entrenador de fútbol en el sistema.
 * Extiende de la clase Person y añade características específicas de entrenadores.
 */
package football_manager.modulos;

import football_manager.controladores.Coach_controller;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Coach extends Person {
    private int victories;
    private boolean nacional;

    /**
     * Constructor para crear un nuevo entrenador.
     *
     * @param name Nombre del entrenador
     * @param surName Apellido del entrenador
     * @param birthDay Fecha de nacimiento (DD-MM-YYYY)
     * @param motivation Nivel de motivación (1-10)
     * @param anualSalary Salario anual
     * @param victories Número de victorias
     * @param nacional Si ha sido seleccionado para un equipo nacional
     */


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
        Coach_controller.train(this);
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

    public void printPersonData(Coach coach){
        Coach_controller.printPersonData(coach);
    }

    public static void dismissCoach(ArrayList<Team> teams, Scanner scanner){
        Coach_controller.dismissCoach(teams, scanner);
    }
}