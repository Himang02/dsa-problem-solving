import java.util.*;


/* You are asked to find the solution to the famous problem of Towers of Hanoi. There are 3 towers 1,2,3 Towers 2 and 3 
 are empty while the tower 1 has n disks on it. The size of disk i is i. 
 There is a special rule that has to be followed at all times: 
 - A disk cannot be placed above a disk of smaller size on the towers i.e. if disk i is above disk j, then disk j∈{i+1,i+2,…,n}. 
 - You can move the disks around from one tower to another. Your task is to move all the disks from tower 1
 to tower 2. You can use tower 3 to place the disks temporarily. (1≤n≤15)
 NOTE: It is recommended to use fast input-output methods.

 INPUT
 The only line of input contains a single integer n.

 OUTPUT
 Let k be the minimum number of moves required to move all the disks from tower 1 to tower 2. The first line of the output should 
 contain this number k. The next k lines should describe your moves. For each move output three numbers a,b,c
 which indicates that you moved the disk a from tower b to tower c. (1≤a≤n,1≤b,c≤3).

*/


// state: no of disks, start, end, aux

public class TowerOfHanoi {
    public static void main(String[] args) {
        System.out.println("Steps: " + solveTowerOfHanoi(4, 1, 2, 3)); // Example with 3 disks)
    }

    private static int solveTowerOfHanoi(int n, int start, int end, int aux) {
        // base condition
        if (n == 1) {
            System.out.println("Move disk 1 from tower " + start + " to tower " + end);
            return 1;
        }

        // transition
        int steps = 0;
        steps += solveTowerOfHanoi(n - 1, start, aux, end); // move n-1 disks from start to aux
        System.out.println("Move disk " + n + " from tower " + start + " to tower " + end);
        steps += 1; // move nth disk from start to end
        steps += solveTowerOfHanoi(n - 1, aux, end, start); // move n-1 disks from aux to end

        return steps;
        
    }
}