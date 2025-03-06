package football_manager;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

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
    /*public static void increaseSalary(String coachName) {
        String filePath = "C:\\Users\\dunkl\\IdeaProjects\\DAM-Project-3\\src\\src\\football_manager\\resources\\market_files.txt";

        char firstLetter = 'E';

        ArrayList<String[]> localPlayerData = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.charAt(0) == firstLetter){
                    String[] split = line.split(";");
                    localPlayerData.add(split);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found...");
        } catch (IOException e) {
            throw new RuntimeException("Error reading file", e);
        }
        for (int i = 0; i < localPlayerData.size(); i++) {
            String[] parts = localPlayerData.get(i);
            if (parts[1].equals(coachName)) {
                parts[7] = coachNewSalary;
                localPlayerData.set(i, parts);
                break;
            }
        }

    }*/

}
