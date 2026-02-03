package simon;
public class PatternGeneratorTest {
    public static void main(String[] args) {
        System.out.println("Unit Test 1: PatternGenerator");
        PatternGenerator<Colour> gen = new PatternGenerator<>(Colour.values());
        gen.addStep();
        gen.addStep();
        // Checks if the generic list correctly stores 2 items
        if (gen.getPattern().size() == 2) {
            System.out.println("RESULT: PASS");
        } else {
            System.out.println("RESULT: FAIL");
        }
    }
}