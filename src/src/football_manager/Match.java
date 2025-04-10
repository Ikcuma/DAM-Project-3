package football_manager;

import java.util.Random;

public class Match {
    private Team homeTeam;
    private Team awayTeam;
    private int homeGoals;
    private int awayGoals;
    private boolean played;

    // Constructor
    public Match(Team homeTeam, Team awayTeam) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.homeGoals = 0;
        this.awayGoals = 0;
        this.played = false;
    }

    // Getters
    public Team getHomeTeam() {
        return homeTeam;
    }

    public Team getAwayTeam() {
        return awayTeam;
    }

    public int getHomeGoals() {
        return homeGoals;
    }

    public int getAwayGoals() {
        return awayGoals;
    }

    public boolean isPlayed() {
        return played;
    }

    // Methods


    public void playMatch() {
        Random random = new Random();


        double homeQuality = calculateTeamQuality(homeTeam);
        double awayQuality = calculateTeamQuality(awayTeam);


        double homeWinProb = homeQuality / (homeQuality + awayQuality);
        double drawProb = 0.2; // Fixed 20% chance of draw
        double awayWinProb = 1 - homeWinProb - drawProb;


        double result = random.nextDouble();

        if (result < homeWinProb) {

            homeGoals = random.nextInt(4) + 1; // 1-4 goals
            awayGoals = random.nextInt(homeGoals);
        } else if (result < homeWinProb + drawProb) {

            int goals = random.nextInt(3); // 0-2 goals
            homeGoals = goals;
            awayGoals = goals;
        } else {
            awayGoals = random.nextInt(4) + 1; // 1-4 goals
            homeGoals = random.nextInt(awayGoals);
        }

        played = true;
    }


    private double calculateTeamQuality(Team team) {
        double quality = 0;
        int playerCount = 0;

        for (Person person : team.getPlayers()) {
            if (person instanceof Player) {
                Player player = (Player) person;
                quality += player.getCualityPoints() * (1 + (player.getMotivation() / 10.0));
                playerCount++;
            }
        }

        if (playerCount > 0) {
            quality /= playerCount;
        }


        if (team.getCoach() != null) {
            Coach coach = team.getCoach();
            quality *= 1 + (coach.getVictories() * 0.01) + (coach.getMotivation() / 20.0);
            if (coach.isNacional()) {
                quality *= 1.05;
            }
        }

        return quality;
    }


    public String getResult() {
        return String.format("%-20s %2d - %-2d %s",
                homeTeam.getName(),
                homeGoals,
                awayGoals,
                awayTeam.getName());
    }

    @Override
    public String toString() {
        if (played) {
            return getResult();
        } else {
            return String.format("%-20s vs %s (Not played)",
                    homeTeam.getName(),
                    awayTeam.getName());
        }
    }
}
