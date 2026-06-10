import java.time.LocalDate;
import java.util.Scanner;

/**
 * Main Class - Entry Point of Bank Management System
 * Menu-driven console application.
 * Demonstrates: Scanner, Loops, Conditional Statements, Exception Handling,
 *               all Java OOP and Core concepts in action.
 *
 * HOW TO COMPILE AND RUN:
 *   1. Open terminal in BankManagementSystem/src/
 *   2. Compile: javac *.java
 *   3. Run:     java Main
 */
public class Main {

    // Scanner for user input - static so all methods can use it
    private static Scanner scanner = new Scanner(System.in);
    private static Bank bank = new Bank("LAKSHYA National Bank");
    private static Customer loggedInCustomer = null;  // tracks logged-in user

    public static void main(String[] args) {
        printWelcomeBanner();

        boolean running = true;
        while (running) {
            if (loggedInCustomer == null) {
                running = showMainMenu();
            } else {
                running = showBankingMenu();
            }
        }

        System.out.println("\n  Thank you for using LAKSHYA National Bank. Goodbye!");
        scanner.close();
    }

    // ============================================================
    //  WELCOME BANNER
    // ============================================================
    private static void printWelcomeBanner() {
        System.out.println("\n" + "=".repeat(55));
        System.out.println("       LAKSHYA NATIONAL BANK - MANAGEMENT SYSTEM       ");
        System.out.println("           First Year Java OOP Project               ");
        System.out.println("=".repeat(55));
    }

    // ============================================================
    //  MAIN MENU (not logged in)
    // ============================================================
    private static boolean showMainMenu() {
        System.out.println("\n---------- MAIN MENU ----------");
        System.out.println("  1. Register New Customer");
        System.out.println("  2. Login");
        System.out.println("  3. Exit");
        System.out.print("  Enter choice: ");

        int choice = readInt();

        switch (choice) {
            case 1: registerCustomer(); break;
            case 2: loginCustomer();    break;
            case 3: return false;       // exit
            default: System.out.println("✗ Invalid option! Try again.");
        }
        return true;
    }

    // ============================================================
    //  BANKING MENU (logged in)
    // ============================================================
    private static boolean showBankingMenu() {
        System.out.println("\n---------- BANKING MENU ----------");
        System.out.println("  Welcome: " + loggedInCustomer.getFullName());
        System.out.println("  1.  Create Savings Account");
        System.out.println("  2.  Create Current Account");
        System.out.println("  3.  Deposit Money");
        System.out.println("  4.  Withdraw Money");
        System.out.println("  5.  Transfer Money");
        System.out.println("  6.  Check Balance");
        System.out.println("  7.  Transaction History");
        System.out.println("  8.  View My Accounts");
        System.out.println("  9.  View My Profile");
        System.out.println("  10. Apply Interest (All Savings)");
        System.out.println("  11. Close an Account");
        System.out.println("  12. Logout");
        System.out.println("  0.  Exit");
        System.out.print("  Enter choice: ");

        int choice = readInt();

        switch (choice) {
            case 1:  createSavingsAccount();  break;
            case 2:  createCurrentAccount();  break;
            case 3:  depositMoney();          break;
            case 4:  withdrawMoney();         break;
            case 5:  transferMoney();         break;
            case 6:  checkBalance();          break;
            case 7:  viewHistory();           break;
            case 8:  bank.showCustomerAccounts(loggedInCustomer.getCustomerId()); break;
            case 9:  loggedInCustomer.displayCustomerInfo(); break;
            case 10: bank.applyInterestToAll(); break;
            case 11: closeAccount();          break;
            case 12: loggedInCustomer = null;
                     System.out.println("✓ Logged out successfully."); break;
            case 0:  return false;
            default: System.out.println("✗ Invalid option! Try again.");
        }
        return true;
    }

    // ============================================================
    //  FEATURE METHODS
    // ============================================================

    private static void registerCustomer() {
        System.out.println("\n----- REGISTER NEW CUSTOMER -----");
        System.out.print("  First Name    : ");
        String firstName = scanner.nextLine().trim();

        System.out.print("  Last Name     : ");
        String lastName = scanner.nextLine().trim();

        System.out.print("  Email         : ");
        String email = scanner.nextLine().trim();

        System.out.print("  Phone Number  : ");
        String phone = scanner.nextLine().trim();

        System.out.print("  Address       : ");
        String address = scanner.nextLine().trim();

        System.out.print("  Password      : ");
        String password = scanner.nextLine().trim();

        System.out.print("  Date of Birth (YYYY-MM-DD): ");
        String dobStr = scanner.nextLine().trim();

        try {
            LocalDate dob = LocalDate.parse(dobStr);
            Customer c = bank.registerCustomer(firstName, lastName, email,
                    phone, address, password, dob);
            System.out.println("\n✓ Registration successful!");
            System.out.println("  Your Customer ID: " + c.getCustomerId());
            System.out.println("  Please save your Customer ID for login.");
        } catch (Exception e) {
            System.out.println("✗ Registration failed: " + e.getMessage());
        }
    }

    private static void loginCustomer() {
        System.out.println("\n----- LOGIN -----");
        System.out.print("  Customer ID : ");
        String customerId = scanner.nextLine().trim();
        System.out.print("  Password    : ");
        String password = scanner.nextLine().trim();

        try {
            Customer c = bank.login(customerId, password);
            if (c != null) {
                loggedInCustomer = c;
            }
        } catch (AccountNotFoundException e) {
            System.out.println("✗ " + e.getMessage());
        }
    }

    private static void createSavingsAccount() {
        System.out.println("\n----- CREATE SAVINGS ACCOUNT -----");
        System.out.print("  Initial Deposit (Min Rs.500): Rs.");
        double amount = readDouble();

        try {
            SavingsAccount acc = bank.createSavingsAccount(
                    loggedInCustomer.getCustomerId(), amount);
            acc.displayAccountInfo();
        } catch (InvalidAmountException | AccountNotFoundException e) {
            System.out.println("✗ " + e.getMessage());
        }
    }

    private static void createCurrentAccount() {
        System.out.println("\n----- CREATE CURRENT ACCOUNT -----");
        System.out.print("  Initial Deposit (Min Rs.1000): Rs.");
        double amount = readDouble();

        try {
            CurrentAccount acc = bank.createCurrentAccount(
                    loggedInCustomer.getCustomerId(), amount);
            acc.displayAccountInfo();
        } catch (InvalidAmountException | AccountNotFoundException e) {
            System.out.println("✗ " + e.getMessage());
        }
    }

    private static void depositMoney() {
        System.out.println("\n----- DEPOSIT MONEY -----");
        System.out.print("  Account ID : ");
        String accountId = scanner.nextLine().trim();
        System.out.print("  Amount     : Rs.");
        double amount = readDouble();

        try {
            bank.deposit(accountId, amount);
        } catch (AccountNotFoundException | InvalidAmountException e) {
            System.out.println("✗ " + e.getMessage());
        }
    }

    private static void withdrawMoney() {
        System.out.println("\n----- WITHDRAW MONEY -----");
        System.out.print("  Account ID : ");
        String accountId = scanner.nextLine().trim();
        System.out.print("  Amount     : Rs.");
        double amount = readDouble();

        try {
            bank.withdraw(accountId, amount);
        } catch (AccountNotFoundException | InvalidAmountException | InsufficientBalanceException e) {
            System.out.println("✗ " + e.getMessage());
        }
    }

    private static void transferMoney() {
        System.out.println("\n----- TRANSFER MONEY -----");
        System.out.print("  From Account ID : ");
        String fromId = scanner.nextLine().trim();
        System.out.print("  To Account ID   : ");
        String toId = scanner.nextLine().trim();
        System.out.print("  Amount          : Rs.");
        double amount = readDouble();

        try {
            bank.transfer(fromId, toId, amount);
        } catch (AccountNotFoundException | InvalidAmountException | InsufficientBalanceException e) {
            System.out.println("✗ " + e.getMessage());
        }
    }

    private static void checkBalance() {
        System.out.println("\n----- CHECK BALANCE -----");
        System.out.print("  Account ID : ");
        String accountId = scanner.nextLine().trim();

        try {
            bank.checkBalance(accountId);
        } catch (AccountNotFoundException e) {
            System.out.println("✗ " + e.getMessage());
        }
    }

    private static void viewHistory() {
        System.out.println("\n----- TRANSACTION HISTORY -----");
        System.out.print("  Account ID : ");
        String accountId = scanner.nextLine().trim();

        try {
            bank.showTransactionHistory(accountId);
        } catch (AccountNotFoundException e) {
            System.out.println("✗ " + e.getMessage());
        }
    }

    private static void closeAccount() {
        System.out.println("\n----- CLOSE ACCOUNT -----");
        System.out.print("  Account ID to close : ");
        String accountId = scanner.nextLine().trim();
        System.out.print("  Are you sure? (yes/no): ");
        String confirm = scanner.nextLine().trim();

        if (confirm.equalsIgnoreCase("yes")) {
            try {
                bank.closeAccount(accountId);
            } catch (AccountNotFoundException e) {
                System.out.println("✗ " + e.getMessage());
            }
        } else {
            System.out.println("  Account closure cancelled.");
        }
    }

    // ============================================================
    //  INPUT HELPER METHODS
    // ============================================================

    // Read an integer safely
    private static int readInt() {
        try {
            String line = scanner.nextLine().trim();
            return Integer.parseInt(line);      // Wrapper class: Integer.parseInt
        } catch (NumberFormatException e) {
            return -1; // invalid input returns -1
        }
    }

    // Read a double safely
    private static double readDouble() {
        try {
            String line = scanner.nextLine().trim();
            return Double.parseDouble(line);    // Wrapper class: Double.parseDouble
        } catch (NumberFormatException e) {
            System.out.println("✗ Invalid amount. Using 0.");
            return 0;
        }
    }
}

