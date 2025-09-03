import java.util.*;

public class ToeplitzMatrix {
    public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    int n = sc.nextInt();
    int m = sc.nextInt();
    
    int[][] matrix = new int[n][m];
    for(int row = 0; row< n; row++){
      for(int col = 0; col<m; col++){
        matrix[row][col] = sc.nextInt();
      }
    }
    
    int startingCol = 0, startingRow = n-1;
    int output = 1;
    while(startingRow >= 0 && output!=0){
      int col = 0, row = startingRow;
      int current = matrix[row][col];
      int next;
      row++; col++;
      while(col < m && row < n){
        next = matrix[row][col];
        if(next!=current){
          output = 0;
          break;
        }
        current = next;
        col++; row++;
      }
      startingRow--;
    }
    
    startingRow = 0; startingCol = 1;
    while(startingCol < m && output!=0){
      int col = startingCol, row = 0;
      int current = matrix[row][col];
      int next;
      row++; col++;
      while(col < m && row < n){
        next = matrix[row][col];
        if(next!=current){
          output = 0;
          break;
        }
        col++; row++;
        current = next;
      }
      startingCol++;
    }
    
    System.out.print(output);
  }
}