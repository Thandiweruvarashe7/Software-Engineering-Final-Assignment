package services;

import model.ATMState;
import interfaces.ATMRepository;
import java.util.Scanner;

public class TechnicianService {
    private ATMRepository atmRepo;
    private Scanner sc = new Scanner(System.in);

    public TechnicianService(ATMRepository atmRepo) {
        this.atmRepo = atmRepo;
    }

    public void start() {
        System.out.print("Technician PIN: ");
        if (!sc.next().equals("9999")) {
            System.out.println("Access Denied.");
            return;
        }

        ATMState state = atmRepo.loadATM();
        System.out.println("\n--- ATM Diagnostic View (Read-Only) ---");
        System.out.println("Firmware: " + state.getFirmware());
        System.out.println("Paper Level: " + state.getPaperAmount());
        System.out.println("Ink Level: " + state.getInkAmount());
        System.out.println("Cash Level: " + state.getCashAmount());
        System.out.println("---------------------------------------");
        System.out.println("Status: System Operational.");
        System.out.println("Press any key to exit diagnostics...");
        sc.next();
    }
}