/**
 * Controlador para gestionar operaciones relacionadas con jugadores.
 */
package football_manager.controladores;

import football_manager.modulos.Player;

import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Player_controller {

    /**
     * Imprime los datos de un jugador en formato legible.
     *
     * @param player Jugador cuyos datos se mostrarán
     */
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
    /**
     * Entrena a un jugador, con posibilidad de cambiar su posición.
     *
     * @param player Jugador que será entrenado
     */
    public static void train(Player player) {
        boolean changed = Player.train(player);

        if (changed) {
            System.out.println("El jugador " + player.getName() + " ha cambiado a la posición: " + player.getPosition());
        } else {
            System.out.println("El jugador " + player.getName() + " ha entrenado pero no cambió de posición.");
        }
    }
    /**
     * Parsea una cadena de texto para crear un objeto Player.
     *
     * @param data Cadena de texto con los datos del jugador
     * @return Objeto Player creado o null si hay error
     */

    public static void printDuplicateError(Player p1) {
        System.out.println("⚠️ Jugadores DUPLICADOS: " + p1.getName());
    }

    /**
     * Muestra un mensaje de error cuando se detectan jugadores duplicados.
     *
     * @param p1 Jugador duplicado
     */
    public static void printDuplicateError(Player p1) {
        System.out.println("⚠️ Jugadores DUPLICADOS: " + p1.getName());
    }
}