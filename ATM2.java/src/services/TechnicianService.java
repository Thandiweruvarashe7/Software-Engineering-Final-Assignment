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

        while (true) {
            ATMState state = atmRepo.loadATM();
            System.out.println("\n--- Maintenance ---");
            System.out.println("Firmware: " + state.getFirmware());
            System.out.println("Paper: " + state.getPaperAmount() + " | Ink: " + state.getInkAmount());
            System.out.println("1. Add Cash\n2. Add Paper\n3. Add Ink\n4. Update Firmware\n0. Exit");
            System.out.print("Choice: ");
            int c = sc.nextInt();
            if (c == 0) break;

            switch (c) {
                case 1 -> { 
                    System.out.print("Amount (Euro): "); 
                    state.addNotes(20, sc.nextInt() / 20); 
                }
                case 2 -> { 
                    System.out.print("Units: "); 
                    state.setPaperAmount(state.getPaperAmount() + sc.nextInt()); 
                }
                case 3 -> { 
                    System.out.print("Units: "); 
                    state.setInkAmount(state.getInkAmount() + sc.nextInt()); 
                }
                case 4 -> { 
                    System.out.print("Version: "); 
                    state = new ATMState((int)state.getCashAmount(), state.getPaperAmount(), sc.next()); 
                }
            }
            atmRepo.saveATM(state);
            System.out.println("Saved successfully");
        }
    }
}