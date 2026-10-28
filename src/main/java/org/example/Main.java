package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {

    static String url = "jdbc:mysql://localhost:3306/jdbc";
    static String username = "root";
    static String password = System.getenv("MYSQL_PASSWORD");

    static Scanner sc = new Scanner(System.in);

    // Establish connection with MySQL
    static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    // 1. Display all students
    static void getAll() {
        String query = "SELECT * FROM student";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            boolean found = false;

            while (rs.next()) {
                found = true;

                System.out.println("ID: " + rs.getInt("id"));
                System.out.println("Name: " + rs.getString("name"));
                System.out.println("Age: " + rs.getInt("age"));
                System.out.println("Gender: " + rs.getString("gender"));
                System.out.println("Aadhar: " + rs.getString("aadhar"));
                System.out.println("-------------------------");
            }

            if (!found) {
                System.out.println("No students found.");
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving students.");
            e.printStackTrace();
        }
    }

    // 2. Display a student by ID
    static void getById(int id) {
        String query = "SELECT * FROM student WHERE id = ?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    System.out.println("ID: " + rs.getInt("id"));
                    System.out.println("Name: " + rs.getString("name"));
                    System.out.println("Age: " + rs.getInt("age"));
                    System.out.println("Gender: " + rs.getString("gender"));
                    System.out.println("Aadhar: " + rs.getString("aadhar"));
                } else {
                    System.out.println("Student not found.");
                }
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving student.");
            e.printStackTrace();
        }
    }

    // 3. Insert a new student

    static void insert() {
        String query =
                "INSERT INTO student (name, age, gender, aadhar) VALUES (?, ?, ?, ?)";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            System.out.print("Enter student name: ");
            String name = sc.nextLine();

            System.out.print("Enter age: ");
            int age = Integer.parseInt(sc.nextLine());

            System.out.print("Enter gender: ");
            String gender = sc.nextLine();

            System.out.print("Enter Aadhar value: ");
            String aadhar = sc.nextLine();

            ps.setString(1, name);
            ps.setInt(2, age);
            ps.setString(3, gender);
            ps.setString(4, aadhar);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Student inserted successfully!");
            }

        } catch (NumberFormatException e) {
            System.out.println("Age must be a valid integer.");

        } catch (SQLException e) {

            if (e.getErrorCode() == 1062) {
                System.out.println("Aadhar number is already available!");

            } else {
                System.out.println("Unable to insert student. Please try again.");
                e.printStackTrace();
            }
        }
    }

    // 4. Update an existing student
    static void update() {
        String query =
                "UPDATE student SET name = ?, age = ?, gender = ?, aadhar = ? WHERE id = ?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            System.out.print("Enter student ID to update: ");
            int id = Integer.parseInt(sc.nextLine());

            System.out.print("Enter new name: ");
            String name = sc.nextLine();

            System.out.print("Enter new age: ");
            int age = Integer.parseInt(sc.nextLine());

            System.out.print("Enter new gender: ");
            String gender = sc.nextLine();

            System.out.print("Enter new Aadhar value: ");
            String aadhar = sc.nextLine();

            ps.setString(1, name);
            ps.setInt(2, age);
            ps.setString(3, gender);
            ps.setString(4, aadhar);
            ps.setInt(5, id);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Student updated successfully!");
            } else {
                System.out.println("Student ID not found.");
            }

        } catch (NumberFormatException e) {
            System.out.println("Age and ID must be valid integers.");
        } catch (SQLException e) {
            System.out.println("Error updating student.");
            e.printStackTrace();
        }
    }

    // 5. Delete a student
    static void delete() {
        String query = "DELETE FROM student WHERE id = ?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            System.out.print("Enter student ID to delete: ");
            int id = Integer.parseInt(sc.nextLine());

            ps.setInt(1, id);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Student deleted successfully!");
            } else {
                System.out.println("Student ID not found.");
            }

        } catch (NumberFormatException e) {
            System.out.println("ID must be a valid integer.");
        } catch (SQLException e) {
            System.out.println("Error deleting student.");
            e.printStackTrace();
        }
    }

    // Main menu
    public static void main(String[] args) {

        while (true) {
            System.out.println("\n===== STUDENT MANAGEMENT SYSTEM =====");
            System.out.println("1. Get All Students");
            System.out.println("2. Get Student By ID");
            System.out.println("3. Insert Student");
            System.out.println("4. Update Student");
            System.out.println("5. Delete Student");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");

            String choice = sc.nextLine();

            switch (choice) {
                case "1":
                    getAll();
                    break;

                case "2":
                    System.out.print("Enter student ID: ");
                    try {
                        int id = Integer.parseInt(sc.nextLine());
                        getById(id);
                    } catch (NumberFormatException e) {
                        System.out.println("ID must be a valid integer.");
                    }
                    break;

                case "3":
                    insert();
                    break;

                case "4":
                    update();
                    break;

                case "5":
                    delete();
                    break;

                case "6":
                    System.out.println("Program closed.");
                    sc.close();
                    return;

                default:
                    System.out.println("Invalid choice. Enter 1 to 6.");
            }
        }
    }
}

