package simon;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class SimonGUI extends JFrame {
    private PatternGenerator<Colour> logic = new PatternGenerator<>(Colour.values());
    private ScoreManager scoreManager = new ScoreManager();
    private SoundPlayer sound = new SoundPlayer();
    private Map<Colour, JButton> buttonMap = new HashMap<>();
    private int playerIndex = 0;
    private boolean isPlayerTurn = false;

    public SimonGUI() {
        setTitle("3D Simon Game");
        setSize(500, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel gamePanel = new JPanel(new GridLayout(2, 2, 10, 10));
        gamePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        for (Colour c : Colour.values()) {
            JButton btn = new JButton();
            btn.setBackground(c.dim);
            btn.setOpaque(true);
            btn.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
            btn.addActionListener(e -> handlePress(c));
            buttonMap.put(c, btn);
            gamePanel.add(btn);
        }

        JButton startBtn = new JButton("START GAME");
        startBtn.addActionListener(e -> startGame());

        add(gamePanel, BorderLayout.CENTER);
        add(startBtn, BorderLayout.SOUTH);
    }

    private void startGame() {
        logic.clear();
        nextRound();
    }

    private void nextRound() {
        playerIndex = 0;
        isPlayerTurn = false;
        logic.addStep();
        new Thread(this::playPattern).start();
    }

    private void playPattern() {
        for (Colour c : logic.getPattern()) {
            flash(c);
            try { Thread.sleep(600); } catch (InterruptedException ignored) {}
        }
        isPlayerTurn = true;
    }

    private void flash(Colour c) {
        JButton b = buttonMap.get(c);
        b.setBackground(c.bright);
        b.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        sound.playTone(400 + (c.ordinal() * 100));
        try { Thread.sleep(300); } catch (InterruptedException ignored) {}
        b.setBackground(c.dim);
        b.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
    }

    private void handlePress(Colour c) {
        if (!isPlayerTurn) return;
        new Thread(() -> flash(c)).start();
        if (c == logic.getPattern().get(playerIndex)) {
            playerIndex++;
            if (playerIndex == logic.getPattern().size()) nextRound();
        } else {
            scoreManager.addScore(logic.getPattern().size() - 1);
            JOptionPane.showMessageDialog(this, "Game Over!\n" + scoreManager.getTopScoresString());
            isPlayerTurn = false;
        }
    }
}

