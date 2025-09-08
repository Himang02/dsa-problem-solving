
public class SquareRoot{
    public static void main(String[] args) {
        // System.out.println("Square root of 0 is: " + getSquareRootUpto2DecimalPoint(0.01f));
        for(int i=0; i<=20; i++){
            System.out.println("Square root of " + i + " is: " + String.format("%.2f", getSquareRootUpto2DecimalPoint(i))+" =="+String.format("%.2f", Math.sqrt(i)));

        }
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
    
    private static float getSquareRootUpto2DecimalPoint(int n){
        float start = 0, end = n+0.01f;
        while(end-start>0.01){
            float mid = start + (end-start)/2;
            if(mid*mid <= n){
                start = mid;
            }
            else{
                end = mid;
            }
            // System.out.println(start + " " + end);
        }
        return start;
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