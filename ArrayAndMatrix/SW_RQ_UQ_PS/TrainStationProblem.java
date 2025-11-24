

public class TrainStationProblem {
    public static void main(String[] args) {
        int[][] trainsStations = {
            {0, 2},
            {1, 3},
            {2, 4},
            {1, 2},
            {0, 4}
        };
        int stations = 5; // Stations are numbered from 0 to 4
        int popularStation = mostPopularStation(trainsStations, stations);
    }

    private static int mostPopularStation(int[][] trainsStations, int stations) {
        int[] diff = new int[stations];
        for (int[] ts : trainsStations) {
            diff[ts[0]] += 1;
            if (ts[1] + 1 < stations) {
                diff[ts[1] + 1] -= 1;
            }
        }
        int index = 1, maxStationIndex = 0;
        while(index < stations){
            diff[index] += diff[index - 1];
            if(diff[index]>diff[maxStationIndex]){
                maxStationIndex = index;
            }
            index++;
        }
        System.out.println("Max trains at station: " + maxStationIndex + " with count: " + diff[maxStationIndex]);
        return maxStationIndex;
    }
}