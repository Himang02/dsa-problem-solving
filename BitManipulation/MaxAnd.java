import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.List;
import java.util.ArrayList;

public class MaxAnd {
    public static void main(String[] args) throws Exception {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(st.nextToken());
        List<Integer> totalList = new ArrayList<>();
        st = new StringTokenizer(br.readLine());
        for(int i = 0; i < n; i++){
            totalList.add(Integer.parseInt(st.nextToken()));
        }

        System.out.println(approach2(totalList));
        System.out.println(approach1(totalList));


    }


    // TC: O(32*N), SC: O(1) -- while iterating we are mainting a mask (with current maxAnd mask) to filter out. 
    // Hence extra list is not required.
    private static int approach2(List<Integer> totalList) {
        int maxAnd = 0;

        for(int bit = 30; bit >= 0; bit--){
            int currentMask = (maxAnd | (1 << bit));
            int count  = 0;
            for(int num: totalList){
                if((num & currentMask) == currentMask){
                    count++;
                }
                if(count>=2){
                    break;
                }
            }
            if(count >=2){
                maxAnd = currentMask;
            }
        }
        return maxAnd;

    }


    // TC: O(32*N), SC: O(N)
    private static int approach1(List<Integer> totalList) {
        int currentBit = 30, mask = 1<<currentBit;
        // System.out.println("starting..");
        while(totalList.size() > 2 && mask > 0){
            // System.out.println("total set: " + totalSet.size() + ", currentBit: " + currentBit);
            List<Integer> newList = new ArrayList();
            for(int num: totalList){
            if((mask & num) > 0){
                newList.add(num);
            }
            }
            currentBit--;
            mask = mask>>>1;
            if(newList.size() < 2){
            continue;
            }
            // System.out.println("newSet size:" + newSet.size());
            totalList = newList;
            
        }

        if(totalList.size()>=2){
            int op = 0;
            
            for(int i =0; i< totalList.size(); i++){
                for(int j = i + 1; j<totalList.size(); j++){
                    op = Math.max(op, totalList.get(i) & totalList.get(j));
                }
            }
            return op;
            
        }
        else{
            return 0;
        }
    }
}