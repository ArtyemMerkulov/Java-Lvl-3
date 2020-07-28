package homework2;

import java.sql.*;

public class DB {

    private static Connection connection;
    private static Statement stmt;
    private static PreparedStatement pstmt;

    public static void connect() throws SQLException, ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:src/homework2/main.db");
        stmt = connection.createStatement();
    }

    public static void disconnect() throws SQLException {
        connection.close();
    }

    public static void createTable() throws SQLException {
        String query = "CREATE TABLE students (" +
                "    id    INTEGER    PRIMARY KEY AUTOINCREMENT" +
                "                     NOT NULL" +
                "                     UNIQUE," +
                "    name  CHAR (240) NOT NULL," +
                "    score INTEGER    NOT NULL" +
                ");";

        stmt.executeUpdate(query);
    }

    public static void dropTable() throws SQLException {
        String query = "DROP TABLE students;";

        stmt.executeUpdate(query);
    }

    public static void addStudent(String name, int score) throws SQLException {
        String query = "INSERT INTO students (name, score) VALUES (?, ?);";

        pstmt = connection.prepareStatement(query);
        pstmt.setString(1, name);
        pstmt.setInt(2, score);

        pstmt.executeUpdate();
    }

    public static void deleteStudentById(int id) throws SQLException {
        String query = "DELETE FROM students WHERE id=?;";

        pstmt = connection.prepareStatement(query);
        pstmt.setInt(1, id);

        pstmt.executeUpdate();
    }

    public static Triplet<Integer, String, Integer> getStudentById(int id) throws SQLException {
        String query = "SELECT * FROM students WHERE id=?;"; // по заданию нужно получить "запись"

        pstmt = connection.prepareStatement(query);
        pstmt.setInt(1, id);

        ResultSet res = pstmt.executeQuery();

        Triplet<Integer, String, Integer> triplet = new Triplet<>();

        while (res.next()) {
            triplet.updateFirst(res.getInt(1));
            triplet.updateSecond(res.getString(2));
            triplet.updateThird(res.getInt(3));
        }

        return triplet;
    }

    public static void updateStudentById(int id, String name, int score) throws SQLException {
        String query = "UPDATE students SET name=?, score=? WHERE id=?;";

        pstmt = connection.prepareStatement(query);
        pstmt.setString(1, name);
        pstmt.setInt(2, score);
        pstmt.setInt(3, id);

        pstmt.executeUpdate();
    }
}
