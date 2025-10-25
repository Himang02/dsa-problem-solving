import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class BufferedReaderFastIO {
    private BufferedReader br;
    private InputStreamReader isr;
    private StringTokenizer st;

    BufferedReaderFastIO() {
        isr = new InputStreamReader(System.in);   // comes from java.io package
        br = new BufferedReader(isr);              // comes from java.io package
    }

    private String getNextToken() throws Exception {
        if(st == null || !st.hasMoreTokens()){
            st = new StringTokenizer(br.readLine());
        }
        return st.nextToken();
    }

    public int nextInt() throws Exception {
        return Integer.parseInt(getNextToken());
    }

    public double nextDouble() throws Exception {
        return Double.parseDouble(getNextToken());
    }

    public String nextString() throws Exception {
        return getNextToken();
    }

    public long nextLong() throws Exception {
        return Long.parseLong(getNextToken());
    }

    public String nextLine() throws Exception {
        st = null; // Invalidate the current tokenizer
        return br.readLine();
    }

    public void close() throws Exception {
        br.close(); // Closing BufferedReader also closes the underlying InputStreamReader and System.in
    }


    public static void main(String[] args) throws Exception {
        BufferedReaderFastIO reader = new BufferedReaderFastIO();

        System.out.print("Enter an integer: ");
        int intValue = reader.nextInt();
        System.out.println("You entered integer: " + intValue);

        System.out.print("Enter a double: ");
        double doubleValue = reader.nextDouble();
        System.out.println("You entered double: " + doubleValue);

        System.out.print("Enter a string: ");
        String stringValue = reader.nextString();
        System.out.println("You entered string: " + stringValue);
        
        System.out.print("Enter a line: ");
        String lineValue = reader.nextLine();
        System.out.println("You entered line: " + lineValue);

        // matrix
        System.out.print("Enter number of rows and columns: ");
        int rows = reader.nextInt();
        int cols = reader.nextInt();
        int[][] matrix = new int[rows][cols];
        System.out.println("Enter matrix elements row-wise:");
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix[i][j] = reader.nextInt();
            }
        }
        System.out.println("You entered the following matrix:");
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }

        reader.close(); // Closing BufferedReader also closes the underlying InputStreamReader and System.in
    }


}