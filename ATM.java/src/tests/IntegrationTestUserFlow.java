package tests;
import interfaces.*;
import model.*;
import services.*;

public class IntegrationTestUserFlow {
    public static void main(String[] args) {
        JsonStorage storage = new JsonStorage();
        ATMService service = new ATMServiceImpl(storage);
        User user = storage.authenticate("1001", "1234");
        ATMState atm = storage.loadATM();
        double startBal = user.getBalance();
        service.withdraw(user, atm, 100);
        assert user.getBalance() == startBal - 100 : "Withdrawal Failed";
        System.out.println("✅ IntegrationTestUserFlow PASSED");
    }
}