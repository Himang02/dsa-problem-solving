
public class XorOfFirstNNumbers {

    public static void main(String[] args) {
        int n = 50; // Example input
        int result = xorFrom1ToN(n);
        // System.out.println("XOR of first " + Integer.toBinaryString(n) + " numbers is: " + result);
        int ans = 0;
        for(int i = 1; i <= n; i++) {
            ans ^= i;
            System.out.println(Integer.toBinaryString(ans) + " : " + i + " => " + Integer.toBinaryString(xorFrom1ToN(i)));
        }
    }

    private static int xorFrom1ToN(int n) {
        if (n % 4 == 0) {
            return n;
        } else if (n % 4 == 1) {
            return 1;
        } else if (n % 4 == 2) {
            return n + 1;
        } else {
            return 0;
        }
    }
}