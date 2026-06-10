/**
 * SavingsAccount - Child class of Account
 * Demonstrates: Inheritance, Method Overriding, Polymorphism,
 *               super keyword, Constructor chaining
 */
public class SavingsAccount extends Account {

    // Constants specific to SavingsAccount
    private static final double INTEREST_RATE   = 4.5;   // 4.5% per annum
    private static final double MINIMUM_BALANCE = 500.0; // Rs. 500 minimum

    private int withdrawalCount;         // track monthly withdrawals
    private static final int MAX_WITHDRAWALS = 5; // limit per month

    // ---- Constructors ----

    // Default Constructor
    public SavingsAccount() {
        super(); // calls Account()
        this.withdrawalCount = 0;
    }

    // Parameterized Constructor - calls parent using super()
    public SavingsAccount(String accountId, String customerId, double initialBalance) {
        super(accountId, customerId, initialBalance); // super() call
        this.withdrawalCount = 0;
    }

    // ---- Method Overriding (Polymorphism) ----

    @Override
    public String getAccountType() {
        return "SAVINGS";
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
     * Override withdraw to add SavingsAccount-specific rules:
     * - Cannot go below minimum balance
     * - Maximum 5 withdrawals per month
     */
    @Override
    public void withdraw(double amount) throws InvalidAmountException, InsufficientBalanceException {
        if (amount <= 0) {
            throw new InvalidAmountException("Withdrawal amount must be positive!", amount);
        }

        // Check withdrawal limit
        if (withdrawalCount >= MAX_WITHDRAWALS) {
            throw new InsufficientBalanceException(
                    "Monthly withdrawal limit of " + MAX_WITHDRAWALS + " reached!", getBalance(), amount);
        }

        // Check minimum balance rule
        if ((getBalance() - amount) < MINIMUM_BALANCE) {
            throw new InsufficientBalanceException(
                    "Cannot withdraw! Balance would go below minimum balance of Rs." + MINIMUM_BALANCE,
                    getBalance(), amount);
        }

        // Call parent withdraw method
        super.withdraw(amount);
        withdrawalCount++;
    }

    /**
     * Apply yearly interest to balance.
     * Overrides abstract method from Account.
     */
    @Override
    public void applyInterest() {
        double interest = getBalance() * INTEREST_RATE / 100;
        try {
            deposit(interest);
            System.out.println("✓ Interest of Rs. " + String.format("%.2f", interest) + " applied.");
        } catch (InvalidAmountException e) {
            System.out.println("Error applying interest: " + e.getMessage());
        }
    }

    // Reset monthly withdrawal counter (called monthly)
    public void resetWithdrawalCount() {
        this.withdrawalCount = 0;
    }

    public int getWithdrawalCount() {
        return withdrawalCount;
    }

    // Method Overloading - display with or without interest info
    public void displayAccountInfo() {
        super.displayAccountInfo(); // call parent method
        System.out.println("  Withdrawals  : " + withdrawalCount + "/" + MAX_WITHDRAWALS + " this month");
    }

    public void displayAccountInfo(boolean showInterestDetails) {
        displayAccountInfo();
        if (showInterestDetails) {
            double yearlyInterest = getBalance() * INTEREST_RATE / 100;
            System.out.println("  Yearly Int.  : Rs. " + String.format("%.2f", yearlyInterest));
        }
    }
}

