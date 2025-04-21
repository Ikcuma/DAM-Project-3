package football_manager.controladores;

import football_manager.modulos.Player;

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
        String[] options = {"DEF", "MIG", "DAV", "POR"};
        Random random = new Random();

        int randomIndex = random.nextInt(options.length);
        String newPosition = options[randomIndex];

        boolean realiza = Math.random() < 0.05;

        if (realiza) {
            player.setPosition(newPosition);
            System.out.println("El jugador " + player.getName() + " ha cambiado a la posición: " + newPosition);
        } else {
            System.out.println("El jugador " + player.getName() + " ha entrenado pero no cambió de posición.");
        }
    }

    public static Player parse(String data) {
        if (data.isEmpty()) {
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
                System.out.println("Error: Datos incompletos para el jugador: " + data);
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

    public static void printDuplicateError(Player p1) {
        System.out.println("⚠️ Jugadores DUPLICADOS: " + p1.getName());
    }
}