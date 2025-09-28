import java.util.*;

public class SumOfINumbers {
    public static void main(String[] args) {
        int num =  (int)(Math.pow(10, 8));
        int i = approach1(num);
        System.out.println("For N = " + num + ", i = " + i + ", sum = " + (i*(i+1)/2));
        int j = approach2(num);
        System.out.println("For N = " + num + ", j = " + j + ", sum = " + (j*(j+1)/2));
        
        num =  1;
        i = approach1(num);
        System.out.println("For N = " + num + ", i = " + i + ", sum = " + (i*(i+1)/2));
        j = approach2(num);
        System.out.println("For N = " + num + ", j = " + j + ", sum = " + (j*(j+1)/2));
        
    }

    // Approach-1 : This will not work for large N as i can be very large
    public static int approach1(int N){
        int start = 0, end = N+1;
        while(end - start > 1){
            int mid = start + (end - start)/2;
            if(predicate(mid, N) == 0){
                start = mid;
            }
            else{
                end = mid;
            }
        }
        return start;
    }

    public static int approach2(int N){
        int start, end = 1;
        while(predicate(end, N) != 1){
            end = (int)(2 * end);
        }
        start = end/2;
        while(end - start > 1){
            int mid = start + (end - start)/2;
            if(predicate(mid, N) == 0){
                start = mid;
            }
            else{
                end = mid;
            }
        }
        return start;
    }

    private static int predicate(int i, int N){
        if(i*(i+1)/2 <= N){
            // System.out.println(i + " " + (i*(i+1)/2) + " " + N);
            return 0;
        }
        else{
            return 1;
        }
    }
}