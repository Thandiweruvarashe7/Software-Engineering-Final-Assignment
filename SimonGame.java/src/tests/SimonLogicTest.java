package simon;
import java.util.List;
public class SimonLogicTest {
    public static void main(String[] args) {
        System.out.println("Integration Test 1: Generator + Enum Coordination");
        PatternGenerator<Colour> logic = new PatternGenerator<>(Colour.values());
        logic.addStep();
        List<Colour> pattern = logic.getPattern();
        // Integration: Validates that the Generator correctly pulls from the Colour Enum
        if (pattern.get(0) instanceof Colour) {
            System.out.println("RESULT: PASS");
        } else {
            System.out.println("RESULT: FAIL");
        }
    }
}