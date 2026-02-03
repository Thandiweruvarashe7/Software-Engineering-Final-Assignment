package simon;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PatternGenerator<T> {
    private List<T> pattern = new ArrayList<>();
    private T[] options;
    private Random random = new Random();

    public PatternGenerator(T[] options) {
        this.options = options;
    }

    public void addStep() {
        pattern.add(options[random.nextInt(options.length)]);
    }

    public List<T> getPattern() {
        return new ArrayList<>(pattern);
    }

    public void clear() {
        pattern.clear();
    }
}

