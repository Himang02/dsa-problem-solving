import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

public class FastIO {
    // Example usage of FastIO class
    public static void main(String[] args) throws Exception {
        // TODO: Implement Fast I/O methods here

        // Approach - 1: System.in and System.out
        System.out.print("Enter a number: ");
        int num = System.in.read();  // reads a single byte - returns ascii value - comes from java.lang package (automatically imported)
        System.out.println("You entered in System.in.read(): " + num);

        // Approach - 2: BufferedReader and InputStreamReader
        InputStreamReader isr = new InputStreamReader(System.in);   // comes from java.io package
        BufferedReader br = new BufferedReader(isr);                // comes from java.io package
        if(System.in.available() > 0){
            System.out.println("Clearing the input buffer...");
            br.readLine();
        }
        System.out.print("Enter a number: ");          
        int n = Integer.parseInt(br.readLine());
        System.out.println("You entered in bufferReader.readLine(): " + n);
        // br.close();

        // Approach - 3: Scanner class
        Scanner sc = new Scanner(System.in);          // comes from java.util package
        if(System.in.available() > 0){
            System.out.println("Clearing the input buffer...");
            sc.nextLine();
        }
        System.out.print("Enter a number: ");
        int number = sc.nextInt();
        System.out.println("You entered in scanner.nextInt(): " + number);
    }
}