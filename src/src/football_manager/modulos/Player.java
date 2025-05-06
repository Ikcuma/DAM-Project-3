
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

    public Player(String name, int back) {
        super(name, "", "", 0, 0);
        this.back = back;
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

    public static boolean train(Player player) {
        Random random = new Random();
        String[] positions = {"DEF", "MIG", "DAV", "POR"};
        String newPosition = positions[random.nextInt(positions.length)];

        boolean shouldChange = Math.random() < 0.05;

        if (shouldChange) {
            player.setPosition(newPosition);
        }

        return shouldChange;
    }

    public static Player parse(String data) {
        if (data == null || data.isEmpty()) {
            return null;
        }

        Pattern pattern = Pattern.compile(
                "Player\\{name='(.*?)', surName='(.*?)', birthDay='(.*?)', motivation=(\\d+), anualSalary=(\\d+), back=(\\d+), position='(.*?)', cualityPoints=(\\d+)\\}"
        );
        Matcher matcher = pattern.matcher(data);

        if (matcher.find()) {
            String name = matcher.group(1);
            String surName = matcher.group(2);
            String birthDay = matcher.group(3);
            String position = matcher.group(7);

            if (name == null || name.isEmpty() || surName == null || surName.isEmpty() ||
                    birthDay == null || birthDay.isEmpty() || position == null || position.isEmpty()) {
                return null;
            }

            return new Player(
                    name,
                    surName,
                    birthDay,
                    Integer.parseInt(matcher.group(4)),
                    Integer.parseInt(matcher.group(5)),
                    Integer.parseInt(matcher.group(6)),
                    position,
                    Integer.parseInt(matcher.group(8))
            );
        }

        return null;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return back == player.back && Objects.equals(name, player.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, back);
    }
}