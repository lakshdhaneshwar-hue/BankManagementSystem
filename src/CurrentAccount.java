/**
 * CurrentAccount - Child class of Account
 * Designed for businesses - allows overdraft facility.
 * Demonstrates: Inheritance, Method Overriding, Polymorphism, super keyword
 */
public class CurrentAccount extends Account {

    private static final double INTEREST_RATE    = 0.0;    // No interest on current accounts
    private static final double MINIMUM_BALANCE  = 1000.0; // Rs. 1000 minimum
    private static final double OVERDRAFT_LIMIT  = 5000.0; // Rs. 5000 overdraft allowed

    private double overdraftLimit;

    // ---- Constructors ----

    public CurrentAccount() {
        super();
        this.overdraftLimit = OVERDRAFT_LIMIT;
    }

    public CurrentAccount(String accountId, String customerId, double initialBalance) {
        super(accountId, customerId, initialBalance);
        this.overdraftLimit = OVERDRAFT_LIMIT;
    }

    // Constructor with custom overdraft
    public CurrentAccount(String accountId, String customerId,
                          double initialBalance, double overdraftLimit) {
        super(accountId, customerId, initialBalance);
        this.overdraftLimit = overdraftLimit;
    }

    // ---- Method Overriding ----

    @Override
    public String getAccountType() {
        return "CURRENT";
    }

    @Override
    public double getInterestRate() {
        return INTEREST_RATE;
    }

    @Override
    public double getMinimumBalance() {
        return MINIMUM_BALANCE;
    }

    /**
     * Override withdraw to allow overdraft.
     * CurrentAccount can go below 0 up to overdraftLimit.
     */
    @Override
    public void withdraw(double amount) throws InvalidAmountException, InsufficientBalanceException {
        if (amount <= 0) {
            throw new InvalidAmountException("Withdrawal amount must be positive!", amount);
        }

        // Total available = balance + overdraft
        double totalAvailable = getBalance() + overdraftLimit;

        if (amount > totalAvailable) {
            throw new InsufficientBalanceException(
                    "Amount exceeds available balance + overdraft limit!",
                    getBalance(), amount);
        }

        // Use parent setBalance since we allow negative balance up to overdraftLimit
        setBalance(getBalance() - amount);

        // Record the transaction manually
        Transaction txn = new Transaction(getAccountId(),
                Transaction.TransactionType.WITHDRAWAL, amount, getBalance(), "Withdrawal");
        getTransactions().add(txn);

        System.out.println("✓ Withdrawn Rs. " + amount + " successfully.");
        System.out.println("  New Balance: Rs. " + String.format("%.2f", getBalance()));

        if (getBalance() < 0) {
            System.out.println("  ⚠ You are using overdraft facility. Overdraft used: Rs. "
                    + String.format("%.2f", Math.abs(getBalance())));
        }
    }

    /**
     * No interest for current accounts.
     */
    @Override
    public void applyInterest() {
        System.out.println("ℹ Current accounts do not earn interest.");
    }

    // ---- Getters/Setters ----
    public double getOverdraftLimit()              { return overdraftLimit; }
    public void setOverdraftLimit(double limit)    { this.overdraftLimit = limit; }

    // Method Overloading
    @Override
    public void displayAccountInfo() {
        super.displayAccountInfo();
        System.out.println("  Overdraft    : Rs. " + overdraftLimit);
        System.out.println("  Available    : Rs. " + String.format("%.2f", getBalance() + overdraftLimit));
    }

    public void displayAccountInfo(boolean showOverdraft) {
        displayAccountInfo();
        if (showOverdraft && getBalance() < 0) {
            System.out.println("  ⚠ Overdraft Used: Rs. " + Math.abs(getBalance()));
        }
    }
}

