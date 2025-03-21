package football_manager;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Player extends Person {
    private int back;
    private String position;
    private int cualityPoints; //(30-100)

    //Constructor

    public Player(String name, String surName, String birthDay, int motivation, int anualSalary, int back, String position, int cualityPoints) {
        super(name, surName, birthDay, motivation, anualSalary);
        this.back = back;
        this.position = position;
        this.cualityPoints = cualityPoints;
    }


    //Getters

    public int getBack() {
        return back;
    }

    public String getPosition() {
        return position;
    }

    public int getCualityPoints() {
        return cualityPoints;
    }


    //Setters

    public void setBack(int back) {
        this.back = back;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setCualityPoints(int cualityPoints) {
        this.cualityPoints = cualityPoints;
    }

    //Methods


    public  void train() {
        String[] options = {"DEF", "MIG", "DAV", "POR"};
        Random random = new Random();

        int randomIndex = random.nextInt(options.length);
        String newPosition = options[randomIndex];

        boolean realiza = Math.random() < 0.05;

            if (realiza) {
                this.position = newPosition;
                System.out.println("El jugador " + this.name + " ha cambiado a la posición: " + newPosition);
            }

    }
    public void printPersonData(){
            System.out.println("════════════════════════════════════════════");
            System.out.println("  Player Information: " + this.name);
            System.out.println("════════════════════════════════════════════");
            System.out.println("Name: " + this.name);
            System.out.println("Surname: " + this.surName);
            System.out.println("Date of Birth: " + this.birthDay);
            System.out.println("Motivation: " + this.motivation + " points");
            System.out.println("Annual Salary: $" + this.anualSalary);
            System.out.println("Position: " + this.position);
            System.out.println("Quality Points: " + this.cualityPoints);
            System.out.println("Jersey Number: " + this.back);
            System.out.println("════════════════════════════════════════════");
    }
    public String toFileFormat() {
        return String.format("J;%s;%s;%s;%d;%d;%d;%s;%d", super.name, super.surName, super.birthDay, super.motivation, super.anualSalary, this.back, this.position, this.cualityPoints);
    }

    public String toFileFormatTeam() {
        return "Player{name='" + this.name +
                "', surName='" + this.surName +
                "', birthDay='" + this.birthDay +
                "', motivation=" + this.motivation +
                ", anualSalary=" + this.anualSalary +
                ", back=" + this.back +
                ", position='" + this.position +
                "', cualityPoints=" + this.cualityPoints + "}";
    }
    public static String extractPlayerData(String data) {
        Pattern pattern = Pattern.compile("Player\\{[^}]+\\}");
        Matcher matcher = pattern.matcher(data);
        return matcher.find() ? matcher.group(0) : "";
    }

    public static Player parse(String data) {
        if (data.isEmpty()) {
            return null;
        }

        Pattern pattern = Pattern.compile(
                "Player\\{name='(.*?)', surName='(.*?)', birthDay='(.*?)', motivation=(\\d+), anualSalary=(\\d+), back=(\\d+), position='(.*?)', cualityPoints=(\\d+)\\}"
        );
        Matcher matcher = pattern.matcher(data);

        if (matcher.find()) {
            String name = matcher.group(1);
            String surName = matcher.group(2);
            String birthDay = matcher.group(3);
            String position = matcher.group(7);

            // Validar que los campos obligatorios no sean nulos o vacíos
            if (name == null || name.isEmpty() || surName == null || surName.isEmpty() ||
                    birthDay == null || birthDay.isEmpty() || position == null || position.isEmpty()) {
                System.out.println("Error: Datos incompletos para el jugador: " + data);
                return null;
            }

            return new Player(
                    name,  // name
                    surName,  // surName
                    birthDay,  // birthDay
                    Integer.parseInt(matcher.group(4)),  // motivation
                    Integer.parseInt(matcher.group(5)),  // anualSalary
                    Integer.parseInt(matcher.group(6)),  // back
                    position,  // position
                    Integer.parseInt(matcher.group(8))   // cualityPoints
            );
        }
        return null;
    }


    @Override
    public String toString() {
        return "Player{" +
                "back=" + back +
                ", position='" + position + '\'' +
                ", cualityPoints=" + cualityPoints +
                ", name='" + name + '\'' +
                ", surName='" + surName + '\'' +
                ", birthDay='" + birthDay + '\'' +
                ", motivation=" + motivation +
                ", anualSalary=" + anualSalary +
                '}';
    }
}
