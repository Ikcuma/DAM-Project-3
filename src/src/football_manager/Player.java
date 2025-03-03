package football_manager;

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


    @Override
    public String toString() {
        return "Player{" +
                "back='" + back + '\'' +
                ", position='" + position + '\'' +
                ", cualityPoints=" + cualityPoints +
                '}';
    }
}
