e
package football_manager;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    public void train() {}

    public static void createNewPerson(String option, HashMap<String, Person> hashPersons, ArrayList<Person> listPersons) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();
        int motivation = 5;

        String personName;
        do {
            System.out.print("\u001B[34m📛 Enter Name: \u001B[0m");
            personName = scanner.nextLine().trim();
            personName = capitalizeFirstLetterNames(personName);
            if (isNameDuplicate(personName, hashPersons)) {
                System.out.println("\u001B[31m🚨 Name already exists! Please enter a different one.\u001B[0m");
            }
        } while (isNameDuplicate(personName,hashPersons));

        System.out.println("\u001B[34m👨‍👩‍👧‍👦 Surname:\u001B[0m");
        String personSurName = scanner.nextLine();

        System.out.println("\u001B[34m🎂 Birthday (DD-MM-YYYY):\u001B[0m");
        String birthday = scanner.nextLine();

        System.out.println("\u001B[34m💰 Salary:\u001B[0m");
        int salary = validateIntegerInput(scanner);

        if (option.equalsIgnoreCase("Player")) {
            System.out.println("\u001B[34m🔙 Back number:\u001B[0m");
            int back = validateIntegerInput(scanner);
            scanner.nextLine();
            System.out.println("\u001B[34m⚽ Position (DAV, POR, DEF, MIG):\u001B[0m");
            String position = scanner.nextLine().toUpperCase();

            int quality = random.nextInt(71) + 30;
            Person newPlayer = new Player(personName, personSurName, birthday, motivation, salary, back, position, quality);


            listPersons.add(newPlayer);
            hashPersons.put(personName, newPlayer);

            System.out.println("\u001B[32m✅ Player successfully added! ⚽\u001B[0m");

        } else if (option.equalsIgnoreCase("Coach")) {
            System.out.println("\u001B[34m🏆 Victories:\u001B[0m");
            int victories = validateIntegerInput(scanner);
            scanner.nextLine();
            System.out.println("\u001B[34m🌍 Have you been selected for a national team? (yes/no):\u001B[0m");
            boolean nacional = scanner.nextLine().trim().equalsIgnoreCase("yes");

            Person newCoach = new Coach(personName, personSurName, birthday, motivation, salary, victories, nacional);


            listPersons.add(newCoach);
            hashPersons.put(personName, newCoach);

            System.out.println("\u001B[32m✅ Coach successfully added! 🎓\u001B[0m");

        } else if (option.equalsIgnoreCase("Owner")) {
            Person newOwner = new Person(personName, personSurName, birthday, motivation, salary);


            listPersons.add(newOwner);
            hashPersons.put(personName, newOwner);

            System.out.println("\u001B[32m✅ Owner successfully added! 🏢\u001B[0m");

        } else {
            System.out.println("\u001B[31m❌ Error: Invalid option! Please choose 'Player', 'Coach', or 'Owner'.\u001B[0m");
        }
    }
    public void printPersonData(){}

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

    public static boolean isNameDuplicate(String name, HashMap<String, Person> persons) {
        return persons.containsKey(name);
    }

    public static String capitalizeFirstLetterNames(String name) {
        if (name == null || name.isEmpty()) {
            return name;
        }
        return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
    }

    public static void loadHashmaps(HashMap<String, Person> peopleHash, ArrayList<Person> peopleList) {
        peopleList.forEach(person -> peopleHash.put(person.getName(), person));
    }
    public static void modifyPresident(ArrayList<Team> teams, Scanner scanner) {
        System.out.println("Enter the name of the team whose president you want to modify:");
        String teamName = scanner.nextLine();

        Team teamToModify = null;
        for (Team team : teams) {
            if (team.getName().equalsIgnoreCase(teamName)) {
                teamToModify = team;
                break;
            }
        }

        if (teamToModify != null) {
            System.out.println("Enter the name of the new president:");
            String newPresidentName = scanner.nextLine();

            Person newPresident = teamToModify.getOwner();
            for (Person owner : teams.stream().map(Team::getOwner).toList()) {
                if (owner.getName().equalsIgnoreCase(newPresidentName)) {
                    newPresident = owner;
                    break;
                }
            }

            if (newPresident != null) {
                teamToModify.setOwner(newPresident);
                System.out.println("President of team '" + teamName + "' has been updated to " + newPresident.getName());
            } else {
                System.out.println("President '" + newPresidentName + "' not found.");
            }
        } else {
            System.out.println("Team '" + teamName + "' not found.");
        }
    }
    public String toFileFormat() {
        return String.format("O;%s;%s;%s;%d;%d", name, surName, birthDay, motivation, anualSalary);
    }
    public String toFileFormatTeam() {
        return "Person{name='" + this.name +
                "', surName='" + this.surName +
                "', birthDay='" + this.birthDay +
                "', motivation=" + this.motivation +
                ", anualSalary=" + this.anualSalary + "}";
    }
    public static String extractPersonData(String data) {
        Pattern pattern = Pattern.compile("Person\\{[^}]+\\}");
        Matcher matcher = pattern.matcher(data);
        return matcher.find() ? matcher.group(0) : "";
    }

    public static Person parse(String data) {
        if (data.isEmpty()) {
            return null;
        }

        Pattern pattern = Pattern.compile(
                "Person\\{name='(.*?)', surName='(.*?)', birthDay='(.*?)', motivation=(\\d+), anualSalary=(\\d+)\\}"
        );
        Matcher matcher = pattern.matcher(data);

        if (matcher.find()) {
            return new Person(
                    matcher.group(1),
                    matcher.group(2),
                    matcher.group(3),
                    Integer.parseInt(matcher.group(4)),
                    Integer.parseInt(matcher.group(5))
            );
        }
        return null;
    }
    public static void createNewPersonMenu(HashMap<String, Person> hashPersons, ArrayList<Person> peopleList, Scanner sc) {
        System.out.println("\n👤 Register a New Person 👤");
        System.out.println("=========================");
        System.out.println("Choose between Player, Coach, and Owner:");
        String optionPCO = capitalizeFirstLetterNames(sc.nextLine());
        Person.createNewPerson(optionPCO, hashPersons, peopleList);
    }


    @Override
    public String toString() {
        return "Person{" +
                "name='" + this.name + '\'' +
                ", surName='" + this.surName + '\'' +
                ", birthDay='" + this.birthDay + '\'' +
                ", motivation=" + this.motivation +
                ", anualSalary=" + this.anualSalary +
                '}';
    }
}