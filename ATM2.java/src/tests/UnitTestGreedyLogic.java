package tests;

import model.ATMState;
import java.util.Map;

public class UnitTestGreedyLogic {
    public static void main(String[] args) {
        System.out.println("=== Unit Test: Greedy Note Allocation ===");

        // Setup: ATM with specifically 10 notes of each type
        ATMState atm = new ATMState(10, 10, 10, 10, 100, 100, "2.0.0");
        
        // Action: Request 180 Euro
        Map<Integer, Integer> allocation = atm.simulateAllocationForAmount(180);

        // Assert: 180 should be 1x100, 1x50, 1x20, 1x10
        assert allocation != null : "Allocation simulation failed";
        assert allocation.get(100) == 1 : "Should have used one 100 Euro note";
        assert allocation.get(50) == 1 : "Should have used one 50 Euro note";
        assert allocation.get(20) == 1 : "Should have used one 20 Euro note";
        assert allocation.get(10) == 1 : "Should have used one 10 Euro note";

        System.out.println("UnitTestGreedyLogic PASSED");
    }
}
