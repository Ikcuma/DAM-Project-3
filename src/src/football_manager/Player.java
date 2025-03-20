package football_manager;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;


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
