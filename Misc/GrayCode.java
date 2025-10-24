import java.util.*;

public class GrayCode{
    public static void main(String[] args) {
        List<List<Integer>> hammingNeighbors = new ArrayList<>();
        for(int i = 0; i < 256; i++){
            hammingNeighbors.add(getAllNumbersWithHammingDistanceOne(i, 8));
        }
        for(int i = 0; i < hammingNeighbors.size(); i++){
            System.out.println(i + "> " + hammingNeighbors.get(i));
        }
        System.out.println();

        int[] possibleMapping = new int[256];
        Arrays.fill(possibleMapping, -1);
        
        System.out.println("Total : " + getPossibleCombinations(8, hammingNeighbors, 0, possibleMapping, 0));
        
    }

    public static List<Integer> grayCode(int n) {
        List<Integer> result = new ArrayList<>();
        int totalNumbers = 1 << n; // 2^n
        for (int i = 0; i < totalNumbers; i++) {
            int grayCodeNumber = i ^ (i >> 1);
            result.add(grayCodeNumber);
        }
        return result;
    }

    public static List<Integer> getAllNumbersWithHammingDistanceOne(int num, int n) {
        List<Integer> result = new ArrayList<>();
        // result.add(num); // Include the original number
        for (int i = 0; i < n; i++) {
            int mask = 1 << i;
            int newNumber = num ^ mask; // Toggle the i-th bit
            result.add(newNumber);
        }
        return result;
    }

    public static int getPossibleCombinations(int totalBits, List<List<Integer>> hammingNeighbors, int mapIndex, int[] possibleMapping, int count){ 
        // base condition
        if(mapIndex == possibleMapping.length){
            // print binary value of i and decimal value of possibleMapping[i]
            
            if(count == 0){
                // for(int i = 0; i < possibleMapping.length; i++){
                //     // System.out.println(String.format("%" + totalBits + "s", Integer.toBinaryString(i)).replace(' ', '0') + " : " + possibleMapping[i]);
                //     // System.out.println("");
                // }
                System.out.println(Arrays.toString(possibleMapping));
            } 
            // System.out.println();
            return count + 1;
        }

        // transition
        for(int alias = 0; alias < totalBits; alias++){
            if(isValid(alias, hammingNeighbors, possibleMapping, mapIndex)){
                possibleMapping[mapIndex] = alias;
                // System.out.println(mapIndex + " : " + Arrays.toString(possibleMapping));
                // if(mapIndex + 1 < hammingNeighbors.size()){
                    count += getPossibleCombinations(totalBits, hammingNeighbors, mapIndex + 1, possibleMapping, count);
                // }
                possibleMapping[mapIndex] = -1;
            }
        }

        return count;
 
        

    }

    private static boolean isValid(int alias, List<List<Integer>> hammingNeighbors, int[] possibleMapping, int listIndex){
        boolean onePassed = false;
        for(int neighbor : hammingNeighbors.get(listIndex)){
            if(possibleMapping[neighbor] == alias){
                if(onePassed){
                    // System.out.println("false because of neighbor: " + neighbor + " for alias: " + alias + " at index: " + listIndex);
                    return false;
                }
                else{
                    onePassed = true;
                }
                
                
            }
        }
        
        for(int neighbor : hammingNeighbors.get(listIndex)){
            for(int nextNeighbor : hammingNeighbors.get(neighbor)){
                if(possibleMapping[nextNeighbor] == alias){
                    // System.out.println("false because of nextNeighbor(of " + neighbor + "): " + nextNeighbor + " for alias: " + alias + " at index: " + listIndex);
                    return false;
                }
            }
        }
        
        
        return true;
    }
}