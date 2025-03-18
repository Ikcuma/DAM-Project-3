package football_manager;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

import static football_manager.Main.reewriteFileMarket;

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


    public static void changePlayerPosition(HashMap<String, Player> hashPlayers,
                                            ArrayList<Player> players) {
        String[] options = {"DEF", "MIG", "DAV", "POR"};
        Random random = new Random();

        for (Player player : players) {
            int randomIndex = random.nextInt(options.length);
            String newPosition = options[randomIndex];

            boolean realiza = Math.random() < 0.05;

            if (realiza) {
                player.setPosition(newPosition);
                hashPlayers.put(player.getName(), player);

                System.out.println("El jugador " + player.getName() + " ha cambiado a la posición: " + newPosition);
            }
        }
    }
    public static void printPlayerData(String playerName, HashMap<String, Player> players){
        Player player = players.get(playerName);
        if(player!= null){
            System.out.println("════════════════════════════════════════════");
            System.out.println("  Player Information: " + playerName);
            System.out.println("════════════════════════════════════════════");
            System.out.println("Name: " + player.getName());
            System.out.println("Surname: " + player.getSurName());
            System.out.println("Date of Birth: " + player.getBirthDay());
            System.out.println("Motivation: " + player.getMotivation() + " points");
            System.out.println("Annual Salary: $" + player.getAnualSalary());
            System.out.println("Position: " + player.getPosition());
            System.out.println("Quality Points: " + player.getCualityPoints());
            System.out.println("Jersey Number: " + player.getBack());
            System.out.println("════════════════════════════════════════════");
        } else {
            System.out.println("Player not found.");
        }
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
