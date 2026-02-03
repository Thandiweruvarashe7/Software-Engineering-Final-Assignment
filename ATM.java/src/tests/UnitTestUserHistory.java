package tests;
import model.User;

public class UnitTestUserHistory {
    public static void main(String[] args) {
        User user = new User("Test", "0000", 100.0, null);
        user.deposit(50.0);
        assert user.getBalance() == 150.0 : "Deposit failed";
        assert user.getHistory().get(0).contains("50.0") : "History missing";
        System.out.println("✅ UnitTestUserHistory PASSED");
    }
}