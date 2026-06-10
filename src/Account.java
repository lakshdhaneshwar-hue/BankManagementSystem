import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Abstract Class: Account
 * Base class for all account types.
 * Demonstrates: Abstraction, Encapsulation, Abstract Methods,
 *               Inheritance (parent), Serialization, ArrayList, Date API
 */
public abstract class Account implements Serializable {

    private static final long serialVersionUID = 1L;

    // ---- Private Fields (Encapsulation) ----
    private String accountId;
    private String customerId;
    private double balance;
    private LocalDate openingDate;
    private boolean isActive;

    // ArrayList to store transactions - Collections Framework
    private ArrayList<Transaction> transactions;

    // ---- Constructors (Constructor concept) ----

    // Default Constructor
    public Account() {
        this.transactions = new ArrayList<>();
        this.isActive     = true;
        this.openingDate  = LocalDate.now();
    }

    // Parameterized Constructor
    public Account(String accountId, String customerId, double initialBalance) {
        this();                           // calls default constructor
        this.accountId  = accountId;
        this.customerId = customerId;
        this.balance    = initialBalance;
    }

    // ---- Abstract Methods (Abstraction) ----
    // Every subclass MUST implement these methods
    public abstract String getAccountType();
    public abstract double getInterestRate();
    public abstract double getMinimumBalance();
    public abstract void applyInterest();

    // ---- Concrete Methods ----

    /**
     * Deposit money into account.
     * Demonstrates: Exception Handling
     */
    public void deposit(double amount) throws InvalidAmountException {
        // Validate amount
        if (amount <= 0) {
            throw new InvalidAmountException("Deposit amount must be positive!", amount);
        }
        this.balance += amount;
        // Record transaction
        Transaction txn = new Transaction(accountId, Transaction.TransactionType.DEPOSIT,
                amount, balance, "Deposit");
        transactions.add(txn);
        System.out.println("✓ Deposited Rs. " + amount + " successfully.");
        System.out.println("  New Balance: Rs. " + balance);
    }

    /**
     * Withdraw money from account.
     * Demonstrates: Exception Handling, Method Overriding (subclasses can override)
     */
    public void withdraw(double amount) throws InvalidAmountException, InsufficientBalanceException {
        if (amount <= 0) {
            throw new InvalidAmountException("Withdrawal amount must be positive!", amount);
        }
        if (amount > balance) {
            throw new InsufficientBalanceException(
                    "Insufficient balance for withdrawal!", balance, amount);
        }
        this.balance -= amount;
        Transaction txn = new Transaction(accountId, Transaction.TransactionType.WITHDRAWAL,
                amount, balance, "Withdrawal");
        transactions.add(txn);
        System.out.println("✓ Withdrawn Rs. " + amount + " successfully.");
        System.out.println("  New Balance: Rs. " + balance);
    }

    /**
     * Transfer money to another account.
     */
    public void transferOut(double amount, String toAccountId)
            throws InvalidAmountException, InsufficientBalanceException {
        if (amount <= 0) {
            throw new InvalidAmountException("Transfer amount must be positive!", amount);
        }
        if (amount > balance) {
            throw new InsufficientBalanceException(
                    "Insufficient balance for transfer!", balance, amount);
        }
        this.balance -= amount;
        Transaction txn = new Transaction(accountId, Transaction.TransactionType.TRANSFER_OUT,
                amount, balance, "Transfer to " + toAccountId);
        transactions.add(txn);
    }

    /**
     * Receive transferred money.
     */
    public void transferIn(double amount, String fromAccountId) throws InvalidAmountException {
        if (amount <= 0) {
            throw new InvalidAmountException("Transfer amount must be positive!", amount);
        }
        this.balance += amount;
        Transaction txn = new Transaction(accountId, Transaction.TransactionType.TRANSFER_IN,
                amount, balance, "Transfer from " + fromAccountId);
        transactions.add(txn);
    }

    // ---- Getters and Setters (Encapsulation) ----
    public String getAccountId()            { return accountId; }
    public void setAccountId(String id)     { this.accountId = id; }

    public String getCustomerId()           { return customerId; }
    public void setCustomerId(String id)    { this.customerId = id; }

    public double getBalance()              { return balance; }
    protected void setBalance(double bal)   { this.balance = bal; } // protected - only subclasses

    public LocalDate getOpeningDate()       { return openingDate; }
    public void setOpeningDate(LocalDate d) { this.openingDate = d; }

    public boolean isActive()               { return isActive; }
    public void setActive(boolean active)   { this.isActive = active; }

    public ArrayList<Transaction> getTransactions() { return transactions; }
    public void setTransactions(ArrayList<Transaction> txns) { this.transactions = txns; }

    // ---- Display Account Info ----
    public void displayAccountInfo() {
        System.out.println("\n========== ACCOUNT DETAILS ==========");
        System.out.println("  Account ID   : " + accountId);
        System.out.println("  Account Type : " + getAccountType());      // polymorphism
        System.out.println("  Customer ID  : " + customerId);
        System.out.println("  Balance      : Rs. " + String.format("%.2f", balance));
        System.out.println("  Opening Date : " + openingDate);
        System.out.println("  Status       : " + (isActive ? "Active" : "Closed"));
        System.out.println("  Interest     : " + getInterestRate() + "% p.a.");
        System.out.println("  Min. Balance : Rs. " + getMinimumBalance());
        System.out.println("=====================================");
    }

    // Convert to CSV for file storage
    public String toCsvString() {
        return accountId + "," + customerId + "," + getAccountType() + ","
                + balance + "," + openingDate + "," + isActive;
    }

    @Override
    public String toString() {
        return "Account[" + accountId + " | " + getAccountType() + " | Rs." + balance + "]";
    }
}

