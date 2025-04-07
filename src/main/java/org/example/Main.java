package org.example;

import java.sql.*;

public class Main {
    public static void main(String[] args) {

        /* Load JDBC Driver (OPTIONAL for modern JDBC versions) */
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("PostgreSQL JDBC Driver not found. Include it in your library path.");
            e.printStackTrace();
            return; // Exit if driver is not found
        }

        String url = "jdbc:postgresql://localhost/Lab1";
        String user = "postgres";
        String pass = "new_secure_password";
        Connection connexion = null;
        try {
            connexion = DriverManager.getConnection(url, user, pass);
            Statement statement = connexion.createStatement();
            /* Requests to bdd will be here */
            System.out.println("Bdd Connected");

            //displayDepartment(connexion);
            moveDepartment(connexion, 7369, 30);
            //displayTable("emp");


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connexion != null)
                try {
                    connexion.close();
                } catch (SQLException ignore) {
                    ignore.printStackTrace();
                }
        }

    }

    public static void displayDepartment(Connection connexion) throws SQLException {
        Statement statement = connexion.createStatement();
        ResultSet resultat = statement.
                executeQuery("SELECT deptno, dname, loc FROM dept");

        while (resultat.next()) {
            int deptno = resultat.getInt("deptno");
            String dname = resultat.getString("dname");
            String location = resultat.getString("loc");
            System.out.println("Department " + deptno + " is for "
                    + dname + " and located in ? " + location);
        }
        resultat.close();
    }

    public static void moveDepartment(Connection connexion, int deptno, int empno)
            throws SQLException {
        String command = "UPDATE EMP set DEPTNO = ? WHERE EMPNO = ?";
        try (PreparedStatement updateEMP = connexion.prepareStatement(command)) {
            updateEMP.setLong(1, deptno);
            updateEMP.setLong(2, empno);
            updateEMP.executeUpdate();
            System.out.println("Employee No: " + empno + " has been moved to " + deptno);
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    public static void displayTable(String tableName) throws SQLException {
        String url = "jdbc:postgresql://localhost/Lab1";
        String user = "postgres";
        String pass = "new_secure_password";

        String query = "SELECT * FROM ?"; //PreparedStatement

        try (Connection connection = DriverManager.getConnection(url, user, pass)) {
            /*try (Statement statement = connection.createStatement()) {
                try (ResultSet resultSet = statement.executeQuery("SELECT * FROM " + tableName)) {
                    ResultSetMetaData metaData = resultSet.getMetaData();
                    int columnCount = metaData.getColumnCount(); */
            /* -----------------------------------PreparedStatement-----------------------------------*/
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, tableName);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    ResultSetMetaData metaData = resultSet.getMetaData();
                    int columnCount = metaData.getColumnCount();
                    /*-----------------------------------PreparedStatement----------------------------------- */
                    for (int i = 1; i <= columnCount; i++) {
                        System.out.print(metaData.getColumnName(i) + "\t| ");
                    }
                    System.out.println();

                    while (resultSet.next()) {
                        for (int i = 1; i <= columnCount; i++) {
                            System.out.print(resultSet.getString(i) + "\t| ");
                        }
                        System.out.println();
                    }
                    //resultSet.close();
                    //statement.close();

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}




