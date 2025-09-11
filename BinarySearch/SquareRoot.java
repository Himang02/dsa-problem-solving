
public class SquareRoot{
    public static void main(String[] args) {
        // System.out.println("Square root of 0 is: " + getSquareRootUpto2DecimalPoint(0.01f));
        for(int i=214500; i<=215000; i++){
            System.out.println("Square root of " + i + " is: " + String.format("%.2f", getSquareRootUpto2DecimalPoint1(i))+" =="+String.format("%.2f", Math.sqrt(i)));

        }
            // System.out.println("Square root of " + 10 + " is: " + String.format("%.2f", getSquareRootUpto2DecimalPoint(10))+" =="+String.format("%.2f", Math.sqrt(10)));
    }

    private static int getSquareRoot(int n){
        int start = 0, end = n+1;
        while(end-start>1){
            int mid = start + (end-start)/2;
            if(mid*mid <= n){
                start = mid;
            }
            else{
                end = mid;
            }
        }
        return start;
    }
    
    private static float getSquareRootUpto2DecimalPoint1(int n){
        // Works till n = 214748
        long N = 10000*n; // making number 10000 times, so that sqaure root is 100 times
        long start = 0, end = N + 1;
        while(end-start>1){
            long mid = start + (end-start)/2;
            // System.out.println(mid);
            if(mid*mid <= N){   // N and mid are long because mid*mid can cause overflow
                // System.out.println("mid*mid: "+ mid*mid + " N: "+N);
                start = mid;
            }
            else{
                end = mid;
            }
            // System.out.println(start + " " + end);
        }
        return start/100.0f;
    }

    public static double squareRoot(int num, int precision) {
        int start = 0, end = num;
        double ans = 0.0;

        // Binary search for integer part
        while (start <= end) {
            int mid = (start + end) / 2;
            if (mid * mid == num) {
                return mid;
            } else if (mid * mid < num) {
                start = mid + 1;
                ans = mid;
            } else {
                end = mid - 1;
            }
        }

        // Refine result for decimal places
        double increment = 0.1;
        for (int i = 0; i < precision; i++) {
            while (ans * ans <= num) {
                ans += increment;
            }
            ans -= increment;
            increment /= 10;
        }

        return ans;
    }
}