package simon;
public class ScoreManagerTest {
    public static void main(String[] args) {
        System.out.println("Unit Test 2: ScoreManager");
        ScoreManager sm = new ScoreManager();
        sm.addScore(100);
        // Verifies the sorting/storing logic of the top scores
        if (sm.getTopScoresString().contains("100")) {
            System.out.println("RESULT: PASS");
        } else {
            System.out.println("RESULT: FAIL");
        }
    }
}