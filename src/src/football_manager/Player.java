package football_manager;

public class Player extends Person {
    private String back;
    private String position;
    private int cualityPoints; //(30-100)

    public Player(String name, String surName, String birthDay, int motivation, String anualSalary, String back, String position, int cualityPoints) {
        super(name, surName, birthDay, motivation, anualSalary);
        this.back = back;
        this.position = position;
        this.cualityPoints = cualityPoints;
    }
}
