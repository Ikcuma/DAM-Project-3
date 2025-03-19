package football_manager;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static football_manager.Team.extractCoachData;
import static java.util.stream.Collectors.toList;

public class Main {

    public static void main(String[] args) {

        ArrayList<Person> peopleList = new ArrayList<>();
        ArrayList<Team> teams = new ArrayList<>();
        HashMap<String, Person> hashMapPeople = new HashMap<>();

        try {
            loadFileToList(peopleList);
            Person.loadHashmaps(hashMapPeople, peopleList);
            loadTeams( teams);
        } catch (IOException e) {
            System.err.println("❌ Error loading data: " + e.getMessage());
            return;
        }
        chooseOptionMenu1(teams, hashMapPeople, peopleList);
    }

    private static void chooseOptionMenu1(ArrayList<Team> teams, HashMap<String, Person> peopleHash, ArrayList<Person> peopleList) {
        Scanner scanner = new Scanner(System.in);
        int option;
        do {
            printWelcome();
            option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 0 -> {
                    try {
                        reewriteFileMarket(peopleList);
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
    private static void loadFileToList(ArrayList<Person> peopleList)throws IOException{
        String filePath = "C:\\Users\\dunkl\\IdeaProjects\\DAM-Project-3\\src\\src\\football_manager\\resources\\market_files.txt";
        ArrayList<String> brutePersonData = new ArrayList<String>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                brutePersonData.add(line);
            }
        }
        for (String personLine : brutePersonData) {
            String[] personData = personLine.split(";");
            if (personData.length >= 6) {
                if (personData[0].equals("J")) {
                    Person player = new Player(personData[1], personData[2], personData[3], Integer.parseInt(personData[4]),
                            Integer.parseInt(personData[5]), Integer.parseInt(personData[6]), personData[7], Integer.parseInt(personData[8]));
                    peopleList.add(player);
                } else if (personData[0].equals("E")) {
                    if (personData.length >= 8) {
                        Person coach = new Coach(personData[1], personData[2], personData[3], Integer.parseInt(personData[4]),
                                Integer.parseInt(personData[5]), Integer.parseInt(personData[6]), Boolean.parseBoolean(personData[7]));
                        peopleList.add(coach);
                    }
                } else if (personData[0].equals("O")) {
                    if (personData.length >= 6) {
                        Person owner = new Person(personData[1], personData[2], personData[3],
                                Integer.parseInt(personData[4]), Integer.parseInt(personData[5]));
                        peopleList.add(owner);
                    }
                }
            } else {
                System.out.println("Línea inválida (menos de 6 campos): " + personLine);
            }
        }
        System.out.println(peopleList);
    }

    public static void reewriteFileMarket(ArrayList<Person> peopleList) throws IOException {
        String filePath = "C:\\Users\\dunkl\\IdeaProjects\\DAM-Project-3\\src\\src\\football_manager\\resources\\market_files.txt";
        try(BufferedWriter w = new BufferedWriter(new FileWriter(filePath))){
            for (Person p : peopleList) {
                w.write(p.toFileFormat());
                w.newLine();
            }
            System.out.println("✅ Team data saved successfully!");
        }
    }

    public static void loadTeams(ArrayList<Team> teams) throws IOException {
        String filePath = "C:\\Users\\dunkl\\IdeaProjects\\DAM-Project-3\\src\\src\\football_manager\\resources\\team_files.txt";
        ArrayList<String> bruteTeamData = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                bruteTeamData.add(line);
            }
        }

        for (String line : bruteTeamData) {
            String[] parts = line.split(";", 4);
            if (parts.length < 4) {
                System.out.println("Error: Formato incorrecto en línea: " + line);
                continue;
            }

            String teamName = parts[0];
            String birthDate = parts[1];
            String city = parts[2];
            String rest = parts[3];

            Coach coach = parseCoach(extractCoachData(rest));
            Person owner = parsePerson(extractOwnerData(rest));

            if (coach == null) {
                System.out.println("Error: Coach no válido para el equipo: " + teamName);
            }
            if (owner == null) {
                System.out.println("Error: Person (dueño) no válido para el equipo: " + teamName);
            }

            if (coach == null || owner == null) {
                System.out.println("Error: Entrenador o dueño no válido para el equipo: " + teamName);
                continue;
            }

            List<Player> teamPlayers = new ArrayList<>();
            String playersData = rest.substring(extractCoachData(rest).length() + extractOwnerData(rest).length());
            Matcher playerMatcher = Pattern.compile("Player\\{back=(\\d+), position='(.*?)', cualityPoints=(\\d+), name='(.*?)', surName='(.*?)', birthDay='(.*?)', motivation=(\\d+), anualSalary=(\\d+)\\}").matcher(playersData);

            while (playerMatcher.find()) {
                Player player = parsePlayer(playerMatcher.group());
                if (player != null) {
                    teamPlayers.add(player);
                } else {
                    System.out.println("Error: No se pudo procesar un jugador en el equipo: " + teamName);
                }
            }

            try {
                Team team = new Team(teamName, birthDate, city, coach, owner, teamPlayers);
                teams.add(team);
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage() + " para el equipo: " + teamName);
            }
        }
    }
    private static String extractCoachData(String data) {
        Matcher matcher = Pattern.compile("Coach\\{.*?\\}").matcher(data);
        if (matcher.find()) {
            return matcher.group();
        }
        return "";
    }

    private static String extractOwnerData(String data) {
        Matcher matcher = Pattern.compile("Person\\{.*?\\}").matcher(data);
        if (matcher.find()) {
            return matcher.group();
        }
        return "";
    }

    private static Coach parseCoach(String data) {
        Matcher matcher = Pattern.compile("Coach\\{victories=(\\d+), nacional=(true|false), name='(.*?)', surName='(.*?)', birthDay='(.*?)', motivation=(\\d+), anualSalary=(\\d+)\\}").matcher(data);
        if (matcher.find()) {
            return new Coach(matcher.group(3), matcher.group(4), matcher.group(5), Integer.parseInt(matcher.group(6)), Integer.parseInt(matcher.group(7)), Integer.parseInt(matcher.group(1)), Boolean.parseBoolean(matcher.group(2)));
        }
        return null;
    }

    private static Person parsePerson(String data) {
        Matcher matcher = Pattern.compile("Person\\{name='(.*?)', surName='(.*?)', birthDay='(.*?)', motivation=(\\d+), anualSalary=(\\d+)\\}").matcher(data);
        if (matcher.find()) {
            return new Person(matcher.group(1), matcher.group(2), matcher.group(3), Integer.parseInt(matcher.group(4)), Integer.parseInt(matcher.group(5)));
        }
        return null;
    }

    private static Player parsePlayer(String data) {
        Matcher matcher = Pattern.compile("Player\\{back=(\\d+), position='(.*?)', cualityPoints=(\\d+), name='(.*?)', surName='(.*?)', birthDay='(.*?)', motivation=(\\d+), anualSalary=(\\d+)\\}").matcher(data);
        if (matcher.find()) {
            return new Player(matcher.group(4), matcher.group(5), matcher.group(6), Integer.parseInt(matcher.group(7)), Integer.parseInt(matcher.group(8)), Integer.parseInt(matcher.group(1)), matcher.group(2), Integer.parseInt(matcher.group(3)));
        }
        return null;
    }
}