/*
 * N passengers arrive at your transport service. The ith passenger arrives at time ti.
 * You have M buses at your service and each bus can hold C passengers.
 * A bus can depart with any number of passengers from 0 to C in it. 
 * It is guaranteed that you have sufficient buses to transport all the passengers to their destination i.e. M∗C≥N.
 * A bus can leave when the last passenger onboard the bus arrives. The other passengers have to wait for the last passenger to arrive to leave. 
 * The passengers don't like waiting. So your task is to calculate the smallest possible value of the maximum waiting time of the passengers if the bus departures are scheduled optimally.
 * Constraints: 1≤N≤105 && 1≤M≤105 && 0≤ti≤109 && 1≤C≤N
 * INPUT: The first line of the input contains three integers - N,M,C. The second line of the input contains N integers stating the arrival time of the N passengers.
 * OUTPUT: Output a single number which is the smallest possible value of the maximum waiting time.
*/

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.io.IOException;
import java.util.Arrays;


public class Transport {
  public static void main(String[] args) throws Exception{
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    StringTokenizer st = new StringTokenizer(br.readLine());
    
    int N = Integer.parseInt(st.nextToken());
    int M = Integer.parseInt(st.nextToken());
    int C = Integer.parseInt(st.nextToken());
    
    int[] times = new int[N];
    st = new StringTokenizer(br.readLine());
    
    for(int i = 0; i < N; i++){
      times[i] = Integer.parseInt(st.nextToken());
    }
    Arrays.sort(times);
    
    int L = -1, R = times[times.length-1] - times[0];
    while(R - L > 1){
      int mid = L + (R-L)/2;
      if(predicate(mid, times, M, C)){
        R = mid;
      }
      else{
        L = mid;
      }
    }
    System.out.println(R);
    br.close();

  }
  
  private static boolean predicate(int maxTime, int[] times, int buses, int busCapacity){
    int busCount = 1, passengerCount = 0, firstPassengerTime = times[0];
    int index = 0;
    while(index < times.length){
      if(passengerCount < busCapacity && times[index] - firstPassengerTime <= maxTime){
        passengerCount++;
        index++;
      }
      else{
        busCount++;
        passengerCount = 0;
        firstPassengerTime = times[index];
        if(busCount > buses){
          return false;
        }
      }
    }
    
    return true;
  }
}