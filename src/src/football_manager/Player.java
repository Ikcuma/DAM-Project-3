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


        public static void changePlayerPosition(String PlayerName, String PlayerPosition) {
            boolean realiza = Math.random() < 0.05;
            if (realiza) {
                    String filePath = "C:\\Users\\dunkl\\IdeaProjects\\DAM-Project-3\\src\\src\\football_manager\\resources\\market_files.txt";

                    try {
                        RandomAccessFile raf = new RandomAccessFile(filePath, "rw");
                        String line;
                        long position = 0;
                        boolean found = false;

                        while ((line = raf.readLine()) != null) {
                            long currentPos = raf.getFilePointer();
                            String[] parts = line.split(";");

                            if (parts.length > 7 && parts[1].equals(PlayerName)) {
                                parts[7] = PlayerPosition;
                                String newLine = String.join(";", parts);

                                raf.seek(position);
                                raf.writeBytes(newLine);

                                if (newLine.length() < line.length()) {
                                    for (int i = newLine.length(); i < line.length(); i++) {
                                        raf.writeBytes(" ");
                                    }
                                }
                                found = true;
                                break;
                            }
                            position = currentPos;
                        }

                        raf.close();

                        if (found) {
                            System.out.println("Posición de " + PlayerName + " cambiada a " + PlayerPosition);
                        } else {
                            System.out.println("Jugador no encontrado.");
                        }

                    } catch (IOException e) {
                        System.out.println("Error al modificar el archivo.");
                        e.printStackTrace();
                    }
                } else {
                System.out.println("Inténtalo de nuevo.");
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
}
e

