package simon;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ScoreManager {
    private List<Integer> topScores = new ArrayList<>();

    public void addScore(int score) {
        topScores.add(score);
        Collections.sort(topScores, Collections.reverseOrder());
        if (topScores.size() > 10) topScores.remove(10);
    }

    public String getTopScoresString() {
        StringBuilder sb = new StringBuilder("Top 10 Scores:\n");
        for (int i = 0; i < topScores.size(); i++) {
            sb.append((i + 1)).append(". ").append(topScores.get(i)).append("\n");
        }
        return sb.toString();
    }
}
