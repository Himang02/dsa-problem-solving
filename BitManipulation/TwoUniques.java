import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.StringTokenizer;



public class TwoUniques {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(st.nextToken());
        int[] arr = new int[n];
        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < n; i++) {
            arr[i] = Integer.parseInt(st.nextToken());
        }

        int[] result = findTwoUniques(arr);
        System.out.println(result[0] + " " + result[1]);
    }

    // TC: O(N), SC: O(1)
    private static int[] findTwoUniques(int[] arr) {
        int xorAll = 0;
        for (int num : arr) {
            xorAll ^= num;
        }

        // Find rightmost set bit
        int setBitMask = xorAll & -xorAll;

        int unique1 = 0, unique2 = 0;
        for (int num : arr) {
            if ((num & setBitMask) != 0) {
                unique1 ^= num;
            } else {
                unique2 ^= num;
            }
        }

        return new int[]{unique1, unique2};
    }
}