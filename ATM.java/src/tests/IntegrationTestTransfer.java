package tests;
import model.User;
import services.JsonStorage;

public class IntegrationTestTransfer {
    public static void main(String[] args) {
        JsonStorage storage = new JsonStorage();
        User sender = storage.authenticate("1002", "5678");
        storage.transfer(sender, "1001", 200.0);
        assert sender.getBalance() == 800.0 : "Transfer math failed";
        System.out.println("✅ IntegrationTestTransfer PASSED");
    }
}