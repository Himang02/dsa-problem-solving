import java.util.*;

public class SumOfINumbers {
    public static void main(String[] args) {
        for(int num = 0; num < 50; num++){
            int i = getI(num);
            System.out.println("For N = " + num + ", i = " + i + ", sum = " + (i*(i+1)/2));
        }
    }

    public static int getI(int N){
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

    private static int predicate(int i, int N){
        if(i*(i+1)/2 <= N){
            return 0;
        }
        else{
            return 1;
        }
    }
}