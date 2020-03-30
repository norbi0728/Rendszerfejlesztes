package marketplace.database;

import java.sql.*;

public class Database {
    public static Database database = null;
    private Connection connection;

    private Database(String name) {
        try {
            Class.forName("org.h2.Driver");
            connection = DriverManager.getConnection("jdbc:h2:file:" + name);
            createTablesIfNotExists();
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Database getDatabase() {
        if (database == null) {
            database = new Database("./mydatabase");
        }
        return database;
    }

    public void clearTables() throws SQLException {
        connection.createStatement().execute(
                "DROP TABLE USERS"
        );
    }

    public boolean userExists(String name) {
        return (getPasswordHash(name) != null);
    }

    /**
     * You need to make sure that no such user exists yet.
     */
    public void addUser(String name, String passwordHash) {
        try {
            String sql = "INSERT INTO USERS (NAME, PASSWORD_HASH) VALUES(?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, passwordHash);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @return null, if no such user
     */
    public String getPasswordHash(String userName) {
        String result;
        try {
            ResultSet resultSet = connection.createStatement().executeQuery(
                    "SELECT * FROM USERS WHERE NAME='" + userName + "'"
            );
            resultSet.first();
            result = resultSet.getString("PASSWORD_HASH");
        } catch (SQLException e) {
            return null;
        }
        return result;
    }

    private final void createTablesIfNotExists() throws SQLException {
        connection.createStatement().execute(
                "CREATE TABLE IF NOT EXISTS USERS("
                        + "ID INT AUTO_INCREMENT, "
                        + "NAME VARCHAR, "
                        + "PASSWORD_HASH VARCHAR, "
                        + "PRIMARY KEY (ID))"
        );
    }

    public static void main(String[] args) {
        Database database = getDatabase();

//        database.addUser("János", "12345");
//        database.addUser("Péter", "54321");
        System.out.println(database.getPasswordHash("Jánoss"));
    }
}
