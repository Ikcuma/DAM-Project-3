package football_manager;

public class Coach extends P
{
    private int victories;
    private boolean nacional;

    //Contructors
    public Coach(int victories, boolean nacional) {
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
