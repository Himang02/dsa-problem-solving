import java.util.function.ToIntFunction;

public class UnboundedKnapsackTest {

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
            new TestCase(new int[]{1, 2, 3, 2}, new int[]{10, 15, 40, 20}, 6,  80),
            new TestCase(new int[]{1, 2, 3, 4, 5}, new int[]{10, 15, 40, 30, 50}, 10, 130),
            new TestCase(new int[]{1, 2, 3}, new int[]{10, 20, 30}, 0,  0),
            new TestCase(new int[]{5}, new int[]{50}, 5,  50),
            new TestCase(new int[]{5, 6, 7}, new int[]{50, 60, 70}, 3,  0),
            new TestCase(new int[]{1, 3}, new int[]{10, 30}, 6,  60),
        };

        String[] methodNames = {"helper", "getMaxValueBottomUp", "getMaxValueBottomUpSpaceOptimized", "getMaxValueTopDownTryAll"};
        ToIntFunction<TestCase>[] methods = new ToIntFunction[]{
            (ToIntFunction<TestCase>) tc -> UnboundedKnapsack.helper(tc.weights(), tc.values(), tc.capacity()),
            (ToIntFunction<TestCase>) tc -> UnboundedKnapsack.getMaxValueBottomUp(tc.weights(), tc.values(), tc.capacity()),
            (ToIntFunction<TestCase>) tc -> UnboundedKnapsack.getMaxValueBottomUpSpaceOptimized(tc.weights(), tc.values(), tc.capacity()),
            (ToIntFunction<TestCase>) tc -> {
                int[] dp = new int[tc.capacity() + 1];
                java.util.Arrays.fill(dp, -1);
                return UnboundedKnapsack.getMaxValueTopDownTryAll(tc.capacity(), tc.weights(), tc.values(), dp);
            },
        };

        for (int m = 0; m < methods.length; m++) {
            for (TestCase tc : cases) {
                runCase(tc, methodNames[m], methods[m]);
            }
            System.out.println(methodNames[m] + ": all cases PASSED");
        }
    }

    // Individual tests (kept for direct invocation if needed)
    public void testHelperBasicCase() {
        int result = UnboundedKnapsack.helper(new int[]{1,2,3,2}, new int[]{10,15,40,20}, 6);
        assertEquals(80, result, "testHelperBasicCase");
        System.out.println("testHelperBasicCase: PASSED");
    }

    public void testHelperMultipleItems() {
        int result = UnboundedKnapsack.helper(new int[]{1,2,3,4,5}, new int[]{10,15,40,30,50}, 10);
        assertEquals(130, result, "testHelperMultipleItems");
        System.out.println("testHelperMultipleItems: PASSED");
    }

    public void testHelperZeroCapacity() {
        int result = UnboundedKnapsack.helper(new int[]{1,2,3}, new int[]{10,20,30}, 0);
        assertEquals(0, result, "testHelperZeroCapacity");
        System.out.println("testHelperZeroCapacity: PASSED");
    }

    public void testHelperSingleItem() {
        int result = UnboundedKnapsack.helper(new int[]{5}, new int[]{50}, 5);
        assertEquals(50, result, "testHelperSingleItem");
        System.out.println("testHelperSingleItem: PASSED");
    }

    public void testHelperCapacityTooSmall() {
        int result = UnboundedKnapsack.helper(new int[]{5,6,7}, new int[]{50,60,70}, 3);
        assertEquals(0, result, "testHelperCapacityTooSmall");
        System.out.println("testHelperCapacityTooSmall: PASSED");
    }

    public void testHelperLargeCapacity() {
        int result = UnboundedKnapsack.helper(new int[]{1,3}, new int[]{10,30}, 6);
        assertEquals(60, result, "testHelperLargeCapacity");
        System.out.println("testHelperLargeCapacity: PASSED");
    }
}
