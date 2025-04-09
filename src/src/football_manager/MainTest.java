package football_manager;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    @Test
    void capitalizeFirstLetterNames_ValidName() {
        // Casos válidos con nombres
        assertEquals("John", Main.capitalizeFirstLetterNames("john"));
        assertEquals("Jane", Main.capitalizeFirstLetterNames("jane"));
        assertEquals("Smith", Main.capitalizeFirstLetterNames("smith"));
    }

    @Test
    void capitalizeFirstLetterNames_NameWithMixedCase() {
        // Casos con mayúsculas y minúsculas mezcladas
        assertEquals("John", Main.capitalizeFirstLetterNames("JoHN"));
        assertEquals("Jane", Main.capitalizeFirstLetterNames("JAnE"));
        assertEquals("Smith", Main.capitalizeFirstLetterNames("SMiTH"));
    }

    @Test
    void loadListToFileMarket() {
        // Crear un jugador y un entrenador para la prueba
        Player player = new Player("John", "Doe", "1990-01-01", 75, 100000, 10, "DEF", 85);
        Coach coach = new Coach("Michael", "Johnson", "1975-02-20", 95, 150000, 50, true);

        // Esperamos un formato específico para cada tipo de persona (esto depende de la implementación de toFileFormat)
        String expectedPlayerFormat = "J;John;Doe;1990-01-01;75;100000;10;DEF;85";  // Cambia según el formato real
        String expectedCoachFormat = "E;Michael;Johnson;1975-02-20;95;150000;50;true";  // Cambia según el formato real

        // Verificamos que el formato generado por toFileFormat sea correcto
        assertEquals(expectedPlayerFormat, player.toFileFormat());
        assertEquals(expectedCoachFormat, coach.toFileFormat());
    }

    @Test
    void transferPlayer() {
        // Crear jugadores y equipos
        Player player1 = new Player("John", "Doe", "1990-01-01", 75, 100000, 10, "DEF", 85);
        Player player2 = new Player("Jane", "Smith", "1992-05-10", 80, 120000, 9, "MID", 90);

        Coach coach1 = new Coach("Michael", "Johnson", "1975-02-20", 95, 150000, 50, true);
        Coach coach2 = new Coach("David", "Williams", "1980-09-15", 85, 130000, 30, false);

        Team team1 = new Team("Team A", "2000-05-10", "Barcelona", coach1, new Person("Owner", "One", "1970-01-01", 100, 100000), new ArrayList<>(List.of(player1)));
        Team team2 = new Team("Team B", "2005-09-15", "Madrid", coach2, new Person("Owner", "Two", "1980-01-01", 90, 120000), new ArrayList<>(List.of(player2)));

        ArrayList<Team> teams = new ArrayList<>();
        teams.add(team1);
        teams.add(team2);

        // Simular la entrada del usuario
        String simulatedInput = "Team A\nTeam B\nplayer\nJohn\n";  // Las entradas simuladas
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        // Llamada al método que estamos probando
        Main.transferPlayerOrCoach(teams);

        // Verificaciones
        assertEquals(0, team1.getPlayers().size(), "Team A no debería tener jugadores restantes.");
        assertEquals(2, team2.getPlayers().size(), "Team B debería tener 2 jugadores ahora.");
        assertTrue(team1.getPlayers().contains(player1), "Team B debe contener al jugador 'John'.");
    }

    @Test
    void transferCoach() {
        // Crear jugadores y entrenadores

        Player player1 = new Player("John", "Doe", "1990-01-01", 75, 100000, 10, "DEF", 85);
        Player player2 = new Player("Jane", "Smith", "1992-05-10", 80, 120000, 9, "MID", 90);

        Coach coach1 = new Coach("Michael", "Johnson", "1975-02-20", 95, 150000, 50, true);
        Coach coach2 = new Coach("David", "Williams", "1980-09-15", 85, 130000, 30, false);

        Team team1 = new Team("Team A", "2000-05-10", "Barcelona", coach1, new Person("Owner", "One", "1970-01-01", 100, 100000), new ArrayList<>(List.of(player1)));
        Team team2 = new Team("Team B", "2005-09-15", "Madrid", coach2, new Person("Owner", "Two", "1980-01-01", 90, 120000), new ArrayList<>(List.of(player2)));

        ArrayList<Team> teams = new ArrayList<>();
        teams.add(team1);
        teams.add(team2);

        // Simular la entrada del usuario para transferir un entrenador
        String simulatedInput = "Team A\nTeam B\ncoach\n";  // Las entradas simuladas para el entrenador
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        // Llamada al método para transferir al entrenador
        Main.transferPlayerOrCoach(teams);

        // Verificaciones para la transferencia del entrenador
        assertNull(team1.getCoach(), "Team A no debería tener entrenador después de la transferencia.");
        assertEquals(coach1, team2.getCoach(), "Team B debería tener como entrenador a 'Michael Johnson' después de la transferencia.");
    }

    @Test
    void printTeamData_TeamNotFound() {
        // Crear jugadores, entrenadores y equipos
        Player player1 = new Player("John", "Doe", "1990-01-01", 75, 100000, 10, "DEF", 85);
        Coach coach1 = new Coach("Michael", "Johnson", "1975-02-20", 95, 150000, 50, true);

        Team team1 = new Team("Team A", "2000-05-10", "Barcelona", coach1, new Person("Owner", "One", "1970-01-01", 100, 100000), new ArrayList<>(List.of(player1)));

        ArrayList<Team> teams = new ArrayList<>();
        teams.add(team1);

// Capturar la salida del método printTeamData
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

// Llamada al método que estamos probando con un equipo no válido
        Main.printTeamData("Team B", teams);

// Restaurar la salida estándar
        System.setOut(originalOut);

// Verificar que la salida contiene el mensaje de "team not found"
        String expectedMessage = "❌ Team 'Team B' not found.\n";
        assertFalse(outputStream.toString().contains(expectedMessage),
                "El mensaje que indica que no se encontró el equipo 'Team B' debería imprimirse.");
    }

    @Test
    void viewPersonDataMenu() {
        // Crear un jugador y un entrenador
        Player player1 = new Player("John", "Doe", "1990-01-01", 75, 100000, 10, "DEF", 85);

        // Crear el HashMap y añadir las personas
        HashMap<String, Person> hashPersons = new HashMap<>();
        hashPersons.put(player1.getName(), player1);

        // Verificar que los datos del jugador no son nulos
        assertNotNull(hashPersons.get("John"), "The player 'John' should be in the hash map.");
    }
}