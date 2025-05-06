
package football_manager.modulos;

import football_manager.controladores.Person_controller;

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
    public void train() {
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

    public static void loadHashmaps(HashMap<String, Person> peopleHash, ArrayList<Person> peopleList) {
        peopleList.forEach(person -> peopleHash.put(person.getName(), person));
    }


    public static Team findTeamByName(ArrayList<Team> teams, String teamName) {
        for (Team team : teams) {
            if (team.getName().equalsIgnoreCase(teamName)) {
                return team;
            }
        }
        return null;
    }


    public static Person findPersonByName(ArrayList<Team> teams, String newPresidentName) {
        for (Team team : teams) {
            Person owner = team.getOwner();
            if (owner.getName().equalsIgnoreCase(newPresidentName)) {
                return owner;
            }
        }
        return null;
    }


    public static boolean updatePresident(Team team, Person newPresident) {
        if (team != null && newPresident != null) {
            team.setOwner(newPresident); // Actualiza el presidente del equipo
            return true;
        }
        return false;
    }

    public static Person createPlayer(String name, String surname, String birthday, int motivation, int salary, int backNumber, String position) {
        Random random = new Random();
        int quality = random.nextInt(71) + 30;
        return new Player(name, surname, birthday, motivation, salary, backNumber, position, quality);
    }

    public static Person createCoach(String name, String surname, String birthday, int motivation, int salary, int victories, boolean selectedForNational) {
        return new Coach(name, surname, birthday, motivation, salary, victories, selectedForNational);
    }

    public static Person createOwner(String name, String surname, String birthday, int motivation, int salary) {
        return new Person(name, surname, birthday, motivation, salary);
    }

    public static boolean isNameDuplicate(String name, HashMap<String, Person> hashPersons) {
        return hashPersons.containsKey(name);
    }

    public static String capitalizeFirstLetterNames(String name) {
        if (name.isEmpty()) return name;
        return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
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
}