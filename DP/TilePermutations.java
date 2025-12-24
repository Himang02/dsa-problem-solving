
public class TilePermutations{
    public static void main(String[] args) {
        for(int n = 1; n<=10; n++){
            int arrangements = countTileArrangements(n);
            System.out.println("Number of ways to arrange tiles of length "+ n + " : " + arrangements);
        }
    }

    public static int countTileArrangements(int n) {
        // base case
        if(n <= 1){
            return 1;
        }
        else if(n == 2){
            return 2;
        }

        // transition
        return countTileArrangements(n-1) + countTileArrangements(n-2);  
    }
}