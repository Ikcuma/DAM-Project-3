

// me da pereza trabajar

import football_manager.modulos.*;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    private static League currentLeague = null;

    public static void main(String[] args) {
        //Variables
        Scanner sc = new Scanner(System.in);
        String filePathTeam = "resources/team_files.txt";
        String filePathMarket = "resources/market_files.txt";
        ArrayList<Person> peopleList = new ArrayList<>();
        ArrayList<Team> teams = new ArrayList<>();
        HashMap<String, Person> hashMapPeople = new HashMap<>();
        //Cargar info

        try {
            loadFileToListMarket(peopleList, filePathMarket);
            Person.loadHashmaps(hashMapPeople, peopleList);
            loadFileToListTeam(teams, filePathTeam);
        } catch (IOException e) {
            System.err.println("❌ Error loading data: " + e.getMessage());
            return;
        }
        //Menu

        chooseOptionMenu1(teams, hashMapPeople, peopleList, sc);
        //Guardar Info

        try {
            loadListToFileMarket(peopleList, filePathMarket);
            loadListToFileTeam(teams, filePathTeam);
        } catch (IOException e) {
            System.err.println("❌ Error saving data: " + e.getMessage());
        }
    }

    private static void chooseOptionMenu1(ArrayList<Team> teams, HashMap<String, Person> hashMapPeople,
                                          ArrayList<Person> peopleList, Scanner sc) {
        int option;
        boolean exit = false;
        do {
            printWelcome();
            option = sc.nextInt();
            sc.nextLine();

            switch (option) {
                case 0 -> exit = true;
                case 1 -> viewLeagueStandings();
                case 2 -> manageTeamMenu(teams, peopleList, sc);
                case 3 -> Team.registerTeam(hashMapPeople, teams, sc);
                case 4 -> Person.createNewPersonMenu(hashMapPeople, peopleList, sc);
                case 5 -> viewTeamDataMenu(teams, sc);
                case 6 -> viewPersonDataMenu(hashMapPeople, sc);
                case 7 -> manageLeagueMenu(teams, sc);
                case 8 -> manageMarketMenu(teams, peopleList, hashMapPeople, sc);
                case 9 -> transferPlayerOrCoach(teams);
                default -> System.out.println("❌ Invalid option. Please try again.");
            }
        } while (!exit);
    }

    private static void manageLeagueMenu(ArrayList<Team> teams, Scanner sc) {
        if (currentLeague == null) {
            System.out.println("No active league. Would you like to create one? (yes/no)");
            String response = sc.nextLine().trim().toLowerCase();
            if (response.equals("yes")) {
                currentLeague = League.createNewLeague(teams, sc);
            } else {
                return;
            }
        }

        boolean exit = false;
        do {
            printLeagueMenu();
            int option = sc.nextInt();
            sc.nextLine();

            switch (option) {
                case 0 -> exit = true;
                case 1 -> currentLeague.showStandings();
                case 2 -> {
                    currentLeague.playAllMatches();
                    System.out.println("All matches have been played!");
                }
                case 3 -> currentLeague.showAllMatches();
                case 4 -> currentLeague.showMatchResults();
                case 5 -> {
                    Team topScorer = currentLeague.getTeamWithMostGoalsFor();
                    System.out.println("Top scoring team: " + topScorer.getName() +
                            " with " + currentLeague.getGoalsFor(topScorer) + " goals");
                }
                case 6 -> {
                    Team worstDefense = currentLeague.getTeamWithMostGoalsAgainst();
                    System.out.println("Team with most goals conceded: " + worstDefense.getName() +
                            " with " + currentLeague.getGoalsAgainst(worstDefense) + " goals");
                }
                case 7 -> {
                    currentLeague = League.createNewLeague(teams, sc);
                    System.out.println("New league created!");
                }
                default -> System.out.println("❌ Invalid option. Please try again.");
            }
        } while (!exit);
    }

    private static void viewLeagueStandings() {
        if (currentLeague == null) {
            System.out.println("No active league. Please create a new league first.");
            return;
        }
        currentLeague.showStandings();
    }

    private static void printLeagueMenu() {
        System.out.println("\n🏆 League Management 🏆");
        System.out.println("======================");
        System.out.println("1️⃣ - View Standings");
        System.out.println("2️⃣ - Play All Matches");
        System.out.println("3️⃣ - View All Matches");
        System.out.println("4️⃣ - View Match Results");
        System.out.println("5️⃣ - View Top Scoring Team");
        System.out.println("6️⃣ - View Worst Defense");
        System.out.println("0️⃣ - Back to Main Menu");
        System.out.println("======================");
        System.out.print("Choose an option: ");
    }

    private static void conductTrainingSession(HashMap<String, Person> hashPersons, ArrayList<Person> listPersons) {
        for (Person p : listPersons) {
            if (p instanceof Coach) {
                p.train();
                hashPersons.put(p.getName(), p);
            } else if (p instanceof Player) {
                p.train();
                hashPersons.put(p.getName(), p);
            }
        }
    }

    private static void manageTeamMenu(ArrayList<Team> teams, ArrayList<Person> people, Scanner sc) {
        Boolean exit = false;
        do {
            printManageTeam();
            int option = sc.nextInt();
            sc.nextLine();
            switch (option) {
                case 0 -> exit = true;
                case 1 -> Team.deregisterTeam(teams, sc);
                case 2 -> Person.modifyPresident(teams, sc);
                case 3 -> Coach.dismissCoach(teams, sc);
                case 4 -> addPersonToTeam(teams, people, sc);
                default -> System.out.println("❌ Invalid option. Please try again.");
            }
        } while (!exit);
        //chooseOptionMenu1(new ArrayList<>(),new HashMap<>(), new ArrayList<>());
    }


    private static void viewTeamDataMenu(ArrayList<Team> teams, Scanner sc) {
        System.out.println("\n📊 View Team Data 📊");
        System.out.println("===================");
        System.out.println("Enter the name of the team you want to check:");
        String input = sc.nextLine();
        input = capitalizeFirstLetterNames(input);
        printTeamData(input, teams);
    }

    // Comprobar que se lee bien las classes
    private static void viewPersonDataMenu(HashMap<String, Person> hashPersons, Scanner sc) {
        System.out.println("\n👤 View Person Data 👤");
        System.out.println("=====================");
        System.out.println("Enter 'Player' or 'Coach' to view their data:");
        String option = capitalizeFirstLetterNames(sc.nextLine());
        System.out.println("What is the name of the" + option + "you want to check?");
        String optionName = capitalizeFirstLetterNames(sc.nextLine());
        Person person = hashPersons.get(optionName);
        if (person instanceof Player) {
            Player player = (Player) person;
            player.printPersonData(player);
        } else if (person instanceof Coach) {
            Coach coach = (Coach) person;
            coach.printPersonData(coach);
        } else {
            System.out.println("❌ Invalid option. Please choose between Player or Coach.");
        }
    }

    private static void manageMarketMenu(ArrayList<Team> listTeam, ArrayList<Person> listPersons, HashMap<String, Person> hashPersons, Scanner sc) {
        Boolean exit = false;
        do {
            printTraining();
            int option = sc.nextInt();
            sc.nextLine();
            switch (option) {
                case 0 -> exit = true;
                case 1 -> transferPlayerOrCoach(listTeam);
                case 2 -> conductTrainingSession(hashPersons, listPersons);
                default -> System.out.println("❌ Invalid option. Please try again.");
            }
        } while (!exit);
        //chooseOptionMenu1(listTeam, hashPersons, listPersons);
    }

    public static void transferPlayerOrCoach(ArrayList<Team> teams) {
        Scanner sc = new Scanner(System.in);


        System.out.println("Enter the name of the team from which you want to transfer:");
        String fromTeamName = sc.nextLine();

        Team fromTeam = findTeam(teams, fromTeamName);
        if (isNull(fromTeam) == true) {
            return;
        }


        System.out.println("Enter the name of the team to which you want to transfer:");
        String toTeamName = sc.nextLine();

        Team toTeam = findTeam(teams, toTeamName);
        if (isNull(toTeam) == true) {
            return;
        }


        System.out.println("Are you transferring a player or a coach? (Enter 'player' or 'coach'):");
        String choice = sc.nextLine().trim().toLowerCase();

        if (choice.equals("player")) {
            System.out.println("Enter the name of the player to transfer:");
            String playerName = sc.nextLine();

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
                fromTeam.setCoach(null);
                toTeam.setCoach(coachToTransfer);
                System.out.println("Coach has been transferred successfully.");
            } else {
                System.out.println("No coach found in team '" + fromTeamName + "'.");
            }
        } else {
            System.out.println("No coach found in team '" + fromTeamName + "'.");
        }
    }

    private static Boolean isNull(Team team) {
        if (team == null) {
            System.out.println("Equipo no encontrado vuelve a escribir");
            return true;
        }
        return false;
    }

    private static Boolean isNull(Person person) {
        if (person == null) {
            return true;
        }
        return false;
    }

    private static Team findTeam(ArrayList<Team> teams, String name) {
        for (Team team : teams) {
            if (team.getName().equalsIgnoreCase(name)) {
                return team;
            }
        }
        return null;
    }

    private static Person findPerson(ArrayList<Person> people, String name) {
        for (Person person : people) {
            if (person.getName().equalsIgnoreCase(name)) {
                return person;
            }
        }
        System.out.println(name + " not found.");
        return null;
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
                for (Person person : team.getPlayers()) {
                    if (person instanceof Player player) {
                        System.out.printf("   - %s %s | Position: %s | Quality: %d | Motivation: %d | Salary: %d%n",
                                player.getName(),
                                player.getSurName(),
                                player.getPosition(),
                                player.getCualityPoints(),
                                player.getMotivation(),
                                player.getAnualSalary());
                    }
                }

                Coach coach = team.getCoach();
                if (coach != null) {
                    System.out.println("🎩 Coach: " + coach.getName() + " " + coach.getSurName() +
                            " | Victories: " + coach.getVictories() +
                            " | Motivation: " + coach.getMotivation() +
                            " | Salary: " + coach.getAnualSalary());
                } else {
                    System.out.println("🎩 Coach: No coach assigned.");
                }

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
        System.out.println("4️⃣ - Add (Player | Coach | Owner) to team");
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
        System.out.println("7️⃣ - Manage league 🏆...");
        System.out.println("8️⃣ - Transfer market ⚡...");
        System.out.println("9️⃣ - Transfer player");
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

    private static void loadListToFileTeam(ArrayList<Team> teams, String filePath) throws IOException {

        try (BufferedWriter w = new BufferedWriter(new FileWriter(filePath))) {
            for (Team t : teams) {

                List<Person> validPlayers = new ArrayList<>();
                for (Person player : t.getPlayers()) {
                    if (player instanceof Player) {
                        Player p = (Player) player;
                        if (p.getName() != null && !p.getName().isEmpty() &&
                                p.getSurName() != null && !p.getSurName().isEmpty() &&
                                p.getPosition() != null && !p.getPosition().isEmpty()) {
                            validPlayers.add(p);
                        }
                    }
                }

                Team validTeam = new Team(t.getName(), t.getBirthDate(), t.getCity(), t.getCoach(), t.getOwner(), validPlayers);
                w.write(validTeam.toFileFormat());
                w.newLine();
            }
            System.out.println("✅ Team data saved successfully!");
        }
    }

    private static void loadFileToListMarket(ArrayList<Person> peopleList, String filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] personData = line.split(";");
                if (personData.length < 6) {
                    System.out.println("Línea inválida (menos de 6 campos): " + line);
                }
                try {
                    switch (personData[0]) {
                        case "J":
                            if (personData.length >= 9) {
                                Player player = new Player(
                                        personData[1], personData[2], personData[3],
                                        Integer.parseInt(personData[4]), Integer.parseInt(personData[5]),
                                        Integer.parseInt(personData[6]), personData[7], Integer.parseInt(personData[8])
                                );
                                peopleList.add(player);
                            }
                            break;
                        case "E":
                            if (personData.length >= 8) {
                                Coach coach = new Coach(
                                        personData[1], personData[2], personData[3],
                                        Integer.parseInt(personData[4]), Integer.parseInt(personData[5]),
                                        Integer.parseInt(personData[6]), Boolean.parseBoolean(personData[7])
                                );
                                peopleList.add(coach);
                            }
                            break;
                        case "O":
                            if (personData.length >= 6) {
                                Person owner = new Person(
                                        personData[1], personData[2], personData[3],
                                        Integer.parseInt(personData[4]), Integer.parseInt(personData[5])
                                );
                                peopleList.add(owner);
                            }
                            break;

                        default:
                            System.out.println("Tipo de persona no reconocido: " + personData[0]);
                    }
                } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                    System.out.println("Error al procesar la línea: " + line + " - " + e.getMessage());
                }
            }
        }
    }

    public static void loadListToFileMarket(ArrayList<Person> peopleList, String filePath) throws IOException {
        try (BufferedWriter w = new BufferedWriter(new FileWriter(filePath))) {
            for (Person p : peopleList) {
                w.write(p.toFileFormat());
                w.newLine();
            }
            System.out.println("✅ Team data saved successfully!");
        }
    }

    public static void loadFileToListTeam(ArrayList<Team> teams, String filePath) throws IOException {
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


            Coach coach = null;
            String coachData = Coach.extractCoachData(rest);
            if (!coachData.equals("null") && !coachData.isEmpty()) {
                coach = Coach.parse(coachData);
            }

            Person owner = Person.parse(Person.extractPersonData(rest));

            if (owner == null) {
                System.out.println("Error: Dueño no válido para el equipo: " + teamName);
                continue;
            }

            List<Person> teamPlayers = new ArrayList<>();
            String playersData = rest.substring(
                    Coach.extractCoachData(rest).length() +
                            Person.extractPersonData(rest).length() + 2
            );

            Matcher playerMatcher = Pattern.compile("Player\\{[^}]+\\}").matcher(playersData);
            while (playerMatcher.find()) {
                Player player = Player.parse(playerMatcher.group());
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

    private static void addPersonToTeam(ArrayList<Team> teams, ArrayList<Person> people, Scanner sc) {
        Boolean exit = false;
        String classChosed;
        System.out.println("What team would you like to choose?");
        String teamChosed = capitalizeFirstLetterNames(sc.nextLine());

        Team team = findTeam(teams, teamChosed);
        if (isNull(team) == true) {
            System.out.println("team not found try again");
            return;
        }

        do {
            System.out.println("What would you like to choose to add to the team?(Owner | Player | Coach)");
            classChosed = capitalizeFirstLetterNames(sc.nextLine());
            exit = verifyIfClassisNull(team, classChosed);
        } while (!exit);


        System.out.println("What is the name of your " + classChosed + "?");
        String name = capitalizeFirstLetterNames(sc.nextLine());

        Person person = findPerson(people, name);
        if (classChosed.equals("Player") && isNull(team.getSpecificPlayer(name)) == false) {
            System.out.println("Player already exists try another one");
            return;
        }

        if (person instanceof Coach) {
            Coach c = (Coach) person;
            team.setCoach(c);
            System.out.println("Coach added succesfully");
        } else if (person instanceof Player) {
            Player p = (Player) person;
            team.addSpecificPlayer(p);
            System.out.println("Player added succesfully");
        } else {
            team.setOwner(person);
            System.out.println("Owner added succesfully");
        }
    }

    private static Boolean verifyIfClassisNull(Team team, String classChosed) {
        switch (classChosed) {
            case "Owner" -> {
                if (isNull(team.getOwner()) == false) {
                    System.out.println("The team Already have a owner");
                    return false;
                }
                return true;
            }
            case "Player" -> {
                return true;
            }
            case "Coach" -> {
                if (isNull(team.getCoach()) == false) {
                    System.out.println("The team Aleready have a Coach");
                    return false;
                }
                return true;
            }
            default -> {
                System.out.println("Wrong input please try again");
                return false;
            }
        }
    }

    public static String capitalizeFirstLetterNames(String name) {
        if (name == null || name.isEmpty()) {
            return name;
        }
        return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
    }
}