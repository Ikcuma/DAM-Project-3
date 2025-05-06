package football_manager.controladores;

import football_manager.modulos.Coach;
import football_manager.modulos.Person;
import football_manager.modulos.Player;
import football_manager.modulos.Team;

import java.util.*;

import static football_manager.modulos.Person.capitalizeFirstLetterNames;
import static football_manager.modulos.Person.isNameDuplicate;

public class Person_controller {
    public static void createNewPerson(String option, HashMap<String, Person> hashPersons, ArrayList<Person> listPersons) {
        Scanner scanner = new Scanner(System.in);
        int motivation = 5;

        String personName;
        do {
            System.out.print("📛 Enter Name: ");
            personName = scanner.nextLine().trim();
            personName = Person.capitalizeFirstLetterNames(personName); // Pone la primera letra en mayúscula y el resto en minúscula

            if (Person.isNameDuplicate(personName, hashPersons)) { // Comprueba si ya existe alguien con ese nombre
                System.out.println("🚨 Name already exists! Please enter a different one.");
            }

        } while (Person.isNameDuplicate(personName, hashPersons));

        System.out.println("👨‍👩‍👧 Surname:");
        String personSurName = scanner.nextLine();

        System.out.println("🎂 Birthday (DD-MM-YYYY):");
        String birthday = scanner.nextLine();

        System.out.println("💰 Salary:");
        int salary = Person.validateIntegerInput(scanner); // Valida que se introduce un número entero

        Person newPerson = null;

        if (option.equalsIgnoreCase("Player")) {
            System.out.println("🎯 Back number:");
            int back = Person.validateIntegerInput(scanner); // Valida que el dorsal sea un número entero
            scanner.nextLine(); // limpiar buffer

            System.out.println("⚽ Position (DAV, POR, DEF, MIG):");
            String position = scanner.nextLine().toUpperCase();

            newPerson = Person.createPlayer(personName, personSurName, birthday, motivation, salary, back, position); // Crea un nuevo objeto Player

        } else if (option.equalsIgnoreCase("Coach")) {
            System.out.println("🏆 Victories:");
            int victories = Person.validateIntegerInput(scanner);
            scanner.nextLine();

            System.out.println("🌍 Selected for national team? (yes/no):");
            boolean nacional = scanner.nextLine().trim().equalsIgnoreCase("yes");
            // Crea un nuevo objeto Coach
            newPerson = Person.createCoach(personName, personSurName, birthday, motivation, salary, victories, nacional);

        } else if (option.equalsIgnoreCase("Owner")) {
            newPerson = Person.createOwner(personName, personSurName, birthday, motivation, salary); // Crea un nuevo objeto Person

        } else {
            System.out.println("❌ Invalid option. Use 'Player', 'Coach' or 'Owner'.");
            return;
        }

        listPersons.add(newPerson);
        hashPersons.put(personName, newPerson);

        System.out.println("✅ " + option + " successfully added!");
    }

    public static void createNewPersonMenu(HashMap<String, Person> hashPersons, ArrayList<Person> peopleList, Scanner sc) {
        System.out.println("\n\uD83D\uDC64 Register a New Person \uD83D\uDC64");
        System.out.println("=========================");
        System.out.println("Choose between Player, Coach, and Owner:");
        String optionPCO = capitalizeFirstLetterNames(sc.nextLine());
        createNewPerson(optionPCO, hashPersons, peopleList);
    }

    public static void modifyPresident(ArrayList<Team> teams, Scanner scanner) {
        System.out.println("Enter the name of the team whose president you want to modify:");
        String teamName = scanner.nextLine(); // Entrada del usuario

        // buscar el equipo
        Team teamToModify = Person.findTeamByName(teams, teamName);

        if (teamToModify != null) {
            System.out.println("Enter the name of the new president:");
            String newPresidentName = scanner.nextLine(); // Entrada del usuario

            // buscar el presidente
            Person newPresident = Person.findPersonByName(teams, newPresidentName);

            // Actualizar el presidente del equipo
            if (newPresident != null && Person.updatePresident(teamToModify, newPresident)) {
                System.out.println("President of team '" + teamName + "' has been updated to " + newPresident.getName());
            } else {
                System.out.println("President '" + newPresidentName + "' not found or update failed.");
            }
        } else {
            System.out.println("Team '" + teamName + "' not found.");
        }
    }
}