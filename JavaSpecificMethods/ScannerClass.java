import java.util.Scanner;

/*
 * Does buffering internally but not as efficient as BufferedReader
 * Wrapping: System.in -> InputStreamReader -> Scanner
 * InputStreamReader already buffers small chunks, Scanner then reads characters from the reader and applies regex parsing to extract tokens
 * But reads smaller chunks (1-8 KB) via InputStreamReader.
 * Uses regex parsing which adds overhead.
 * Creates many temporary objects during parsing.
 * Bottlenecked is not the I/O itself but the parsing logic.
 * Reads line by line, token by token (newline doesn't matter).
 * sc.next() reads next token (space, newline, tab separated)
 * If you mix nextInt() and nextLine(), be careful — the newline after an integer remains in the buffer. You must consume it before calling nextLine()
 */

public class ScannerClass {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter an integer: ");
        int intValue = sc.nextInt();
        System.out.println("You entered integer: " + intValue);

        System.out.print("Enter a double: ");
        double doubleValue = sc.nextDouble();
        System.out.println("You entered double: " + doubleValue);

        sc.nextLine(); // Consume the leftover newline

        System.out.print("Enter a string: ");
        String stringValue = sc.nextLine();
        System.out.println("You entered string: " + stringValue);

        // matrix
        System.out.print("Enter number of rows and columns: ");
        int rows = sc.nextInt();
        int cols = sc.nextInt();
        int[][] matrix = new int[rows][cols];
        System.out.println("Enter matrix elements row-wise:");
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix[i][j] = sc.nextInt();
            }
        }
        System.out.println("You entered the following matrix:");
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }

        // EOF handling
        // System.out.print("Enter integers :");
        // while (sc.hasNext()) {
        //     String val = sc.next();
        //     System.out.println("Read integer: " + val);
        // }

        // Reading with delimiter
        // sc.useDelimiter(","); // set comma as delimiter
        // System.out.print("Enter values separated by commas: ");
        // while (sc.hasNext()) {
        //     String val = sc.next();
        //     System.out.println("Read value: " + val);
        // }

        sc.close();
    }
}