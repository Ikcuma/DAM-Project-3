package football_manager;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Coach extends Person
{
    private int victories;
    private boolean nacional;

    //Contructors
    public Coach(String name, String surName, String birthDay, int motivation, int anualSalary, int victories, boolean nacional) {
        super(name, surName, birthDay, motivation, anualSalary);
        this.victories = victories;
        this.nacional = nacional;
    }


    //Getters
    public int getVictories() {
        return victories;
    }

    public boolean isNacional() {
        return nacional;
    }
    //Setters
    public void setVictories(int victories) {
        this.victories = victories;
    }

    public void setNacional(boolean nacional) {
        this.nacional = nacional;
    }

    //Methods
    public static void increaseSalary(String coachName) {
        String filePath = "C:\\Users\\dunkl\\IdeaProjects\\DAM-Project-3\\src\\src\\football_manager\\resources\\market_files.txt";

        try {
            RandomAccessFile raf = new RandomAccessFile(filePath, "rw");
            String line;
            long position = 0;
            boolean found = false;

            while ((line = raf.readLine()) != null) {
                long currentPos = raf.getFilePointer();
                String[] parts = line.split(";");

                if (parts.length > 7 && parts[1].equals(coachName)) {
                    int value = Integer.parseInt(parts[5]);
                    value += value * 0.05;
                    parts[5] = String.valueOf(value);
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
                System.out.println("Salary increased of coach: " + coachName );
            } else {
                System.out.println("Coach not found");
            }

        } catch (IOException e) {
            System.out.println("Error al modificar el archivo.");
            e.printStackTrace();
        }

    }
    public static void printCoachData(String coachName, HashMap<String, Coach> coaches){
            Coach coach = coaches.get(coachName);
            if(coach!= null){
                System.out.println("════════════════════════════════════════════");
                System.out.println("  Player Information: " + coachName);
                System.out.println("════════════════════════════════════════════");
                System.out.println("Name: " + coach.getName());
                System.out.println("Surname: " + coach.getSurName());
                System.out.println("Date of Birth: " + coach.getBirthDay());
                System.out.println("Motivation: " + coach.getMotivation() + " points");
                System.out.println("Annual Salary: $" + coach.getAnualSalary());
                System.out.println("Victories: " + coach.getVictories());
                System.out.println("Is he nacional?: " + coach.isNacional());
                System.out.println("════════════════════════════════════════════");
            } else {
                System.out.println("Coach not found.");
            }
        }
    }

