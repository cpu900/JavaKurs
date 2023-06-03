package TestBank;

import java.util.Scanner;
import alebia2.BankLogic;

public class TestMenu {

    private BankLogic bank = new BankLogic();
    private Scanner scanner = new Scanner(System.in);

    public void start() {
        while (true) {
            System.out.println("\nBanking System Menu:");
            System.out.println("1. Create Customer");
            System.out.println("2. Change Customer Name");
            System.out.println("3. Create Savings Account");
            System.out.println("4. Deposit");
            System.out.println("5. Withdraw");
            System.out.println("6. Close Account");
            System.out.println("7. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    createCustomer();
                    break;
                case 2:
                    changeName();
                    break;
                case 3:
                    createSavingsAccount();
                    break;
                case 4:
                    // deposit();
                    break;
                case 5:
                    // withdraw();
                    break;
                case 6:
                    // closeAccount();
                    break;
                case 7:
                    System.out.println("Exiting the system...");
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private void createCustomer() {
        System.out.print("Enter customer name: ");
        String name = scanner.next();
        System.out.print("Enter customer surname: ");
        String surname = scanner.next();
        System.out.print("Enter personal number: ");
        String pNo = scanner.next();

        boolean result = bank.createCustomer(name, surname, pNo);
        if (result) {
            System.out.println("Customer created successfully.");
        } else {
            System.out.println("Failed to create customer.");
        }
    }

    private void changeName() {
        System.out.print("Enter customer name: ");
        String name = scanner.next();
        System.out.print("Enter customer surname: ");
        String surname = scanner.next();
        System.out.print("Enter personal number: ");
        String pNo = scanner.next();

        boolean result = bank.changeCustomerName(name, surname, pNo);
        if (result) {
            System.out.println("Customer name changed successfully.");
        } else {
            System.out.println("Failed to change customer name.");
        }
    }

    private void createSavingsAccount() {
        System.out.print("Enter personal number: ");
        String pNo = scanner.next();

        int result = bank.createSavingsAccount(pNo);
        if (result != -1) {
            System.out.println("Savings account created with account number: " + result);
        } else {
            System.out.println("Failed to create savings account.");
        }
    }



}
