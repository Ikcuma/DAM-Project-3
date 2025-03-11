package football_manager;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

public class Main {

    public static void main(String[] args)  {

        ArrayList<String> brutePersonData = new ArrayList<>();
        ArrayList<String> bruteTeamData = new ArrayList<>();
        ArrayList<Player> players = new ArrayList<>();
        ArrayList<Coach> coaches = new ArrayList<>();
        ArrayList<Person> owners = new ArrayList<>();
        ArrayList<Team> teams = new ArrayList<>();

        HashMap<String, Player> hashMapPlayers = new HashMap<>();
        HashMap<String, Coach> hashMapCoaches = new HashMap<>();
        HashMap<String, Person> hashMapOwners = new HashMap<>();


        Person.loadPersons(brutePersonData, players, coaches, owners);
        Person.loadHashmaps(hashMapPlayers,hashMapCoaches,hashMapOwners,players,coaches,owners);
        Team.loadTeams(bruteTeamData,teams,hashMapPlayers,hashMapCoaches,hashMapOwners);

        printWelcome();
        chooseOptionMenu1(teams, hashMapPlayers,hashMapCoaches,hashMapOwners);
        //HACER UN METODOS PARA REESCRIBIR LOS ARCHIVOS OSEA EDITAS LOS ARRAYS O HASHMAPS Y LUEGO EL METODO LO PILLO Y REESCRIBE TODO, reescriir lo archivo siempre que la aplicacion cierre


        reewriteFileMarket(players, coaches, owners, teams);

    }


    private static void chooseOptionMenu1(ArrayList<Team> teams, HashMap<String, Player> players, HashMap<String, Coach> coaches, HashMap<String, Person> owners) {
        Scanner scanner = new Scanner(System.in);
        int option = scanner.nextInt();
        switch (option) {
            case 0:
                System.exit(0);
                break;
            case 1:
                System.out.println("View current league standings");
                break;
            case 2:
                printManageTeam();
                chooseOptionMenu2(teams);
                break;
            case 3:
                System.out.println("Register a new team ���");
                break;
            case 4:
                System.out.println("Register a new player, coach or owner ��");
                System.out.println("Choose between Player, Coach and Owner");
                scanner.nextLine();
                String optionPCO = scanner.nextLine();
                optionPCO = capitalizeFirstLetterNames(optionPCO);
                Person.createNewPerson(optionPCO, players, coaches, owners);

                break;
            case 5:
                System.out.println("View team data");
                System.out.println("What team would you like to check the data?");
                scanner.nextLine();
                String teamName = scanner.nextLine();
                teamName = capitalizeFirstLetterNames(teamName);
                printTeamData(teamName, teams);
                break;
            case 6:
                System.out.println("View player or coach data");
                break;
            case 7:
                System.out.println("View player or coach statistics");
                break;
            case 8:
                printTraining();
                chooseOptionMenu3();
                break;
            default:



        }
    }

    private static void printTeamData(String teamName, ArrayList<Team> teams) {
        for (Team team : teams) {
            if (team.getName().equalsIgnoreCase(teamName)) {
                System.out.println("**************************************************");
                System.out.println("                 TEAM DATA 📊");
                System.out.println("**************************************************");
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
                System.out.println("**************************************************");
                return;
            }
        }
    }
    private static void chooseOptionMenu2(HashMap <String, Team> teams) {
        Scanner scanner = new Scanner(System.in);
        int option = scanner.nextInt();
        switch (option) {
            case 0:
                chooseOptionMenu1(new ArrayList<>(), new HashMap<>(),new HashMap<>(),new HashMap<>());
                break;
            case 1:
                System.out.println("Deregister team");
                Team.deregisterTeam(teams);
                break;
            case 2:
                System.out.println("Modify president");
                break;
            case 3:
                System.out.println("Dismiss coach");
                break;
            case 4:
                System.out.println("Sign player or coach");
                break;
            default:
                System.out.println("Invalid option");
                chooseOptionMenu2();
        }
    }
    private static void chooseOptionMenu3() {
        Scanner scanner = new Scanner(System.in);
        int option = scanner.nextInt();
        switch (option) {
            case 0:
                chooseOptionMenu2();
                break;
            case 1:
                System.out.println("Transfer player or coach");
                break;
            case 2:
                System.out.println("what player would you like to change position?");
                scanner.nextLine();
                String playerName= scanner.nextLine();
                System.out.println("To what position would you like to change? (DAV,POR,DEF,MIG)");
                String position = scanner.nextLine();
                playerName.substring(0,1).toUpperCase();
                position.toUpperCase();
                Player.changePlayerPosition(playerName,position);
                break;
            case 3:
                System.out.println("what couch would you like to choose to increase salary?");
                scanner.nextLine();
                String coachName = scanner.nextLine();
                coachName = capitalizeFirstLetterNames(coachName);
                Coach.increaseSalary(coachName);
        }
    }



    private static void printManageTeam() {
        System.out.println("**************************************************");
        System.out.println("            Welcome to Team Manager ⚽️");
        System.out.println("**************************************************");
        System.out.println();
        System.out.println(" 1️⃣ - Deregister team ❌");
        System.out.println(" 2️⃣ - Modify president 👔");
        System.out.println(" 3️⃣ - Dismiss coach 🛑");
        System.out.println(" 4️⃣ - Sign player or coach 📝");
        System.out.println(" 0️⃣ - Exit 🚪");
        System.out.println();
        System.out.println("**************************************************");
        System.out.print("  Choose an option: ");
    }

    private static void printWelcome() {
        System.out.println("**************************************************");
        System.out.println("   Welcome to Politècnics Football Manager ⚽️");
        System.out.println("**************************************************");
        System.out.println();
        System.out.println(" 1️⃣ - View current league standings 🏆");
        System.out.println(" 2️⃣ - Manage team ⚽...");
        System.out.println(" 3️⃣ - Register a new team 🆕");
        System.out.println(" 4️⃣ - Register a new player, coach or Owner 👥");
        System.out.println(" 5️⃣ - View team data 📊");
        System.out.println(" 6️⃣ - View player data of team 👤");
        System.out.println(" 7️⃣ - Start a new league 🏅");
        System.out.println(" 8️⃣ - Manage market ⚡...");
        System.out.println(" 9️⃣ - Transfer player 🔄");
        System.out.println(" 🔟 - Save team data 💾");
        System.out.println(" 0️⃣ - Exit 🚪");
        System.out.println();
        System.out.println("**************************************************");
        System.out.print("  Choose an option: ");
    }
    private static void printTraining(){
        System.out.println("**************************************************");
        System.out.println("            Welcome to Transfer Market 💹️");
        System.out.println("**************************************************");
        System.out.println();
        System.out.println(" 1️⃣ - Conduct training session (transfer market) 📑");
        System.out.println(" 2️⃣ - Change player position ⛓️‍💥");
        System.out.println(" 3️⃣ - increase couch salary 💸");
        System.out.println(" 0️⃣ - Exit 🚪");
        System.out.println();
        System.out.println("**************************************************");
        System.out.print("  Choose an option: ");
    }

    private static String capitalizeFirstLetterNames(String name) {
        if (name == null || name.isEmpty()) {
            return name;
        }
        return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
    }
    private static void reewriteFileMarket(ArrayList<Player> players, ArrayList<Coach> coaches, ArrayList<Person> owners,ArrayList<Team> teams) {
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
                String ownerData = String.format("T;%s;%d;%s;%s;%s%n",
                        o.getName(), o.getSurName(), o.getBirthDay(), o.getMotivation(),o.getAnualSalary());
                w.write(ownerData);
            }
            for (Team t : teams) {
                String teamData = String.format("T;%s;%d;%s;%s;%s%n",
                        t.getName(), t.getBirthDate(), t.getCity(),
                        t.getCoach().getName(), t.getOwner().getName());
                w.write(teamData);
            }
            System.out.println("Changes saved");
        } catch (IOException e) {
            System.out.println("Error al escribir en el archivo: " + e.getMessage());
        }
    }



}
