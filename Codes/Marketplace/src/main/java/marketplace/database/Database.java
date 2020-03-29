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

    public void addUser(String name, String password) {
        try {
            String sql = "INSERT INTO USERS (NAME, PASSWORD) VALUES(?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, password);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void getUser(String name) {
        try {
            ResultSet resultSet = connection.createStatement().executeQuery(
                    "SELECT * FROM USERS WHERE NAME='" + name + "'"
            );
            resultSet.first();
            System.out.println("name: " + resultSet.getString("NAME"));
            System.out.println("password: " + resultSet.getString("PASSWORD"));
        } catch (SQLException e) {
            System.err.println("Hiba");;
        }
    }

    private final void createTablesIfNotExists() throws SQLException {
        connection.createStatement().execute(
                "CREATE TABLE IF NOT EXISTS USERS("
                        + "ID INT AUTO_INCREMENT, "
                        + "NAME VARCHAR, "
                        + "PASSWORD VARCHAR, "
                        + "PRIMARY KEY (ID))"
        );
    }

    public static void main(String[] args) {
        System.out.println("Hello world");
        Database database = getDatabase();
        try {
            //database.clearTables();
            database.createTablesIfNotExists();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        //database.addUser("János", "12345");
        //database.addUser("Péter", "54321");
        //database.getUser("János");
        database.getUser("Péter");
        database.getUser("Sarolta");
    }
}
