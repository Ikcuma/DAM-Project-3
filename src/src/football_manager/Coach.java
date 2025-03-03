package football_manager;

public class Coach extends Person
{
    private int victories;
    private boolean nacional;

    //Contructors
    public Coach(String name, String surName, String birthDay, int motivation, int anualSalary, int victories, boolean nacional) {
        super(name, surName, birthDay, motivation, anualSalary);
        this.victories = victories;
        this.nacional = nacional;
    }


    //Getters
    public int getVictories() {
        return victories;
    }

    public boolean isNacional() {
        return nacional;
    }
    //Setters
    public void setVictories(int victories) {
        this.victories = victories;
    }

    public void setNacional(boolean nacional) {
        this.nacional = nacional;
    }

    //Methods
    public void increaseNumber(){

    }

}
