package tests;
import model.ATMState;
import java.util.Map;

public class UnitTestGreedyLogic {
    public static void main(String[] args) {
        // Testing the allocation logic independently
        ATMState atm = new ATMState(10, 10, 10, 10, 100, 100, "1.0.0");
        Map<Integer, Integer> res = atm.simulateAllocationForAmount(180);
        assert res.get(100) == 1 && res.get(50) == 1 : "Greedy logic failed";
        System.out.println("✅ UnitTestGreedyLogic PASSED");
    }
}