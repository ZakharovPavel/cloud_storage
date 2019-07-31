package ru.zakharov.server;

import java.sql.*;

public class AuthService {

    private static Connection connection;
    private static Statement stmt;

    public static void connect() throws SQLException, ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:A:/Mine/Java/cloud_storage/server/usersDB.db");
        stmt = connection.createStatement();
    }

    public static boolean checkLogin(String target) throws SQLException {
        String sql = String.format("SELECT nickname FROM blacklist where " +
                "nickname = '%s'", target);
        ResultSet rs = stmt.executeQuery(sql);

        if (rs.next()) {
            return true;
        }
        return false;
    }

    public static void createAnAccount(String login, String pass) {

    }

    public static int getIdByLoginAndPass(String login, String pass) throws SQLException {
        String sql = String.format("SELECT login FROM users where " +
                "login = '%s' and password = '%s'", login, pass);
        ResultSet rs = stmt.executeQuery(sql);

        int id = -1;
        if (rs.next()) {
            id = rs.getInt(1);
        }
        return id;
    }

    public static void disconnect() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
