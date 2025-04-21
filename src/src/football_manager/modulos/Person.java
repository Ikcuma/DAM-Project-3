
package football_manager.modulos;

import football_manager.controladores.Person_controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
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

    public void printPersonData() {
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

    public static void createNewPersonMenu(HashMap<String, Person> hashPersons, ArrayList<Person> peopleList, Scanner sc) {
        Person_controller.createNewPersonMenu(hashPersons, peopleList, sc);
    }

    public static void loadHashmaps(HashMap<String, Person> peopleHash, ArrayList<Person> peopleList){
        Person_controller.loadHashmaps(peopleHash, peopleList);
    }

    public static void modifyPresident(ArrayList<Team> teams, Scanner scanner){
        Person_controller.modifyPresident(teams, scanner);
    }
}