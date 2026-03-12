import java.util.function.ToIntFunction;

public class BoundedKnapsackTest {

    private record TestCase(int[] weights, int[] values, int[] counts, int capacity, int expected) {}

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
            // counts=1 each → reduces to 0/1 knapsack: take all = 65
            new TestCase(new int[]{1, 2, 3},    new int[]{10, 15, 40}, new int[]{1, 1, 1},    6,  65),

            // counts allow repetition: 2×item1(w=6,v=60) beats 3×item0(w=6,v=30)
            new TestCase(new int[]{2, 3},       new int[]{10, 30},     new int[]{3, 2},        6,  60),

            // counts limit repetition: item2(w=3,v=40) available only once,
            // best: 2×item0 + 1×item2 + 1×item4 = w(2+3+5=10), v(20+40+50=110)
            new TestCase(new int[]{1, 2, 3, 4, 5}, new int[]{10, 15, 40, 30, 50},
                         new int[]{2, 3, 1, 2, 1}, 10, 110),

            // counts=1 same as 0/1: best subset {2,3} weight=5, value=60
            new TestCase(new int[]{1, 2, 3, 2}, new int[]{10, 15, 40, 20}, new int[]{1, 1, 1, 1}, 5, 60),

            // zero capacity → 0
            new TestCase(new int[]{1, 2, 3},    new int[]{10, 20, 30}, new int[]{2, 2, 2},    0,   0),

            // single item, multiple copies: floor(10/3)=3 copies, value=30
            new TestCase(new int[]{3},           new int[]{10},         new int[]{4},          10,  30),

            // all counts=0 → 0
            new TestCase(new int[]{1, 2, 3},    new int[]{10, 20, 30}, new int[]{0, 0, 0},    6,   0),

            // capacity too small for any item → 0
            new TestCase(new int[]{5, 6, 7},    new int[]{50, 60, 70}, new int[]{3, 3, 3},    4,   0),
        };

        String[] methodNames = {"getMaxValueTopDownHelper", "getMaxValueTopDownTryIncludeCountsHelper", "getMaxValueByConvertingTo01Helper", "getMaxValueBottomUp"};
        ToIntFunction<TestCase>[] methods = new ToIntFunction[]{
            (ToIntFunction<TestCase>) tc -> BoundedKnapsack.getMaxValueTopDownHelper(tc.weights(), tc.values(), tc.counts(), tc.capacity()),
            (ToIntFunction<TestCase>) tc -> BoundedKnapsack.getMaxValueTopDownTryIncludeCountsHelper(tc.weights(), tc.values(), tc.counts(), tc.capacity()),
            (ToIntFunction<TestCase>) tc -> BoundedKnapsack.getMaxValueByConvertingTo01Helper(tc.capacity(), tc.weights(), tc.values(), tc.counts()),
            (ToIntFunction<TestCase>) tc -> BoundedKnapsack.getMaxValueBottomUp(tc.capacity(), tc.weights(), tc.values(), tc.counts()),
        };

        for (int m = 0; m < methods.length; m++) {
            for (TestCase tc : cases) {
                runCase(tc, methodNames[m], methods[m]);
            }
            System.out.println(methodNames[m] + ": all cases PASSED");
        }
    }
}
