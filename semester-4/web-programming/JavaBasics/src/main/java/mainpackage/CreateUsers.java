package mainpackage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Main test-harness class for Phase 1. Reads student data from a text
 * file, validates it, builds Students objects, accepts additional user
 * data from terminal input, and prints formatted summaries.
 *
 * @author Petros Travlos P21173
 */
public class CreateUsers {

    /**
     * Program entry point.
     *
     * @param args not used
     */
    public static void main(String[] args) {
        List<Students> students = loadStudentsFromFile("students.txt");

        System.out.println("\n--- Students loaded from file ---");
        for (Students s : students) {
            System.out.println(s);
        }
        Scanner scanner = new Scanner(System.in);

        // ===== NEW BLOCK #1 =====
        // Assignment requires: one plain Users object, created directly.
        Users genericUser = new Users("baseuser", "Generic", "User", "N/A");
        System.out.println("\n--- Plain Users object ---");
        System.out.println(genericUser);
        // ===================================================

        // ===== NEW BLOCK #2 =====
        // Assignment requires: a Students object built specifically via
        // Scanner keyboard input (separate from the file-based ones above).
        System.out.println("\n--- Now let's create a Student via terminal input ---");
        Students keyboardStudent = createStudentFromInput(scanner);
        System.out.println("Created: " + keyboardStudent);
        // ================================================================

        // Now take some interactive terminal input to create a Professor
        // and a Secretary, exercising the other subclasses too.
        System.out.println("\n--- Now let's create a Professor via terminal input ---");
        Professors professor = createProfessorFromInput(scanner);
        System.out.println("Created: " + professor);

        System.out.println("\n--- Now let's create a Secretary via terminal input ---");
        Secretaries secretary = createSecretaryFromInput(scanner);
        System.out.println("Created: " + secretary);

        // Quick demonstration that Secretaries/Professors/Courses/Grades
        // classes work together correctly.
        System.out.println("\n--- Demonstrating course assignment ---");
        Courses course = new Courses("CS101", "Intro to Programming");
        secretary.assignProfessorToCourse(professor, course.getCourseName());
        course.setAssignedProfessor(professor);
        System.out.println(course);

        if (!students.isEmpty()) {
            System.out.println("\n--- Demonstrating a Grade record ---");
            Grades grade = new Grades(students.get(0), course, "2026-Spring", 8.5);
            System.out.println(grade);
        }

        // Final summary using the static usersCounter from Users.
        System.out.println("\n--- Summary ---");
        System.out.println("Total users created so far: " + Users.getUsersCounter());

        scanner.close();
    }

    /**
     * Reads student records from a comma-separated text file, validating
     * each line. Lines with invalid data are skipped with an error message,
     * rather than crashing the whole program.
     *
     * Expected line format: username,name,surname,department,registrationNumber
     *
     * @param filePath path to the students text file
     * @return a list of successfully parsed Students objects
     */
    public static List<Students> loadStudentsFromFile(String filePath) {
        List<Students> students = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (line.trim().isEmpty()) {
                    continue; // skip blank lines silently
                }

                String[] fields = line.split(",");
                if (fields.length != 5) {
                    System.out.println("Line " + lineNumber + " skipped: expected 5 fields, found " + fields.length);
                    continue;
                }

                try {
                    // Validate each field; throws InvalidDataException on failure
                    DataValidator.validateStringField(fields[0], "username");
                    DataValidator.validateStringField(fields[1], "name");
                    DataValidator.validateStringField(fields[2], "surname");
                    DataValidator.validateStringField(fields[3], "department");
                    int regNumber = DataValidator.validateRegistrationNumber(fields[4], "registrationNumber");

                    Students student = new Students(fields[0].trim(), fields[1].trim(),
                            fields[2].trim(), fields[3].trim(), regNumber);
                    students.add(student);

                } catch (InvalidDataException e) {
                    // Catch per-line so one bad row doesn't stop the whole file from loading
                    System.out.println("Line " + lineNumber + " skipped: " + e.getMessage());
                }
            }

        } catch (IOException e) {
            System.out.println("Could not read file \"" + filePath + "\": " + e.getMessage());
        }

        return students;
    }

    /**
     * Prompts the user via terminal input to create a Professors object,
     * re-prompting on invalid input rather than crashing.
     *
     * @param scanner the Scanner reading from System.in
     * @return a validated Professors object
     */
    public static Professors createProfessorFromInput(Scanner scanner) {
        while (true) {
            try {
                System.out.print("Enter username: ");
                String username = scanner.nextLine();
                DataValidator.validateStringField(username, "username");

                System.out.print("Enter name: ");
                String name = scanner.nextLine();
                DataValidator.validateStringField(name, "name");

                System.out.print("Enter surname: ");
                String surname = scanner.nextLine();
                DataValidator.validateStringField(surname, "surname");

                System.out.print("Enter department: ");
                String department = scanner.nextLine();
                DataValidator.validateStringField(department, "department");

                return new Professors(username, name, surname, department);

            } catch (InvalidDataException e) {
                System.out.println("Invalid input: " + e.getMessage() + " Please try again.");
            }
        }
    }
    /**
     * Prompts the user via terminal input to create a Students object,
     * validating the registration number as a genuine integer.
     * This directly fulfills the assignment's requirement to build a
     * Students object using Scanner input for the constructor arguments.
     *
     * @param scanner the Scanner reading from System.in
     * @return a validated Students object
     */

    public static Students createStudentFromInput(Scanner scanner) {
        while (true) {
            try {
                System.out.print("Enter username: ");
                String username = scanner.nextLine();
                DataValidator.validateStringField(username, "username");

                System.out.print("Enter name: ");
                String name = scanner.nextLine();
                DataValidator.validateStringField(name, "name");

                System.out.print("Enter surname: ");
                String surname = scanner.nextLine();
                DataValidator.validateStringField(surname, "surname");

                System.out.print("Enter department: ");
                String department = scanner.nextLine();
                DataValidator.validateStringField(department, "department");

                System.out.print("Enter registration number: ");
                String regInput = scanner.nextLine();
                int registrationNumber = DataValidator.validateRegistrationNumber(regInput, "registrationNumber");

                return new Students(username, name, surname, department, registrationNumber);

            } catch (InvalidDataException e) {
                System.out.println("Invalid input: " + e.getMessage() + " Please try again.");
            }
        }
    }
    /**
     * Prompts the user via terminal input to create a Secretaries object,
     * re-prompting on invalid input rather than crashing.
     *
     * @param scanner the Scanner reading from System.in
     * @return a validated Secretaries object
     */
    public static Secretaries createSecretaryFromInput(Scanner scanner) {
        while (true) {
            try {
                System.out.print("Enter username: ");
                String username = scanner.nextLine();
                DataValidator.validateStringField(username, "username");

                System.out.print("Enter name: ");
                String name = scanner.nextLine();
                DataValidator.validateStringField(name, "name");

                System.out.print("Enter surname: ");
                String surname = scanner.nextLine();
                DataValidator.validateStringField(surname, "surname");

                System.out.print("Enter department: ");
                String department = scanner.nextLine();
                DataValidator.validateStringField(department, "department");

                return new Secretaries(username, name, surname, department);

            } catch (InvalidDataException e) {
                System.out.println("Invalid input: " + e.getMessage() + " Please try again.");
            }
        }
    }
}