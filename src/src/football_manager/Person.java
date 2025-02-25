package football_manager;

public class Person {
    private final String name;
    private final String surName;
    private final String birthDay;
    private int motivation; //(1-10)
    private String anualSalary;

    //Contructors
    public Person(String name, String surName, String birthDay, int motivation, String anualSalary) {
        this.name = name;
        this.surName = surName;
        this.birthDay = birthDay;
        this.motivation = motivation;
        this.anualSalary = anualSalary;
    }
    //Getters
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

    public String getAnualSalary() {
        return anualSalary;
    }
    //Setters
    public void setMotivation(int motivation) {
        this.motivation = motivation;
    }

    public void setAnualSalary(String anualSalary) {
        this.anualSalary = anualSalary;
    }

    //methods
    public void train(){

    }




}
