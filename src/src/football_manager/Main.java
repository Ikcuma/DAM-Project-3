package football_manager;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        ArrayList<String> brutePersonData = new ArrayList<>();
        ArrayList<String> bruteTeamData = new ArrayList<>();
        ArrayList<Player> players = new ArrayList<>();
        ArrayList<Coach> coaches = new ArrayList<>();
        ArrayList<Person> owners = new ArrayList<>();
        ArrayList<Team> teams = new ArrayList<>();

        HashMap<String, Player> hashMapPlayers = new HashMap<>();
        HashMap<String, Coach> hashMapCoaches = new HashMap<>();
        HashMap<String, Person> hashMapOwners = new HashMap<>();

        try {
            Person.loadPersons(brutePersonData, players, coaches, owners);
            Person.loadHashmaps(hashMapPlayers, hashMapCoaches, hashMapOwners, players, coaches, owners);
            Team.loadTeams(bruteTeamData, teams, hashMapPlayers, hashMapCoaches, hashMapOwners);
        } catch (IOException e) {
            System.err.println("❌ Error loading data: " + e.getMessage());
            return;
        }
        chooseOptionMenu1(teams, hashMapPlayers, hashMapCoaches, hashMapOwners, owners, players, coaches);
    }

    private static void chooseOptionMenu1(ArrayList<Team> teams, HashMap<String, Player> hashPlayers,
                                          HashMap<String, Coach> hashCoaches, HashMap<String, Person> hashOwners,
                                          ArrayList<Person> owners, ArrayList<Player> players, ArrayList<Coach> coaches) {
        Scanner scanner = new Scanner(System.in);
        int option;
        do {
            printWelcome();
            option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 0 -> {
                    try {
                        reewriteFileMarket(players, coaches, owners);
                        reewriteTeamFile(teams);
                    } catch (IOException e) {
                        System.err.println("❌ Error saving data: " + e.getMessage());
                    }
                    System.exit(0);
                }
                case 1 -> System.out.println("\n🏆 View current league standings 🏆");
                case 2 -> manageTeamMenu(teams, scanner);
                case 3 -> Team.registerTeam(hashPlayers, hashCoaches, hashOwners, teams);
                case 4 -> createNewPersonMenu(scanner, hashPlayers, hashCoaches, hashOwners, owners, players, coaches);
                case 5 -> viewTeamDataMenu(teams, scanner);
                case 6 -> viewPersonDataMenu(hashPlayers, hashCoaches, scanner);
                case 7 -> manageMarketMenu(teams, hashPlayers, players, hashCoaches, coaches, scanner);
                default -> System.out.println("❌ Invalid option. Please try again.");
            }
        } while (option != 0);
    }

    private static void manageTeamMenu(ArrayList<Team> teams, Scanner scanner) {
        printManageTeam();
        int option = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (option) {
            case 0 -> chooseOptionMenu1(new ArrayList<>(), new HashMap<>(), new HashMap<>(), new HashMap<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
            case 1 -> Team.deregisterTeam(teams, scanner);
            case 2 -> Person.modifyPresident(teams, scanner);
            case 3 -> Coach.dismissCoach(teams, scanner);
            default -> System.out.println("❌ Invalid option. Please try again.");
        }
    }

    private static void createNewPersonMenu(Scanner scanner, HashMap<String, Player> hashPlayers, HashMap<String, Coach> hashCoaches, HashMap<String, Person> hashOwners, ArrayList<Person> owners, ArrayList<Player> players, ArrayList<Coach> coaches) {
        System.out.println("\n👤 Register a New Person 👤");
        System.out.println("=========================");
        System.out.println("Choose between Player, Coach, and Owner:");
        String optionPCO = capitalizeFirstLetterNames(scanner.nextLine());
        Person.createNewPerson(optionPCO, hashPlayers, hashCoaches, hashOwners, owners, players, coaches);
    }

    private static void viewTeamDataMenu(ArrayList<Team> teams, Scanner scanner) {
        System.out.println("\n📊 View Team Data 📊");
        System.out.println("===================");
        System.out.println("Enter the name of the team you want to check:");
        printTeamData(capitalizeFirstLetterNames(scanner.nextLine()), teams);
    }

    private static void viewPersonDataMenu(HashMap<String, Player> hashPlayers, HashMap<String, Coach> hashCoaches, Scanner scanner) {
        System.out.println("\n👤 View Person Data 👤");
        System.out.println("=====================");
        System.out.println("Enter 'Player' or 'Coach' to view their data:");
        String option = capitalizeFirstLetterNames(scanner.nextLine());
        System.out.println("What is the name of the"+option+"you want to check?");
        String optionName = capitalizeFirstLetterNames(scanner.nextLine());
        if ("Player".equals(option)) {
            Player.printPlayerData(optionName, hashPlayers);
        } else if ("Coach".equals(option)) {
            Coach.printCoachData(optionName, hashCoaches);
        }else {
            System.out.println("❌ Invalid option. Please choose between Player or Coach.");
        }
    }

    private static void manageMarketMenu(ArrayList<Team> teams, HashMap<String, Player> hashPlayers, ArrayList<Player> players, HashMap<String, Coach> hashCoaches, ArrayList<Coach> coaches, Scanner scanner) {
        printTraining();
        int option = scanner.nextInt();
        scanner.nextLine();

        switch (option) {
            case 0 -> chooseOptionMenu1(teams, hashPlayers, hashCoaches, new HashMap<>(), new ArrayList<>(), players, coaches);
            case 1 -> System.out.println("\n🔄 Transfer player or coach 🔄");
            case 2 -> changePlayerPositionMenu(hashPlayers, players, scanner);
            case 3 -> increaseCoachSalaryMenu(hashCoaches, coaches, scanner);
            default -> System.out.println("❌ Invalid option. Please try again.");
        }
    }

    private static void changePlayerPositionMenu(HashMap<String, Player> hashPlayers, ArrayList<Player> players, Scanner scanner) {
        System.out.println("\n🔄 Change Player Position 🔄");
        System.out.println("=========================");
        System.out.println("Enter the name of the player:");
        String playerName = scanner.nextLine();
        System.out.println("Enter the new position (DAV, POR, DEF, MIG):");
        String position = scanner.nextLine();
        playerName = capitalizeFirstLetterNames(playerName);
        position = position.toUpperCase();
        Player.changePlayerPosition(playerName, position, hashPlayers, players);
    }

    private static void increaseCoachSalaryMenu(HashMap<String, Coach> hashCoaches, ArrayList<Coach> coaches, Scanner scanner) {
        System.out.println("\n💸 Increase Coach Salary 💸");
        System.out.println("=========================");
        System.out.println("Enter the name of the coach:");
        String coachName = scanner.nextLine();
        coachName = capitalizeFirstLetterNames(coachName);
        Coach.increaseSalary(coachName, hashCoaches, coaches);
    }

    private static void printTeamData(String teamName, ArrayList<Team> teams) {
        for (Team team : teams) {
            if (team.getName().equalsIgnoreCase(teamName)) {
                System.out.println("\n📊 Team Data for " + teamName + " 📊");
                System.out.println("====================================");
                System.out.println("🏆 Team: " + team.getName());
                System.out.println("👑 President: " + team.getOwner().getName() + " " + team.getOwner().getSurName());
                System.out.println("🎽 Players:");
                for (Player player : team.getPlayers()) {
                    System.out.println("   - " + player.getName() + " " + player.getSurName() +
                            " | Position: " + player.getPosition() +
                            " | Motivation: " + player.getMotivation());
                }
                System.out.println("🎩 Coach: " + team.getCoach().getName() + " " + team.getCoach().getSurName() +
                        " | Motivation: " + team.getCoach().getMotivation());
                System.out.println("====================================\n");
                return;
            }
        }
        System.out.println("❌ Team '" + teamName + "' not found.");
    }

    private static void printManageTeam() {
        System.out.println("\n⚽ Team Manager ⚽");
        System.out.println("=================");
        System.out.println("1️⃣ - Deregister team ❌");
        System.out.println("2️⃣ - Modify president 👔");
        System.out.println("3️⃣ - Dismiss coach 🛑");
        System.out.println("0️⃣ - Exit 🚪");
        System.out.println("=================");
        System.out.print("Choose an option: ");
    }

    private static void printWelcome() {
        System.out.println("\n⚽ Welcome to Politècnics Football Manager ⚽");
        System.out.println("============================================");
        System.out.println("1️⃣ - View current league standings 🏆");
        System.out.println("2️⃣ - Manage team ⚽...");
        System.out.println("3️⃣ - Register a new team 🆕");
        System.out.println("4️⃣ - Register a new player, coach, or owner 👥");
        System.out.println("5️⃣ - View team data 📊");
        System.out.println("6️⃣ - View player or coach data 👤");
        System.out.println("7️⃣- Manage market ⚡...");
        System.out.println("0️⃣ - Exit 🚪");
        System.out.println("============================================");
        System.out.print("Choose an option: ");
    }

    private static void printTraining() {
        System.out.println("\n⚡ Transfer Market ⚡");
        System.out.println("===================");
        System.out.println("1️⃣ - Conduct training session 📑");
        System.out.println("2️⃣ - Change player position ⛓️‍💥");
        System.out.println("3️⃣ - Increase coach salary 💸");
        System.out.println("0️⃣ - Exit 🚪");
        System.out.println("===================");
        System.out.print("Choose an option: ");
    }

    public static String capitalizeFirstLetterNames(String name) {
        if (name == null || name.isEmpty()) {
            return name;
        }
        return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
    }

    public static void reewriteFileMarket(ArrayList<Player> players, ArrayList<Coach> coaches, ArrayList<Person> owners) throws IOException {
        String filePath = "C:\\Users\\dunkl\\IdeaProjects\\DAM-Project-3\\src\\src\\football_manager\\resources\\market_files.txt";

        try (BufferedWriter w = new BufferedWriter(new FileWriter(filePath))) {
            for (Player p : players) {
                String playerData = String.format("J;%s;%s;%s;%d;%d;%d;%s;%d%n",
                        p.getName(), p.getSurName(), p.getBirthDay(), p.getMotivation(),
                        p.getAnualSalary(), p.getBack(), p.getPosition(), p.getCualityPoints());
                w.write(playerData);
            }

            for (Coach c : coaches) {
                String coachData = String.format("E;%s;%s;%s;%d;%d;%d;%b%n",
                        c.getName(), c.getSurName(), c.getBirthDay(), c.getMotivation(),
                        c.getAnualSalary(), c.getVictories(), c.isNacional());
                w.write(coachData);
            }

            for (Person o : owners) {
                String ownerData = String.format("O;%s;%s;%s;%d;%d%n",
                        o.getName(), o.getSurName(), o.getBirthDay(), o.getMotivation(), o.getAnualSalary());
                w.write(ownerData);
            }
            System.out.println("✅ Changes saved successfully!");
        }
    }

    private static void reewriteTeamFile(ArrayList<Team> teams) throws IOException {
        String filePath = "C:\\Users\\dunkl\\IdeaProjects\\DAM-Project-3\\src\\src\\football_manager\\resources\\team_files.txt";

        try (BufferedWriter w = new BufferedWriter(new FileWriter(filePath))) {
            for (Team t : teams) {
                String coachName = t.getCoach().getName();
                String ownerName = t.getOwner().getName();

                StringBuilder playerNames = new StringBuilder();
                for (Player player : t.getPlayers()) {
                    playerNames.append(player.getName()).append(";");
                }

                if (playerNames.length() > 0) {
                    playerNames.setLength(playerNames.length() - 1);
                }

                String teamData = String.format("%s;%s;%s;%s;%s;%s%n",
                        t.getName(), t.getBirthDate(), t.getCity(),
                        coachName, ownerName, playerNames.toString());


                w.write(teamData);
            }
            System.out.println("✅ Team data saved successfully!");
        }
    }
}