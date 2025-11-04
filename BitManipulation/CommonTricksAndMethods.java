

public class CommonTricksAndMethods {
    public static void main(String[] args){
        // Common Integer and Long methods
        int x = 26;
        int y = -1;

        System.out.println("Integer to Binary String of " + x + " : " + Integer.toBinaryString(x));
        System.out.println("Integer to Binary String of " + y + " : " + Integer.toBinaryString(y));

        System.out.println("Count of set bits in " + x + " using in-build method : " + Integer.bitCount(x));
        System.out.println("Count of set bits in " + y + " using in-build method : " + Integer.bitCount(y));
        System.out.println("Count of set bits in " + x + " using Brian Kernighan's Algorithm method : " + getSetBits(x));
        System.out.println("Count of set bits in " + x + " using Brian Kernighan's Algorithm method : " + getSetBits(y));

        System.out.println("Leading zeros in " + x + " : " + Integer.numberOfLeadingZeros(x));
        System.out.println("Leading zeros in " + y + " : " + Integer.numberOfLeadingZeros(y));
        System.out.println("Trailing zeros in " + x + " : " + Integer.numberOfTrailingZeros(x));
        System.out.println("Trailing zeros in " + y + " : " + Integer.numberOfTrailingZeros(y));
        System.out.println("Highest one bit in " + x + " : " + Integer.toBinaryString(Integer.highestOneBit(x)));
        System.out.println("Highest one bit in " + y + " : " + Integer.toBinaryString(Integer.highestOneBit(y)));
        System.out.println("Lowest one bit in " + x + " : " + Integer.toBinaryString(Integer.lowestOneBit(x)));
        System.out.println("Lowest one bit in " + y + " : " + Integer.toBinaryString(Integer.lowestOneBit(y)));

        // rotate
        System.out.println("Left rotate " + x + " by 2 : " + Integer.toBinaryString(Integer.rotateLeft(x, 2)));
        System.out.println("Left rotate " + y + " by 2 : " + Integer.toBinaryString(Integer.rotateLeft(y, 2)));
        System.out.println("Right rotate " + x + " by 2 : " + Integer.toBinaryString(Integer.rotateRight(x, 2)));
        System.out.println("Right rotate " + y + " by 2 : " + Integer.toBinaryString(Integer.rotateRight(y, 2)));

        // reverse bits and bytes
        System.out.println("Reverse bits of " + x + " : " + Integer.toBinaryString(Integer.reverse(x)));
        System.out.println("Reverse bits of " + y + " : " + Integer.toBinaryString(Integer.reverse(y)));
        System.out.println("Reverse bytes of " + x + " : " + Integer.toBinaryString(Integer.reverseBytes(x)));
        System.out.println("Reverse bytes of " + y + " : " + Integer.toBinaryString(Integer.reverseBytes(y)));

        // right and unsigned right shift
        System.out.println("Right shift " + Integer.toBinaryString(Integer.MIN_VALUE) + " by 2 signed : " + Integer.toBinaryString(Integer.MIN_VALUE >> 2));
        System.out.println("Right shift " + Integer.toBinaryString(Integer.MIN_VALUE) + " by 2 unsigned: " + Integer.toBinaryString(Integer.MIN_VALUE >>> 2));

        // lowest set bit
        System.out.println("Lowest set bit of " + x + " : " + Integer.toBinaryString(Integer.lowestOneBit(x)));
        System.out.println("Lowest set bit of " + y + " : " + Integer.toBinaryString(Integer.lowestOneBit(y)));
        System.out.println("Lowest set bit using " + x + " & "+ Integer.toBinaryString(-x) + " : " + Integer.toBinaryString(isolateRightmostSetBit(x)));
        System.out.println("Lowest set bit using " + y + " & "+ Integer.toBinaryString(-y) + " : " + Integer.toBinaryString(isolateRightmostSetBit(y)));

        // -n = ~n + 1 (2's complement = 1's complement + 1)

    }

    private static int turnOffRightmostSetBit(int n){
        return n & (n-1);
    }
    
    private static int turnOnRightmostUnsetBit(int n){
        return n | (n+1);
    }

    private static int isolateRightmostSetBit(int n){
        return n & -n;
    }

    private static int getGrayCode(int n){
        return n ^ (n >> 1);
    }

    private static int getBinaryFromGray(int gray){
        int binary = 0;
        for(; gray != 0; gray = gray >> 1){
            binary ^= gray;
        }
        return binary;
    }

    private static int getSetBits(int n){
        int count = 0;
        while(n != 0){
            n = n & (n - 1);
            count++;
        }
        return count;
    }
}