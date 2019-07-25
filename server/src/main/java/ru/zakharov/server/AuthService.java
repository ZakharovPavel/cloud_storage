package ru.zakharov.server;

import java.sql.*;

public class AuthService {

    private static Connection connection;
    private static Statement stmt;

    public static void connect() throws SQLException, ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:server/usersDB.db");
        stmt = connection.createStatement();
    }

//    public static void userRegistration() {
//        String sql = String.format("SELECT nickname FROM main where " +
//                "login = '%s' and password = '%s'", login, pass);
//        ResultSet rs = stmt.executeQuery(sql);
//    }

    public static void disconnect() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
