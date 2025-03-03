package football_manager;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args)  {
        loadPlayers();
        printWelcome();
        chooseOptionMenu1();
    }

    private static void loadPlayers()  {

        String filePath = "C:\\Users\\dunkl\\IdeaProjects\\DAM-Project-3\\src\\src\\football_manager\\resources\\market_files.txt";
        ArrayList<String> brutePersondata = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = reader.readLine())!= null) {
                brutePersondata.add(line);
            }
        }catch (FileNotFoundException e) {
            System.out.println("file not found...");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (int i = 0; i < brutePersondata.size(); i++){
            String[] personData = brutePersondata.get(i).split(";");
            if (personData[0].equals("J")){
                Player player = new Player(personData[1], personData[2], personData[3], Integer.parseInt(personData[4]), Integer.parseInt(personData[5]), Integer.parseInt(personData[6]), personData[7], Integer.parseInt(personData[8]));
            }else{
                Coach coach = new Coach(personData[1], personData[2], personData[3], Integer.parseInt(personData[4]), Integer.parseInt(personData[5]), Integer.parseInt(personData[6]), Boolean.parseBoolean(personData[7]));
            }
        }
        System.out.println();

    }

    private static void chooseOptionMenu1() {
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
                chooseOptionMenu2();
                break;
            case 3:
                System.out.println("Register a new team ���");
                break;
            case 4:
                System.out.println("Register a new player or coach ��");
                break;
            case 5:




        }
    }

    private static void chooseOptionMenu2() {
        Scanner scanner = new Scanner(System.in);
        int option = scanner.nextInt();
        switch (option) {
            case 0:
                chooseOptionMenu1();
                break;
            case 1:
                System.out.println("Deregister team");
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
        System.out.println(" 2️⃣ - Manage team ⚽");
        System.out.println(" 3️⃣ - Register a new team 🆕");
        System.out.println(" 4️⃣ - Register a new player or coach 👥");
        System.out.println(" 5️⃣ - View team data 📊");
        System.out.println(" 6️⃣ - View player data of team 👤");
        System.out.println(" 7️⃣ - Start a new league 🏅");
        System.out.println(" 8️⃣ - Conduct training session (transfer market) ⚡");
        System.out.println(" 9️⃣ - Transfer player 🔄");
        System.out.println(" 🔟 - Save team data 💾");
        System.out.println(" 0️⃣ - Exit 🚪");
        System.out.println();
        System.out.println("**************************************************");
        System.out.print("  Choose an option: ");
    }
}
