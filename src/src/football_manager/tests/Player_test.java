package football_manager.tests;
import football_manager.modulos.Player;

import java.util.HashSet;
import java.util.Set;

public class Player_test {
    public static void main(String[] args) {

        Player player1 = new Player("Lamine", 10);
        Player player2 = new Player("Lamine", 10);
        Player player3 = new Player("Xavi", 6);

        System.out.println("player1 equals player2: " + player1.equals(player2));
        System.out.println("player1 equals player3: " + player1.equals(player3));

        System.out.println("player1 hashCode: " + player1.hashCode());
        System.out.println("player2 hashCode: " + player2.hashCode());
        System.out.println("player3 hashCode: " + player3.hashCode());

        // Probar en un conjunto (Set) para asegurarnos de que no haya duplicados
        Set<Player> playerSet = new HashSet<>();
        playerSet.add(player1);
        playerSet.add(player2);
        playerSet.add(player3);

        System.out.println("Contenido del Set:");
        for (Player player : playerSet) {
            System.out.println(player);
        }
    }
}
