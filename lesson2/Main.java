package homework2;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) {
        String filePath = "src/homework2/test.txt";

        int studentId = 4;
        String studentName = "Bobik";
        int studentScore = 10;


        try {
            DB.connect();
        } catch (SQLException e) {
            e.printStackTrace();
            e.getCause();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            DB.createTable();

            ArrayList<Triplet<Integer, String, Integer>> res = parseTestFile(filePath);
            for (Triplet<Integer, String, Integer> t : res) {
                DB.addStudent(studentName, studentScore);
                DB.updateStudentById(t.getFirst(), t.getSecond(), t.getThird());
            }

            DB.addStudent(studentName, studentScore);

            Triplet<Integer, String, Integer> triplet = DB.getStudentById(studentId);
            System.out.println(triplet.getFirst() + " " + triplet.getSecond() + " " + triplet.getThird());

            DB.deleteStudentById(studentId);

            DB.dropTable();
        } catch (SQLException e) {
            e.printStackTrace();
            e.getCause();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            e.getCause();
        }

        try {
            DB.disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    public static ArrayList<Triplet<Integer, String, Integer>> parseTestFile(String filePath)
            throws FileNotFoundException {
        ArrayList<Triplet<Integer, String, Integer>> res = new ArrayList<>();

        Scanner sc = new Scanner(new File(filePath));

        while (sc.hasNext()) {
            String line = sc.nextLine();
            if (Pattern.matches("^\\d+\\s.+\\s\\d+$", line)) {
                String[] tokens = line.split(" ");
                res.add(new Triplet<>(new Integer(tokens[0]), tokens[1], new Integer(tokens[2])));
            }
        }

        sc.close();

        return res;
    }
}
