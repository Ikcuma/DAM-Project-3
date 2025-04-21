
package football_manager.modulos;

import football_manager.controladores.Player_controller;

import java.util.Comparator;
import java.util.Objects;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static football_manager.controladores.Player_controller.printDuplicateError;


public class Player extends Person {
    private int back;
    private String position;
    private int cualityPoints; //(30-100)

    //Constructor

    public Player(String name, String surName, String birthDay, int motivation, int anualSalary, int back, String position, int cualityPoints) {
        super(name, surName, birthDay, motivation, anualSalary);
        this.back = back;
        this.position = position;
        this.cualityPoints = cualityPoints;
    }


    //Getters

    public int getBack() {
        return back;
    }

    public String getPosition() {
        return position;
    }

    public int getCualityPoints() {
        return cualityPoints;
    }


    //Setters

    public void setBack(int back) {
        this.back = back;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setCualityPoints(int cualityPoints) {
        this.cualityPoints = cualityPoints;
    }

    //Methods
    public static void printPersonData(Player player){
        Player_controller.printPersonData(player);
    }

    public void train() {
        Player_controller.train(this);
    }

    public static Player parse(String data) {
        return Player_controller.parse(data);
    }

    public String toFileFormat() {
        return String.format("J;%s;%s;%s;%d;%d;%d;%s;%d", super.name, super.surName, super.birthDay, super.motivation, super.anualSalary, this.back, this.position, this.cualityPoints);
    }

    public String toFileFormatTeam() {
        return "Player{name='" + this.name +
                "', surName='" + this.surName +
                "', birthDay='" + this.birthDay +
                "', motivation=" + this.motivation +
                ", anualSalary=" + this.anualSalary +
                ", back=" + this.back +
                ", position='" + this.position +
                "', cualityPoints=" + this.cualityPoints + "}";
    }

    @Override
    public String toString() {
        return "Player{" +
                "back=" + back +
                ", position='" + position + '\'' +
                ", cualityPoints=" + cualityPoints +
                ", name='" + name + '\'' +
                ", surName='" + surName + '\'' +
                ", birthDay='" + birthDay + '\'' +
                ", motivation=" + motivation +
                ", anualSalary=" + anualSalary +
                '}';
    }

    public static class PlayerEqualityChecker implements Comparator<Player> {
        @Override
        public int compare(Player p1, Player p2) {
            if (p1.equals(p2)) {
                printDuplicateError(p1);
                return 0;
            }
            return p1.getName().compareTo(p2.getName());
        }
    }
}