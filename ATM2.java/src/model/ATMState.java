package model;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

public class ATMState {

    // Denomination counts
    private int c100;
    private int c50;
    private int c20;
    private int c10;

    private int paper;
    private int ink;
    private String firmware;

    // Construct from denomination counts
    public ATMState(int c100, int c50, int c20, int c10, int paper, int ink, String firmware) {
        this.c100 = c100;
        this.c50 = c50;
        this.c20 = c20;
        this.c10 = c10;
        this.paper = paper;
        this.ink = ink;
        this.firmware = firmware;
    }

    // Backwards-compatible constructor: distribute cash into notes (greedy)
    public ATMState(double cash, int paper, String firmware) {
        int remaining = (int) cash;
        this.c100 = remaining / 100; remaining %= 100;
        this.c50 = remaining / 50; remaining %= 50;
        this.c20 = remaining / 20; remaining %= 20;
        this.c10 = remaining / 10; remaining %= 10;
        this.paper = paper;
        this.ink = 10; // default ink
        this.firmware = firmware;
    }

    public double getCashAmount() {
        return c100 * 100 + c50 * 50 + c20 * 20 + c10 * 10;
    }

    public Map<Integer, Integer> getNoteCounts() {
        Map<Integer, Integer> m = new LinkedHashMap<>();
        m.put(100, c100);
        m.put(50, c50);
        m.put(20, c20);
        m.put(10, c10);
        return m;
    }

    public int getCountFor(int denom) {
        if (denom == 100) {
            return c100;
        } else if (denom == 50) {
            return c50;
        } else if (denom == 20) {
            return c20;
        } else if (denom == 10) {
            return c10;
        } else {
            return 0;
        }
    }

    public void addNotes(int denom, int count) {
        if (denom == 100) {
            c100 += count;
        } else if (denom == 50) {
            c50 += count;
        } else if (denom == 20) {
            c20 += count;
        } else if (denom == 10) {
            c10 += count;
        }
    }

    // Try to remove the given notes (requested map), return false if any not available
    public boolean removeRequestedNotes(Map<Integer, Integer> requested) {
        if (requested == null) return false;
        // check availability
        for (Map.Entry<Integer, Integer> e : requested.entrySet()) {
            int denom = e.getKey();
            int cnt = e.getValue();
            if (cnt < 0) return false;
            if (getCountFor(denom) < cnt) return false;
        }
        // apply
        for (Map.Entry<Integer, Integer> e : requested.entrySet()) {
            int denom = e.getKey();
            int cnt = e.getValue();
            if (denom == 100) {
                c100 -= cnt;
            } else if (denom == 50) {
                c50 -= cnt;
            } else if (denom == 20) {
                c20 -= cnt;
            } else if (denom == 10) {
                c10 -= cnt;
            }
        }
        return true;
    }

    // Greedy allocation for a requested amount without mutating state; returns map of notes or null if impossible
    public Map<Integer, Integer> simulateAllocationForAmount(int amount) {
        int remaining = amount;
        Map<Integer, Integer> result = new LinkedHashMap<>();

        int use100 = Math.min(remaining / 100, c100);
        remaining -= use100 * 100; result.put(100, use100);

        int use50 = Math.min(remaining / 50, c50);
        remaining -= use50 * 50; result.put(50, use50);

        int use20 = Math.min(remaining / 20, c20);
        remaining -= use20 * 20; result.put(20, use20);

        int use10 = Math.min(remaining / 10, c10);
        remaining -= use10 * 10; result.put(10, use10);

        if (remaining != 0) return null; // cannot make exact amount

        return result;
    }

    // Find up to `limit` different allocations (combinations of notes) that make `amount` using available counts
    public List<Map<Integer, Integer>> findAllocationsForAmount(int amount, int limit) {
        List<Map<Integer,Integer>> results = new ArrayList<>();
        int[] denoms = new int[]{100,50,20,10};
        findAllocationsRecursive(amount, 0, denoms, new LinkedHashMap<>(), results, limit);
        return results;
    }

    // Recursive helper method to find different combinations of notes
    // This method uses recursion to try different numbers of each denomination
    private void findAllocationsRecursive(int remaining, int idx, int[] denoms, Map<Integer,Integer> current, List<Map<Integer,Integer>> results, int limit) {
        if (results.size() >= limit) return; // Stop if we have enough results
        if (remaining == 0) { // If we have made the exact amount
            // Ensure all denominations are in the map with 0 if not used
            Map<Integer,Integer> copy = new LinkedHashMap<>();
            for (int d : denoms) copy.put(d, current.getOrDefault(d, 0));
            results.add(copy); // Add this combination to results
            return;
        }
        if (idx >= denoms.length) return; // No more denominations to try

        int denom = denoms[idx];
        int maxAvailable = Math.min(remaining / denom, getCountFor(denom)); // Max notes we can use
        for (int cnt = 0; cnt <= maxAvailable; cnt++) { // Try 0 to max notes
            current.put(denom, cnt);
            findAllocationsRecursive(remaining - cnt * denom, idx + 1, denoms, current, results, limit);
            if (results.size() >= limit) return;
        }
        current.remove(denom); // Backtrack
    }

    // Greedy allocation that removes notes and returns the removed notes map
    public Map<Integer, Integer> allocateNotesForAmount(int amount) {
        Map<Integer,Integer> allocation = simulateAllocationForAmount(amount);
        if (allocation == null) return null;
        removeRequestedNotes(allocation);
        return allocation;
    }

    // For tests that call removeCash(amount)
    public boolean removeCash(double amount) {
        Map<Integer, Integer> allocation = allocateNotesForAmount((int) amount);
        return allocation != null;
    }

    public int getPaperAmount() {
        return paper;
    }

    public void setPaperAmount(int paper) {
        this.paper = paper;
    }

    public int getInkAmount() {
        return ink;
    }

    public void setInkAmount(int ink) {
        this.ink = ink;
    }

    public void useInk(int amount) {
        this.ink = Math.max(0, this.ink - amount);
    }

    public String getFirmware() {
        return firmware;
    }

    // Backwards compatibility
    public double getCash() { return getCashAmount(); }
    public void setCash(double c) { /* no-op; use addNotes instead */ }
    public int getPaper() { return getPaperAmount(); }
} 

