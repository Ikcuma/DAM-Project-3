package football_manager;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import static java.util.stream.Collectors.toList;

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
            Team.loadTeams(bruteTeamData, teams);
            System.out.println(bruteTeamData);
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

    private static void conductTrainingSession(HashMap<String, Player> hashPlayers, HashMap<String, Coach> hashCoaches, ArrayList<Player> players, ArrayList<Coach> coaches) {
        Player.changePlayerPosition( hashPlayers, players);
        Coach.increaseSalary( hashCoaches, coaches);
    }

    private static void manageTeamMenu(ArrayList<Team> teams, Scanner scanner) {
        printManageTeam();
        int option = scanner.nextInt();
        scanner.nextLine();

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
        String input = scanner.nextLine();
        input = capitalizeFirstLetterNames(input);
        printTeamData(input, teams);
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
            case 1 -> transferPlayerOrCoach(teams, scanner);
            case 2 -> conductTrainingSession(hashPlayers,hashCoaches,players,coaches);
            default -> System.out.println("❌ Invalid option. Please try again.");
        }
    }

    public static void transferPlayerOrCoach(ArrayList<Team> teams, Scanner scanner) {
        System.out.println("Enter the name of the team from which you want to transfer:");
        String fromTeamName = scanner.nextLine();

        Team fromTeam = null;
        for (Team team : teams) {
            if (team.getName().equalsIgnoreCase(fromTeamName)) {
                fromTeam = team;
                break;
            }
        }

        if (fromTeam == null) {
            System.out.println("Team '" + fromTeamName + "' not found.");
            return;
        }

        System.out.println("Enter the name of the team to which you want to transfer:");
        String toTeamName = scanner.nextLine();

        Team toTeam = null;
        for (Team team : teams) {
            if (team.getName().equalsIgnoreCase(toTeamName)) {
                toTeam = team;
                break;
            }
        }

        if (toTeam == null) {
            System.out.println("Team '" + toTeamName + "' not found.");
            return;
        }

        System.out.println("Are you transferring a player or a coach? (Enter 'player' or 'coach'):");
        String choice = scanner.nextLine().trim().toLowerCase();

        if (choice.equals("player")) {
            System.out.println("Enter the name of the player to transfer:");
            String playerName = scanner.nextLine();

            Player playerToTransfer = fromTeam.getSpecificPlayer(playerName);

            if (playerToTransfer != null) {
                fromTeam.getPlayers().remove(playerToTransfer);
                toTeam.getPlayers().add(playerToTransfer);
                System.out.println("Player '" + playerName + "' has been transferred from '" + fromTeamName + "' to '" + toTeamName + "'.");
            } else {
                System.out.println("Player '" + playerName + "' not found in team '" + fromTeamName + "'.");
            }
        } else if (choice.equals("coach")) {
            System.out.println("Transferring the coach from '" + fromTeamName + "' to '" + toTeamName + "'.");

            Coach coachToTransfer = fromTeam.getCoach();

            if (coachToTransfer != null) {
                toTeam.setCoach(coachToTransfer);
                fromTeam.setCoach(null);
                System.out.println("Coach has been transferred successfully.");
            } else {
                System.out.println("No coach found in team '" + fromTeamName + "'.");
            }
        } else {
            System.out.println("Invalid choice. Please enter 'player' or 'coach'.");
        }
    }






    public static void printTeamData(String teamName, ArrayList<Team> teams) {
        for (Team team : teams) {
            if (team.getName().equals(teamName)) {
                System.out.println("\n📊 Team Data for " + teamName + " 📊");
                System.out.println("====================================");
                System.out.println("🏆 Team: " + team.getName());
                System.out.println("📅 Founded: " + team.getBirthDate());
                System.out.println("🌍 City: " + team.getCity());
                System.out.println("👑 President: " + team.getOwner().getName() + " " + team.getOwner().getSurName());
                System.out.println("💰 Annual Salary: " + team.getOwner().getAnualSalary());
                System.out.println("🎽 Players:");
                for (Player player : team.getPlayers()) {
                    System.out.println("   - " + player.getName() + " " + player.getSurName() +
                            " | Position: " + player.getPosition() +
                            " | Quality Points: " + player.getCualityPoints() +
                            " | Motivation: " + player.getMotivation() +
                            " | Salary: " + player.getAnualSalary());
                }
                System.out.println("🎩 Coach: " + team.getCoach().getName() + " " + team.getCoach().getSurName() +
                        " | Victories: " + team.getCoach().getVictories() +
                        " | Motivation: " + team.getCoach().getMotivation() +
                        " | Salary: " + team.getCoach().getAnualSalary());
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
        System.out.println("7️⃣ - Manage market ⚡...");
        System.out.println("0️⃣ - Exit 🚪");
        System.out.println("============================================");
        System.out.print("Choose an option: ");
    }

    private static void printTraining() {
        System.out.println("\n⚡ Transfer Market ⚡");
        System.out.println("===================");
        System.out.println("1️⃣ - Transfer Coach or Player 📑");
        System.out.println("2️⃣ - Conduct Training Session 👟");
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
                String coach = t.getCoach().toString();
                String owner = t.getOwner().toString();

                StringBuilder playerString = new StringBuilder();
                for (Player player : t.getPlayers()) {
                    playerString.append(player.toString()).append(";");
                }

                if (playerString.length() > 0) {
                    playerString.setLength(playerString.length() - 1);
                }

                String teamData = String.format("%s;%s;%s;%s;%s;%s%n",
                        t.getName(), t.getBirthDate(), t.getCity(),
                        coach, owner, playerString);


                w.write(teamData);
            }
            System.out.println("✅ Team data saved successfully!");
        }
    }
}