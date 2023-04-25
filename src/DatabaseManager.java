package src;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:pokemons.db";

    public DatabaseManager() {
        try {
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection(DB_URL);
            if (conn != null) {
                createTables(conn);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void createTables(Connection conn) throws SQLException {
        String createPokemonsTable = "CREATE TABLE IF NOT EXISTS pokemons (" +
                "id INTEGER PRIMARY KEY," +
                "name TEXT NOT NULL," +
                "type TEXT NOT NULL," +
                "xp REAL DEFAULT 0," +
                "level INTEGER DEFAULT 1," +
                "status_id INTEGER," +
                "evolved_id INTEGER," +
                "devolved_id INTEGER," +
                "FOREIGN KEY(status_id) REFERENCES statuses (id)," +
                "FOREIGN KEY(evolved_id) REFERENCES pokemons (id)," +
                "FOREIGN KEY(devolved_id) REFERENCES pokemons (id)" +
                ");";
        String createStatusesTable = "CREATE TABLE IF NOT EXISTS statuses (" +
                "id INTEGER PRIMARY KEY," +
                "max_hp INTEGER," +
                "max_attack INTEGER," +
                "max_defence INTEGER," +
                "max_spAttack INTEGER," +
                "max_spDefence INTEGER," +
                "max_speed INTEGER" +
                ");";
        String createSpellsTable = "CREATE TABLE IF NOT EXISTS spells (" +
                "id INTEGER PRIMARY KEY," +
                "name TEXT NOT NULL," +
                "type TEXT NOT NULL," +
                "classification TEXT NOT NULL," +
                "power INTEGER," +
                "accuracy INTEGER," +
                "pp INTEGER," +
                "direct BOOLEAN," +
                "description TEXT" +
                ");";
        String createPokemonSpellsTable = "CREATE TABLE IF NOT EXISTS pokemon_spells (" +
                "pokemon_id INTEGER," +
                "spell_id INTEGER," +
                "PRIMARY KEY (pokemon_id, spell_id)," +
                "FOREIGN KEY(pokemon_id) REFERENCES pokemons (id)," +
                "FOREIGN KEY(spell_id) REFERENCES spells (id)" +
                ");";

        Statement stmt = conn.createStatement();
        stmt.execute(createPokemonsTable);
        stmt.execute(createStatusesTable);
        stmt.execute(createSpellsTable);
        stmt.execute(createPokemonSpellsTable);
    }

    public int addStatus(Status status) throws SQLException {
        String sql = "INSERT INTO statuses (max_hp, max_attack, max_defence, max_spAttack, max_spDefence, max_speed) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, status.getMax_hp());
            pstmt.setInt(2, status.getMax_attack());
            pstmt.setInt(3, status.getMax_defence());
            pstmt.setInt(4, status.getMax_spAttack());
            pstmt.setInt(5, status.getMax_spDefence());
            pstmt.setInt(6, status.getMax_speed());
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            return rs.next() ? rs.getInt(1) : -1;
        }
    }

    public int addSpell(Spell spell) throws SQLException {
        String sql = "INSERT INTO spells (name, type, classification, power, accuracy, pp, direct, description) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, spell.getName());
            pstmt.setString(2, spell.getType());
            pstmt.setString(3, spell.getClassification());
            pstmt.setInt(4, spell.getPower());
            pstmt.setInt(5, spell.getAccuracy());
            pstmt.setInt(6, spell.getPp());
            pstmt.setBoolean(7, spell.isDirect());
            pstmt.setString(8, spell.getDescription());
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            return rs.next() ? rs.getInt(1) : -1;
        }
    }

    public int addPokemon(Pokemon pokemon) throws SQLException {
        String sql = "INSERT INTO pokemons (name, type, status_id, evolved_id, devolved_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, pokemon.getName());
            pstmt.setString(2, pokemon.getType());
            pstmt.setInt(3, addStatus(pokemon.getStatus()));
            pstmt.setInt(4, pokemon.getEvolved() == null ? -1 : pokemon.getEvolved().getId());
            pstmt.setInt(5, pokemon.getDevolved() == null ? -1 : pokemon.getDevolved().getId());
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            int id = rs.next() ? rs.getInt(1) : -1;
            if (id != -1) {
                addPokemonSpells(id, pokemon.getSpell());
            }
            return id;
        }
    }

    private void addPokemonSpells(int pokemonId, Spell[] spells) throws SQLException {
        String sql = "INSERT INTO pokemon_spells (pokemon_id, spell_id) VALUES (?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (Spell spell : spells) {
                if (spell != null) { // Add this check
                    pstmt.setInt(1, pokemonId);
                    pstmt.setInt(2, spell.getId());
                    pstmt.executeUpdate();
                } else {
                    System.out.println("Error: Could not find spell with ID ");
                }
            }
        }
    }

    public List<Spell> getAllSpells() throws SQLException {
        String sql = "SELECT * FROM spells";
        List<Spell> spells = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String type = rs.getString("type");
                String classification = rs.getString("classification");
                int power = rs.getInt("power");
                int accuracy = rs.getInt("accuracy");
                int pp = rs.getInt("pp");
                boolean direct = rs.getBoolean("direct");
                String description = rs.getString("description");
                Spell spell = new Spell(name, type, classification, power, accuracy, pp, direct, description);
                spell.setId(id);
                spells.add(spell);
            }
        }
        return spells;
    }

    public List<Pokemon> getAllPokemons() throws SQLException {
        String sql = "SELECT * FROM pokemons";
        List<Pokemon> pokemons = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String type = rs.getString("type");
                double xp = rs.getDouble("xp");
                int level = rs.getInt("level");
                int statusId = rs.getInt("status_id");
                int evolvedId = rs.getInt("evolved_id");
                int devolvedId = rs.getInt("devolved_id");
                Status status = getStatus(statusId);
                Pokemon evolved = evolvedId != -1 ? getPokemon(evolvedId) : null;
                Pokemon devolved = devolvedId != -1 ? getPokemon(devolvedId) : null;
                Spell[] spells = getPokemonSpells(id).toArray(new Spell[0]);
                Pokemon pokemon = new Pokemon(name, type, spells, status, evolved, devolved);
                pokemon.setId(id);
                pokemons.add(pokemon);
            }
        }
        return pokemons;
    }

    public Status getStatus(int statusId) throws SQLException {
        String sql = "SELECT * FROM statuses WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, statusId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int max_hp = rs.getInt("max_hp");
                int max_attack = rs.getInt("max_attack");
                int max_defence = rs.getInt("max_defence");
                int max_spAttack = rs.getInt("max_spAttack");
                int max_spDefence = rs.getInt("max_spDefence");
                int max_speed = rs.getInt("max_speed");
                return new Status(max_hp, max_attack, max_defence, max_spAttack, max_spDefence, max_speed);
            } else {
                return null;
            }
        }
    }

    public Pokemon getPokemon(int pokemonId) throws SQLException {
        String sql = "SELECT * FROM pokemons WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, pokemonId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String type = rs.getString("type");
                double xp = rs.getDouble("xp");
                int level = rs.getInt("level");
                int statusId = rs.getInt("status_id");
                int evolvedId = rs.getInt("evolved_id");
                int devolvedId = rs.getInt("devolved_id");
                Status status = getStatus(statusId);
                Pokemon evolved = evolvedId != -1 ? getPokemon(evolvedId) : null;
                Pokemon devolved = devolvedId != -1 ? getPokemon(devolvedId) : null;
                Spell[] spells = getPokemonSpells(id).toArray(new Spell[0]);
                Pokemon pokemon = new Pokemon(name, type, spells, status, evolved, devolved);
                pokemon.setId(id);
                return pokemon;
            } else {
                return null;
            }
        }
    }

    public List<Spell> getPokemonSpells(int pokemonId) throws SQLException {
        String sql = "SELECT spells.* FROM pokemon_spells JOIN spells ON pokemon_spells.spell_id = spells.id WHERE pokemon_spells.pokemon_id = ?";
        List<Spell> spells = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, pokemonId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String type = rs.getString("type");
                String classification = rs.getString("classification");
                int power = rs.getInt("power");
                int accuracy = rs.getInt("accuracy");
                int pp = rs.getInt("pp");
                boolean direct = rs.getBoolean("direct");
                String description = rs.getString("description");
                Spell spell = new Spell(name, type, classification, power, accuracy, pp, direct, description);
                spell.setId(id);
                spells.add(spell);
            }
        }
        return spells;
    }

    public void displayAllPokemonsName() {
        String sql = "SELECT id, name FROM pokemons";

        try (Connection conn = DriverManager.getConnection(DB_URL);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            System.out.printf("%-5s %-15s%n", "ID",
                    "Name");
            System.out.println(
                    "------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            while (rs.next()) {
                System.out.printf("%-5s %-15s%n",
                        rs.getInt("id"),
                        rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void displayAllPokemons() {
        String sql = "SELECT pokemons.*, statuses.* FROM pokemons " +
                "INNER JOIN statuses ON pokemons.status_id = statuses.id";

        try (Connection conn = DriverManager.getConnection(DB_URL);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            System.out.printf("%-5s %-15s %-15s %-7s %-7s %-11s %-11s %-11s %-8s %-12s %-12s %-12s %-12s %-12s%n", "ID",
                    "Name", "Type", "XP", "Level", "Status ID", "Evolved ID", "Devolved ID", "Max HP", "Max Attack",
                    "Max Defence", "Max SpAttack", "Max SpDefence", "Max Speed");
            System.out.println(
                    "------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            while (rs.next()) {
                System.out.printf("%-5d %-15s %-15s %-7.2f %-7d %-11d %-11d %-11d %-8d %-12d %-12d %-12d %-12d %-12d%n",
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("type"),
                        rs.getDouble("xp"),
                        rs.getInt("level"),
                        rs.getInt("status_id"),
                        rs.getInt("evolved_id"),
                        rs.getInt("devolved_id"),
                        rs.getInt("max_hp"),
                        rs.getInt("max_attack"),
                        rs.getInt("max_defence"),
                        rs.getInt("max_spAttack"),
                        rs.getInt("max_spDefence"),
                        rs.getInt("max_speed"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void displayAllSpells() {
        String sql = "SELECT * FROM spells";

        try (Connection conn = DriverManager.getConnection(DB_URL);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            System.out.printf("%-5s %-15s %-15s %-15s %-7s %-9s %-5s %-7s %-25s%n", "ID", "Name", "Type",
                    "Classification", "Power", "Accuracy", "PP", "Direct", "Description");
            System.out.println(
                    "-------------------------------------------------------------------------------------------------------------");
            while (rs.next()) {
                System.out.printf("%-5d %-15s %-15s %-15s %-7d %-9d %-5d %-7b %-25s%n",
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("type"),
                        rs.getString("classification"),
                        rs.getInt("power"),
                        rs.getInt("accuracy"),
                        rs.getInt("pp"),
                        rs.getBoolean("direct"),
                        rs.getString("description"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getLastInsertedPokemonId() {
        String sql = "SELECT id FROM pokemons ORDER BY id DESC LIMIT 1";

        try (Connection conn = DriverManager.getConnection(DB_URL);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int getLastInsertedSpellId() {
        String sql = "SELECT id FROM spells ORDER BY id DESC LIMIT 1";

        try (Connection conn = DriverManager.getConnection(DB_URL);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                System.out.println("registered at " + rs.getInt("id"));
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    
    public void setPokemonData(Pokemon pokemon, int id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        String sql = "UPDATE pokemons SET name = ?, type = ?, status_id = ?, evolved_id = ?, devolved_id = ? WHERE id = ?";
    
        try {
            connection = DriverManager.getConnection(DB_URL);
            preparedStatement = connection.prepareStatement(sql);
            System.out.println("Debug flag" + pokemon.getName());
            preparedStatement.setString(1, pokemon.getName());
            preparedStatement.setString(2, pokemon.getType());
            preparedStatement.setInt(3, addStatus(pokemon.getStatus()));
            preparedStatement.setInt(4, pokemon.getEvolved() == null ? -1 : pokemon.getEvolved().getId());
            preparedStatement.setInt(5, pokemon.getDevolved() == null ? -1 : pokemon.getDevolved().getId());
            preparedStatement.setInt(6, id);
            preparedStatement.executeUpdate();

            addPokemonSpells(id, pokemon.getSpell());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }


private void setPokemonSpells(int pokemonId, Spell[] spells) throws SQLException {
String sql = "UPDATE pokemon_spells set(pokemon_id, spell_id) VALUES (?, ?)";
try (Connection conn = DriverManager.getConnection(DB_URL);
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
    for (Spell spell : spells) {
        if (spell != null) { // Add this check
            pstmt.setInt(1, pokemonId);
            pstmt.setInt(2, spell.getId());
            pstmt.executeUpdate();
        } else {
            System.out.println("Error: Could not find spell with ID ");
        }
    }
}
}
    

    public void setSpellData(Spell spell, int id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        String sql = "UPDATE spells SET name = ?, type = ?, classification = ?, power = ?, accuracy = ?, pp = ?, is_direct = ?, description = ? WHERE id = ?";

        try {
            connection = DriverManager.getConnection(DB_URL);
            preparedStatement = connection.prepareStatement(sql);
            
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, spell.getName());
            preparedStatement.setString(2, spell.getType());
            preparedStatement.setString(3, spell.getClassification());
            preparedStatement.setInt(4, spell.getPower());
            preparedStatement.setInt(5, spell.getAccuracy());
            preparedStatement.setInt(6, spell.getPp());
            preparedStatement.setBoolean(7, spell.isDirect());
            preparedStatement.setString(8, spell.getDescription());
            preparedStatement.setInt(9, id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void deletePokemonData(int pokemonId) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DriverManager.getConnection(DB_URL);

            // 進化ポケモンの外部キーを解除
            String updateEvolvedIdQuery = "UPDATE pokemons SET evolved_id = -1 WHERE evolved_id = ?";
            preparedStatement = connection.prepareStatement(updateEvolvedIdQuery);
            preparedStatement.setInt(1, pokemonId);
            preparedStatement.executeUpdate();
            preparedStatement.close();

            // 後退ポケモンの外部キーを解除
            String updateDeevolvedIdQuery = "UPDATE pokemons SET deevolved_id = -1 WHERE deevolved_id = ?";
            preparedStatement = connection.prepareStatement(updateDeevolvedIdQuery);
            preparedStatement.setInt(1, pokemonId);
            preparedStatement.executeUpdate();
            preparedStatement.close();

            // ポケモンデータを削除
            String deletePokemonQuery = "DELETE FROM pokemons WHERE id = ?";
            preparedStatement = connection.prepareStatement(deletePokemonQuery);
            preparedStatement.setInt(1, pokemonId);
            preparedStatement.executeUpdate();
            preparedStatement.close();

            // Statusデータを削除
            String deleteStatusQuery = "DELETE FROM statuses WHERE pokemon_id = ?";
            preparedStatement = connection.prepareStatement(deleteStatusQuery);
            preparedStatement.setInt(1, pokemonId);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void deleteSpellData(int spellId) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DriverManager.getConnection(DB_URL);

            // 中間テーブルの外部キーを解除
            String deletePokemonSpellQuery = "DELETE FROM pokemon_spells WHERE spell_id = ?";
            preparedStatement = connection.prepareStatement(deletePokemonSpellQuery);
            preparedStatement.setInt(1, spellId);
            preparedStatement.executeUpdate();
            preparedStatement.close();

            // Spellデータを削除
            String deleteSpellQuery = "DELETE FROM spells WHERE id = ?";
            preparedStatement = connection.prepareStatement(deleteSpellQuery);
            preparedStatement.setInt(1, spellId);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void close() {
        try {
            Connection conn = DriverManager.getConnection(DB_URL);
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}