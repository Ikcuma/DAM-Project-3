package football_manager;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Person {
    protected final String name;
    protected final String surName;
    protected final String birthDay;
    protected int motivation; //(1-10)
    protected int anualSalary;

    //Contructors
    public Person(String name, String surName, String birthDay, int motivation, int anualSalary) {
        this.name = name;
        this.surName = surName;
        this.birthDay = birthDay;
        this.motivation = motivation;
        this.anualSalary = anualSalary;
    }
    //Getters
    public String getName() {
        return name;
    }

    public String getSurName() {
        return surName;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public int getMotivation() {
        return motivation;
    }

    public int getAnualSalary() {
        return anualSalary;
    }
    //Setters
    public void setMotivation(int motivation) {
        this.motivation = motivation;
    }

    public void setAnualSalary(int anualSalary) {
        this.anualSalary = anualSalary;
    }

    //methods
    public void train(){

    }

    public static void loadPersons(ArrayList<String> brutePersonData, ArrayList<Player> players, ArrayList<Coach> coaches, ArrayList<Person> owners) {
        String filePath = "C:\\Users\\dunkl\\IdeaProjects\\DAM-Project-3\\src\\src\\football_manager\\resources\\market_files.txt";

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = reader.readLine()) != null) {
                brutePersonData.add(line);
            }
        } catch (FileNotFoundException e) {
            System.out.println("file not found...");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (String personLine : brutePersonData) {
            String[] personData = personLine.split(";");
            if (personData[0].equals("J")) {
                Player player = new Player(personData[1], personData[2], personData[3], Integer.parseInt(personData[4]), Integer.parseInt(personData[5]), Integer.parseInt(personData[6]), personData[7], Integer.parseInt(personData[8]));
                players.add(player);
            } else if (personData[0].equals("E")) {
                Coach coach = new Coach(personData[1], personData[2], personData[3], Integer.parseInt(personData[4]), Integer.parseInt(personData[5]), Integer.parseInt(personData[6]), Boolean.parseBoolean(personData[7]));
                coaches.add(coach);
            } else{
                Person owner = new Person(personData[1], personData[2], personData[3], Integer.parseInt(personData[4]),Integer.parseInt(personData[5]));
                owners.add(owner);
            }
        }
    }

    public static void loadHashmaps(HashMap<String, Player> players, HashMap<String,Coach> coaches,HashMap<String, Person> owners, ArrayList<Player> playerArray,ArrayList<Coach> coachArray,ArrayList<Person> ownersArray) {
        for (Player player : playerArray) {
            players.put(player.getName(), player);
        }
        for (Coach coach : coachArray) {
            coaches.put(coach.getName(), coach);
        }
        for (Person owner : ownersArray) {
            owners.put(owner.getName(), owner);
        }
    }
}
