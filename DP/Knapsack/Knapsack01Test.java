import java.util.function.ToIntFunction;

public class Knapsack01Test {

    private record TestCase(int[] weights, int[] values, int capacity, int expected) {}

    private static void assertEquals(int expected, int actual, String label) {
        if (expected != actual)
            throw new AssertionError(label + " — expected " + expected + " but got " + actual);
    }

    private void runCase(TestCase tc, String methodName, ToIntFunction<TestCase> method) {
        int result = method.applyAsInt(tc);
        assertEquals(tc.expected, result, methodName + "(cap=" + tc.capacity + ", expected=" + tc.expected + ")");
    }

    public void testAll() {
        TestCase[] cases = {
            // take all items: 10+15+40 = 65
            new TestCase(new int[]{1, 2, 3},    new int[]{10, 15, 40},       6,  65),
            // best: items {2,3} weight=5, value=60
            new TestCase(new int[]{1, 2, 3, 2}, new int[]{10, 15, 40, 20},   5,  60),
            // best: items {1,2,4} weight=10, value=105
            new TestCase(new int[]{1, 2, 3, 4, 5}, new int[]{10, 15, 40, 30, 50}, 10, 105),
            // zero capacity
            new TestCase(new int[]{1, 2, 3},    new int[]{10, 20, 30},       0,   0),
            // single item fits exactly
            new TestCase(new int[]{5},           new int[]{50},               5,  50),
            // no item fits
            new TestCase(new int[]{5, 6, 7},    new int[]{50, 60, 70},       3,   0),
        };

        String[] methodNames = {"helper", "getMaxValueBottomUp", "getMaxValueBottomUpSpaceOptimized"};
        ToIntFunction<TestCase>[] methods = new ToIntFunction[]{
            (ToIntFunction<TestCase>) tc -> Knapsack01.helper(tc.weights(), tc.values(), tc.capacity()),
            (ToIntFunction<TestCase>) tc -> Knapsack01.getMaxValueBottomUp(tc.weights(), tc.values(), tc.capacity()),
            (ToIntFunction<TestCase>) tc -> Knapsack01.getMaxValueBottomUpSpaceOptimized(tc.weights(), tc.values(), tc.capacity()),
        };

        for (int m = 0; m < methods.length; m++) {
            for (TestCase tc : cases) {
                runCase(tc, methodNames[m], methods[m]);
            }
            System.out.println(methodNames[m] + ": all cases PASSED");
        }
    }
}
