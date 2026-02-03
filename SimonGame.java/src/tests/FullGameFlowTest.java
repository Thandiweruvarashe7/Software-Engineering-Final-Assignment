package simon;
public class FullGameFlowTest {
    public static void main(String[] args) {
        System.out.println("Integration Test 2: Flow between Logic and Scoring");
        PatternGenerator<Colour> logic = new PatternGenerator<>(Colour.values());
        ScoreManager sm = new ScoreManager();

        logic.addStep();
        logic.addStep();
        // Integration: Transfers data from the game logic to the score storage
        int score = logic.getPattern().size();
        sm.addScore(score);

        if (sm.getTopScoresString().contains(String.valueOf(score))) {
            System.out.println("RESULT: PASS");
        } else {
            System.out.println("RESULT: FAIL");
        }
    }
}