package football_manager;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

public class Person {
    protected final String name;
    protected final String surName;
    protected final String birthDay;
    protected int motivation; // (1-10)
    protected int anualSalary;

    // Constructors
    public Person(String name, String surName, String birthDay, int motivation, int anualSalary) {
        this.name = name;
        this.surName = surName;
        this.birthDay = birthDay;
        this.motivation = motivation;
        this.anualSalary = anualSalary;
    }

    // Getters


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

    // Setters
    public void setMotivation(int motivation) {
        this.motivation = motivation;
    }

    public void setAnualSalary(int anualSalary) {
        this.anualSalary = anualSalary;
    }

    // Methods
    public void train() {
        // Logic for training
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
            System.out.println("File not found...");
        } catch (IOException e) {
            throw new RuntimeException("Error reading file", e);
        }

        for (String personLine : brutePersonData) {
            String[] personData = personLine.split(";");

            if (personData.length >= 6) {
                if (personData[0].equals("J")) {
                    Player player = new Player(personData[1], personData[2], personData[3], Integer.parseInt(personData[4]),
                            Integer.parseInt(personData[5]), Integer.parseInt(personData[6]), personData[7], Integer.parseInt(personData[8]));
                    players.add(player);
                } else if (personData[0].equals("E")) {
                    if (personData.length >= 8) {
                        Coach coach = new Coach(personData[1], personData[2], personData[3], Integer.parseInt(personData[4]),
                                Integer.parseInt(personData[5]), Integer.parseInt(personData[6]), Boolean.parseBoolean(personData[7]));
                        coaches.add(coach);
                    }
                } else if (personData[0].equals("O")) {
                    if (personData.length >= 6) {
                        Person owner = new Person(personData[1], personData[2], personData[3],
                                Integer.parseInt(personData[4]), Integer.parseInt(personData[5]));
                        owners.add(owner);
                    }
                }
            } else {
                System.out.println("Línea inválida (menos de 6 campos): " + personLine);
            }
        }
    }

    public static void CreateNewPerson(String option) {
        try{
            Scanner scanner = new Scanner(System.in);
            String filePath = "C:\\Users\\dunkl\\IdeaProjects\\DAM-Project-3\\src\\src\\football_manager\\resources\\market_files.txt";
            RandomAccessFile raf = new RandomAccessFile(filePath, "rw");
            Random random = new Random();
            int motivation = 5;

            System.out.println("Name:");
            String personName = scanner.nextLine();
            System.out.println("surName");
            String personSurName = scanner.nextLine();
            System.out.println("birthday:");
            String birthday = scanner.nextLine();
            System.out.println("salary:");
            int salary = scanner.nextInt();
            if (option.equals("Player")) {
                System.out.println("back:");
                int back = scanner.nextInt();
                System.out.println("position(DAV,POR,DEF,MIG):");
                scanner.nextLine();
                String position = scanner.nextLine();
                int quality = random.nextInt(71) + 30;
                Player p = new Player(personName, personSurName,birthday,motivation,salary,back,position,quality);
                String playerData =
                        "J"+";"+
                        p.getName() + ";" +
                        p.getSurName() + ";" +
                        p.getBirthDay() + ";" +
                        p.getMotivation() + ";" +
                        p.getAnualSalary() + ";" +
                        p.getBack() + ";" +
                        p.getPosition() + ";" +
                        p.getCualityPoints() ;
                raf.writeBytes(playerData);
                raf.close();

                System.out.println("✅" +option+ "added");


            } else if (option.equals("Coach")) {
                System.out.println("victories:");
                int victories = scanner.nextInt();
            }
        } catch (IOException e) {
        System.out.println("❌ Error");
        e.printStackTrace();
    }


    }



    public static void loadHashmaps(HashMap<String, Player> players, HashMap<String, Coach> coaches, HashMap<String, Person> owners,
                                    ArrayList<Player> playerArray, ArrayList<Coach> coachArray, ArrayList<Person> ownersArray) {
        playerArray.forEach(player -> players.put(player.getName(), player));
        coachArray.forEach(coach -> coaches.put(coach.getName(), coach));
        ownersArray.forEach(owner -> owners.put(owner.getName(), owner));
    }
}
