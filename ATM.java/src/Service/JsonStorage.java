package services;

import model.*;
import interfaces.ATMRepository;
import interfaces.UserRepository;
import java.io.*;
import java.util.*;

public class JsonStorage implements ATMRepository, UserRepository {
    private static final String DATA_FILE = "data/data.json";
    private Map<String, User> memoryUsers = new HashMap<>();
    private ATMState memoryATM;

    public JsonStorage() {
        File dir = new File("data");
        if (!dir.exists()) dir.mkdirs();

        // Initializing with "Presentation Ready" defaults
        memoryATM = new ATMState(10, 10, 10, 10, 100, 100, "2.0.0");
        memoryUsers.put("1001", new User("1001", "1234", 500.0, new ArrayList<>()));
        memoryUsers.put("1002", new User("1002", "5678", 1000.0, new ArrayList<>()));

        loadFromFile(); // Try to load, but memory stays safe if it fails
    }

    @Override
    public User authenticate(String acc, String pin) {
        User u = memoryUsers.get(acc);
        if (u != null && u.getPin().equals(pin)) return u;
        return null;
    }

    @Override
    public void updateUser(User user) {
        memoryUsers.put(user.getAccount(), user);
        saveToFile();
    }

    @Override
    public void transfer(User from, String toAccount, double amount) {
        User to = memoryUsers.get(toAccount);
        if (to == null) throw new IllegalArgumentException("Recipient not found");
        from.transfer(amount, toAccount);
        to.receive(amount, from.getAccount());
        updateUser(from);
        updateUser(to);
    }

    @Override
    public ATMState loadATM() {
        return memoryATM;
    }

    @Override
    public void saveATM(ATMState atm) {
        this.memoryATM = atm;
        saveToFile();
    }

    private synchronized void saveToFile() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(DATA_FILE))) {
            pw.println("{");
            pw.println("  \"paper\":" + memoryATM.getPaperAmount() + ",");
            pw.println("  \"ink\":" + memoryATM.getInkAmount() + ",");
            pw.println("  \"users\":[");
            int i = 0;
            for (User u : memoryUsers.values()) {
                pw.print("    {\"account\":\"" + u.getAccount() + "\", \"pin\":\"" + u.getPin() + "\", \"balance\":" + u.getBalance() + "}");
                if (++i < memoryUsers.size()) pw.print(",");
                pw.println();
            }
            pw.println("  ]");
            pw.println("}");
        } catch (Exception e) { /* Silent fail to prevent crash */ }
    }

    private void loadFromFile() {
        File f = new File(DATA_FILE);
        if (!f.exists()) return;
        try {
            String content = readFile();
            // Simple robust extraction to avoid NumberFormatException
            int p = safeParse(extractValue(content, "paper"), 100);
            int k = safeParse(extractValue(content, "ink"), 100);
            memoryATM = new ATMState(10, 10, 10, 10, p, k, "2.0.0");
        } catch (Exception e) { /* Keeps defaults if file is messy */ }
    }

    private String readFile() throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(DATA_FILE))) {
            String line;
            while ((line = br.readLine()) != null) sb.append(line);
        }
        return sb.toString();
    }

    private String extractValue(String json, String key) {
        try {
            String pattern = "\"" + key + "\":";
            int start = json.indexOf(pattern) + pattern.length();
            int end = json.indexOf(",", start);
            if (end == -1) end = json.indexOf("}", start);
            return json.substring(start, end).replaceAll("[^0-9.]", "").trim();
        } catch (Exception e) { return "0"; }
    }

    private int safeParse(String val, int def) {
        try { return (int) Double.parseDouble(val); } catch (Exception e) { return def; }
    }
}
