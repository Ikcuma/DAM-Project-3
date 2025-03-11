package football_manager;

import java.io.*;
import java.util.*;

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

    public static void createNewPerson(String option, HashMap<String, Player> players, HashMap<String, Coach> coaches, HashMap<String, Person> owners) {
            Scanner scanner = new Scanner(System.in);
            String filePath = "C:\\Users\\dunkl\\IdeaProjects\\DAM-Project-3\\src\\src\\football_manager\\resources\\market_files.txt";
            Random random = new Random();
            int motivation = 5;

            try (RandomAccessFile raf = new RandomAccessFile(filePath, "rw")) {
                String personName;
                do {
                    System.out.print("\u001B[34m📛 Enter Name: \u001B[0m");
                    personName = scanner.nextLine().trim();
                    personName = capitalizeFirstLetterNames(personName);
                    if (isNameDuplicate(personName, players, coaches, owners)) {
                        System.out.println("\u001B[31m🚨 Name already exists! Please enter a different one.\u001B[0m");
                    }
                } while (isNameDuplicate(personName, players, coaches, owners));

                System.out.println("\u001B[34m👨‍👩‍👧‍👦 Surname:\u001B[0m");
                String personSurName = scanner.nextLine();

                System.out.println("\u001B[34m🎂 Birthday (DD-MM-YYYY):\u001B[0m");
                String birthday = scanner.nextLine();

                System.out.println("\u001B[34m💰 Salary:\u001B[0m");
                int salary = validateIntegerInput(scanner);

                if (option.equalsIgnoreCase("Player")) {
                    System.out.println("\u001B[34m🔙 Back number:\u001B[0m");
                    int back = validateIntegerInput(scanner);

                    System.out.println("\u001B[34m⚽ Position (DAV, POR, DEF, MIG):\u001B[0m");
                    String position = scanner.nextLine().toUpperCase();

                    int quality = random.nextInt(71) + 30;
                    Player p = new Player(personName, personSurName, birthday, motivation, salary, back, position, quality);
                    String playerData = String.format("J;%s;%s;%s;%d;%d;%d;%s;%d%n",
                            p.getName(), p.getSurName(), p.getBirthDay(), p.getMotivation(),
                            p.getAnualSalary(), p.getBack(), p.getPosition(), p.getCualityPoints());

                    raf.writeBytes(playerData);
                    System.out.println("\u001B[32m✅ Player successfully added! ⚽\u001B[0m");

                } else if (option.equalsIgnoreCase("Coach")) {
                    System.out.println("\u001B[34m🏆 Victories:\u001B[0m");
                    int victories = validateIntegerInput(scanner);
                    scanner.nextLine();
                    System.out.println("\u001B[34m🌍 Have you been selected for a national team? (yes/no):\u001B[0m");
                    boolean nacional = scanner.nextLine().trim().equalsIgnoreCase("yes");

                    Coach c = new Coach(personName, personSurName, birthday, motivation, salary, victories, nacional);
                    String coachData = String.format("E;%s;%s;%s;%d;%d;%d;%  b%n",
                            c.getName(), c.getSurName(), c.getBirthDay(), c.getMotivation(),
                            c.getAnualSalary(), c.getVictories(), c.isNacional());

                    raf.writeBytes(coachData);
                    System.out.println("\u001B[32m✅ Coach successfully added! 🎓\u001B[0m");

                } else if (option.equalsIgnoreCase("Owner")) {
                    Person p = new Person(personName, personSurName, birthday, motivation, salary);
                    String personData = String.format("O;%s;%s;%s;%d;%d%n",
                            p.getName(), p.getSurName(), p.getBirthDay(), p.getMotivation(), p.getAnualSalary());

                    raf.writeBytes(personData);
                    System.out.println("\u001B[32m✅ Owner successfully added! 🏢\u001B[0m");

                } else {
                    System.out.println("\u001B[31m❌ Error: Invalid option! Please choose 'Player', 'Coach', or 'Owner'.\u001B[0m");
                }

            } catch (IOException e) {
                System.out.println("\u001B[31m❌ An error occurred while writing to the file.\u001B[0m");
                e.printStackTrace();
            }
        }

        public static int validateIntegerInput(Scanner scanner) {
            while (true) {
                try {
                    return scanner.nextInt();
                } catch (InputMismatchException e) {
                    System.out.println("\u001B[31m🚨 Invalid input! Please enter a valid number.\u001B[0m");
                    scanner.next();
                }
            }
        }
    public static boolean isNameDuplicate(String name,HashMap<String, Player> players, HashMap<String, Coach> coaches, HashMap<String, Person> owners) {
        return players.containsKey(name) || coaches.containsKey(name) || owners.containsKey(name);
    }

    public static String capitalizeFirstLetterNames(String name) {
        if (name == null || name.isEmpty()) {
            return name;
        }
        return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
    }


    public static void loadHashmaps(HashMap<String, Player> players, HashMap<String, Coach> coaches, HashMap<String, Person> owners,
                                    ArrayList<Player> playerArray, ArrayList<Coach> coachArray, ArrayList<Person> ownersArray) {
        playerArray.forEach(player -> players.put(player.getName(), player));
        coachArray.forEach(coach -> coaches.put(coach.getName(), coach));
        ownersArray.forEach(owner -> owners.put(owner.getName(), owner));
    }
}
