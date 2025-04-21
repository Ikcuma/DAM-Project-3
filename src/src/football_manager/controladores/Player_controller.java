package football_manager.controladores;

import football_manager.modulos.Player;

import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Player_controller {
    public static void printPersonData(Player player) {
        System.out.println("════════════════════════════════════════════");
        System.out.println("  Player Information: " + player.getName());
        System.out.println("════════════════════════════════════════════");
        System.out.println("Name: " + player.getName());
        System.out.println("Surname: " + player.getSurName());
        System.out.println("Date of Birth: " + player.getBirthDay());
        System.out.println("Motivation: " + player.getMotivation() + " points");
        System.out.println("Annual Salary: $" + player.getAnualSalary());
        System.out.println("Position: " + player.getPosition());
        System.out.println("Quality Points: " + player.getCualityPoints());
        System.out.println("Jersey Number: " + player.getBack());
        System.out.println("════════════════════════════════════════════");
    }

    public static void train(Player player) {
        boolean changed = Player.train(player);

        if (changed) {
            System.out.println("El jugador " + player.getName() + " ha cambiado a la posición: " + player.getPosition());
        } else {
            System.out.println("El jugador " + player.getName() + " ha entrenado pero no cambió de posición.");
        }
    }

    public static void parseAndAddPlayer(String data, ArrayList<Player> players) {
        Player parsedPlayer = Player.parse(data);

        if (parsedPlayer != null) {
            players.add(parsedPlayer);
            System.out.println("✅ Jugador añadido correctamente: " + parsedPlayer.getName());
        } else {
            System.out.println("❌ Error al analizar los datos del jugador: " + data);
        }
    }

    public static void printDuplicateError(Player p1) {
        System.out.println("⚠️ Jugadores DUPLICADOS: " + p1.getName());
    }
}