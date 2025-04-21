package football_manager.controladores;

import football_manager.modulos.Coach;
import football_manager.modulos.Person;
import football_manager.modulos.Player;
import football_manager.modulos.Team;

import java.util.*;

public class Person_controller {
    public static void createNewPerson(String option, HashMap<String, Person> hashPersons, ArrayList<Person> listPersons) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();
        int motivation = 5;

        String personName;
        do {
            System.out.print("\u001B[34m\uD83D\uDCDB Enter Name: \u001B[0m");
            personName = scanner.nextLine().trim();
            personName = capitalizeFirstLetterNames(personName);
            if (isNameDuplicate(personName, hashPersons)) {
                System.out.println("\u001B[31m\uD83D\uDEA8 Name already exists! Please enter a different one.\u001B[0m");
            }
        } while (isNameDuplicate(personName, hashPersons));

        System.out.println("\u001B[34m\uD83D\uDC68\u200D\uD83D\uDC69\u200D\uD83D\uDC67 Surname:\u001B[0m");
        String personSurName = scanner.nextLine();

        System.out.println("\u001B[34m\uD83C\uDF82 Birthday (DD-MM-YYYY):\u001B[0m");
        String birthday = scanner.nextLine();

        System.out.println("\u001B[34m\uD83D\uDCB0 Salary:\u001B[0m");
        int salary = validateIntegerInput(scanner);

        if (option.equalsIgnoreCase("Player")) {
            System.out.println("\u001B[34m\uD83C\uDFAF Back number:\u001B[0m");
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
            System.out.println("\u001B[34m\uD83C\uDFC6 Victories:\u001B[0m");
            int victories = validateIntegerInput(scanner);
            scanner.nextLine();
            System.out.println("\u001B[34m\uD83C\uDF0D Have you been selected for a national team? (yes/no):\u001B[0m");
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

    public static void createNewPersonMenu(HashMap<String, Person> hashPersons, ArrayList<Person> peopleList, Scanner sc) {
        System.out.println("\n\uD83D\uDC64 Register a New Person \uD83D\uDC64");
        System.out.println("=========================");
        System.out.println("Choose between Player, Coach, and Owner:");
        String optionPCO = capitalizeFirstLetterNames(sc.nextLine());
        createNewPerson(optionPCO, hashPersons, peopleList);
    }

    public static int validateIntegerInput(Scanner scanner) {
        while (true) {
            try {
                return scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("\u001B[31m\uD83D\uDEA8 Invalid input! Please enter a valid number.\u001B[0m");
                scanner.next();
            }
        }
    }

    public static boolean isNameDuplicate(String name, HashMap<String, Person> persons) {
        return persons.containsKey(name);
    }

    public static String capitalizeFirstLetterNames(String name) {
        if (name == null || name.isEmpty()) return name;
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
}