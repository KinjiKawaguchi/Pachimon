package src;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class RegisterData {
    private static Scanner scanner = new Scanner(System.in);
    private static DatabaseManager databaseManager = new DatabaseManager();

    public static void main(String[] args) {
        try {
            while (true) {
                displayMenu();

                int action = scanner.nextInt();
                scanner.nextLine(); // Clear the newline character

                if (action == 1) {
                    registerNewPokemon();
                } else if (action == 2) {
                    registerNewSpell();
                } else if (action == 3) {
                    databaseManager.displayAllPokemons();
                } else if (action == 4) {
                    databaseManager.displayAllSpells();
                } else if (action == 5) {
                    break;
                } else {
                    System.out.println("Invalid action! Please try again.");
                }
            }

            // Close the database connection
            databaseManager.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void displayMenu() {
        System.out.println("Please choose an action:");
        System.out.println("1. Register a new Pokemon");
        System.out.println("2. Register a new Spell");
        System.out.println("3. Display all registered Pokemons");
        System.out.println("4. Display all registered Spells");
        System.out.println("5. Exit");
    }

    private static void registerNewPokemon() throws SQLException {
        Pokemon newPokemon = createPokemon();
        databaseManager.addPokemon(newPokemon);
        System.out.println("Pokemon registered!");
    }

    private static int registerReturnNewPokemon() throws SQLException {
        Pokemon newPokemon = createPokemon();
        databaseManager.addPokemon(newPokemon);
        System.out.println("Pokemon registered!");
        return databaseManager.getLastInsertedPokemonId();
    }

    private static void registerNewSpell() throws SQLException {
        Spell newSpell = createSpell();
        databaseManager.addSpell(newSpell);
        System.out.println("Spell registered!");
    }

    private static int registerReturnNewSpell() throws SQLException {
        Spell newSpell = createSpell();
        databaseManager.addSpell(newSpell);
        System.out.println("Spell registered");
        return databaseManager.getLastInsertedSpellId();
    }

    private static Pokemon createPokemon() throws SQLException {
        System.out.println("Enter the Pokemon's name:");
        String name = scanner.nextLine();

        System.out.println("Enter the Pokemon's type:");
        String type = scanner.nextLine();

        System.out.println(
                "Enter the Pokemon's status (max_hp, max_attack, max_defence, max_spAttack, max_spDefence, max_speed):");
        Status status = new Status(scanner.nextInt(), scanner.nextInt(), scanner.nextInt(), scanner.nextInt(),
                scanner.nextInt(), scanner.nextInt());
        scanner.nextLine(); // Clear the newline character

        System.out.println("Choose the Pokemon's spells (enter the spell IDs separated by a space)\n"
                + "-2 if you want to register new spell");
        List<Spell> allSpells = databaseManager.getAllSpells();
        allSpells.forEach(spell -> System.out.println(spell.getId() + ": " + spell.getName()));
        String[] spellIds = scanner.nextLine().split(" ");
        Spell[] spells = new Spell[spellIds.length];
        for (int i = 0; i < spellIds.length; i++) {
            int spellId;
            if (Integer.parseInt(spellIds[i]) == -2) {
                spellId = registerReturnNewSpell();
                allSpells = databaseManager.getAllSpells();
            } else {
                spellId = Integer.parseInt(spellIds[i]);
            }
            spells[i] = allSpells.stream().filter(spell -> spell.getId() == spellId).findFirst().orElse(null);
        }

        databaseManager.displayAllPokemonsName();
        System.out.println("Enter the evolved Pokemon's ID\n"
                + "enter -1 if it doesn't have an evolved form\n"
                + "-2 if you want to register new pokemon");
        int evolvedId = scanner.nextInt();
        scanner.nextLine(); // Clear the newline character
        Pokemon evolved;
        if (evolvedId == -2) {
            evolvedId = registerReturnNewPokemon();
        }
        evolved = evolvedId != -1 ? databaseManager.getPokemon(evolvedId) : null;
        databaseManager.displayAllPokemonsName();
        System.out.println("Enter the deevolved Pokemon's ID\n"
                + "enter -1 if it doesn't have an deevolved form\n"
                + "-2 if you want to register new pokemon");
        int devolvedId = scanner.nextInt();
        scanner.nextLine(); // Clear the newline character
        Pokemon devolved;
        if (devolvedId == -2) {
            devolvedId = registerReturnNewPokemon();
        }
        devolved = devolvedId != -1 ? databaseManager.getPokemon(devolvedId) : null;

        return new Pokemon(name, type, spells, status, evolved, devolved);
    }

    private static Spell createSpell() {
        System.out.println("Enter the Spell's name:");
        String name = scanner.nextLine();

        System.out.println("Enter the Spell's type:");
        String type = scanner.nextLine();

        System.out.println("Enter the Spell's classification:");
        String classification = scanner.nextLine();

        System.out.println("Enter the Spell's power:");
        int power = scanner.nextInt();

        System.out.println("Enter the Spell's accuracy:");
        int accuracy = scanner.nextInt();

        System.out.println("Enter the Spell's pp:");
        int pp = scanner.nextInt();

        System.out.println("Enter whether the Spell is direct (true/false):");
        boolean direct = scanner.nextBoolean();
        scanner.nextLine(); // Clear the newline character

        System.out.println("Enter the Spell's description:");
        String description = scanner.nextLine();

        return new Spell(name, type, classification, power, accuracy, pp, direct, description);
    }
}