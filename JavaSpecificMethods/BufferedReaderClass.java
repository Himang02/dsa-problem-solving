import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;


/*
 * reads text lines efficiently (with internal buffering)
 * method br.readLine() returns a whole line as a String (not individual numbers or tokens)
 * You then parse that line into numbers using split() or StringTokenizer
 * StringTokenizer is better as it does not use regex internally like split()
 * Object creation overhead is less with StringTokenizer
 * Better for competitive programming where speed matters

*/
public class BufferedReaderClass {
    public static void main(String[] args) throws Exception {
        // BufferedReader and InputStreamReader
        InputStreamReader isr = new InputStreamReader(System.in);   // comes from java.io package
        BufferedReader br = new BufferedReader(isr);                // comes from java.io package

        System.out.print("Enter an integer: ");
        int intValue = Integer.parseInt(br.readLine());
        System.out.println("You entered integer: " + intValue);

        System.out.print("Enter a double: ");
        double doubleValue = Double.parseDouble(br.readLine());
        System.out.println("You entered double: " + doubleValue);

        System.out.print("Enter a string: ");
        String stringValue = br.readLine();
        System.out.println("You entered string: " + stringValue);

        // matrix
        System.out.print("Enter number of rows and columns: ");
        StringTokenizer st = new StringTokenizer(br.readLine());
        int rows = Integer.parseInt(st.nextToken());
        int cols = Integer.parseInt(st.nextToken());
        int[][] matrix = new int[rows][cols];
        System.out.println("Enter matrix elements row-wise:");
        for (int i = 0; i < rows; i++) {
            StringTokenizer rowSt = new StringTokenizer(br.readLine());
            for (int j = 0; j < cols; j++) {
                matrix[i][j] = Integer.parseInt(rowSt.nextToken());
            }
        }
        System.out.println("You entered the following matrix:");
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }


        // eof buffer reader
        // System.out.println("Enter pairs of integers to sum (Ctrl+Z to end):");
        // String line;
        // while ((line = br.readLine()) != null && !line.isEmpty()) {
        //     StringTokenizer eofst = new StringTokenizer(line);
        //     int a = Integer.parseInt(eofst.nextToken());
        //     int b = Integer.parseInt(eofst.nextToken());
        //     System.out.println(a + b);
        // }

        String line = br.readLine();
        StringTokenizer roweost = new StringTokenizer(line);
        while(roweost.hasMoreTokens()){
            // int val = Integer.parseInt(roweost.nextToken());
            System.out.println("Read integer: " + roweost.nextToken());
        }

        br.close(); // Closing BufferedReader also closes the underlying InputStreamReader and System.in
    }
}