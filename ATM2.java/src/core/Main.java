package core;

import interfaces.*;
import services.*;
import model.*;
import java.util.Scanner;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        JsonStorage storage = new JsonStorage();
        ATMService atmService = new ATMServiceImpl(storage);
        TechnicianService technicianService = new TechnicianService(storage);
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n*******************************");
            System.out.println("* Welcome to the ATM          *");
            System.out.println("*******************************");
            System.out.println("1. Customer Login");
            System.out.println("2. Technician Login");
            System.out.println("0. Exit");
            System.out.print("Choice: ");

            String choice = sc.next();
            switch (choice) {
                case "1" -> customerLogin(sc, atmService, storage, storage);
                case "2" -> technicianService.start();
                case "0" -> System.exit(0);
            }
        }
    }

    private static void customerLogin(Scanner sc, ATMService atmService, UserRepository userRepo, ATMRepository atmRepo) {
        System.out.print("Account: ");
        String acc = sc.next();
        System.out.print("PIN: ");
        User user = userRepo.authenticate(acc, sc.next());
        if (user == null) {
            System.out.println("Login failed.");
            return;
        }
        customerMenu(sc, atmService, userRepo, atmRepo, user);
    }

    private static void customerMenu(Scanner sc, ATMService atmService, UserRepository userRepo, ATMRepository atmRepo, User user) {
        boolean active = true;
        while (active) {
            System.out.println("\n--- Welcome, " + user.getAccount() + " ---");
            System.out.println("1. Balance\n2. Withdraw\n3. Deposit\n4. Transfer\n5. Transaction History\n6. Logout");
            System.out.print("Choice: ");
            int c = sc.nextInt();
            switch (c) {
                case 1 -> System.out.println("Balance: " + user.getBalance() + " Euro");
                case 2 -> withdrawAction(sc, atmService, atmRepo, userRepo, user);
                case 3 -> depositAction(sc, atmService, userRepo, user);
                case 4 -> transferAction(sc, userRepo, atmService, user);
                case 5 -> showHistory(user);
                case 6 -> active = false;
            }
            if (active && c != 6) {
                System.out.print("\nAnother action? (yes/no): ");
                if (sc.next().equalsIgnoreCase("no")) active = false;
            }
        }
    }

    private static void showHistory(User user) {
        System.out.println("\n--- Transaction History ---");
        List<String> history = user.getHistory();
        if (history.isEmpty()) {
            System.out.println("No transactions found.");
        } else {
            for (String record : history) {
                System.out.println("- " + record);
            }
        }
    }

    private static void transferAction(Scanner sc, UserRepository userRepo, ATMService atmService, User user) {
        System.out.print("Enter recipient account: ");
        String recipient = sc.next();
        System.out.print("Enter amount (Euro): ");
        double amount = sc.nextDouble();

        if (user.getBalance() >= amount) {
            try {
                userRepo.transfer(user, recipient, amount);
                System.out.println("Transfer done successfully");
                offerReceipt(sc, atmService);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("Insufficient funds.");
        }
    }

    private static void withdrawAction(Scanner sc, ATMService atmService, ATMRepository atmRepo, UserRepository userRepo, User user) {
        System.out.print("Amount (Euro): ");
        int amount = sc.nextInt();
        if (atmService.withdraw(user, atmRepo.loadATM(), amount)) {
            userRepo.updateUser(user);
            System.out.println("Success! New balance: " + user.getBalance() + " Euro");
            offerReceipt(sc, atmService);
        } else {
            System.out.println("Withdrawal failed.");
        }
    }

    private static void depositAction(Scanner sc, ATMService atmService, UserRepository userRepo, User user) {
        System.out.print("Amount (Euro): ");
        double amount = sc.nextDouble();
        user.deposit(amount);
        userRepo.updateUser(user);
        System.out.println("Deposit successful! New balance: " + user.getBalance() + " Euro");
        offerReceipt(sc, atmService);
    }

    private static void offerReceipt(Scanner sc, ATMService atmService) {
        System.out.print("Would you like a receipt? (yes/no): ");
        if (sc.next().equalsIgnoreCase("yes")) {
            if (atmService.printReceipt()) System.out.println("Receipt printed.");
            else System.out.println("Error: No paper/ink.");
        }
    }
}