/**
 * Custom Exception: InsufficientBalanceException
 * Thrown when account does not have enough balance for withdrawal/transfer.
 * Demonstrates: Custom Exceptions, Inheritance from Exception
 */
public class InsufficientBalanceException extends Exception {

    private double currentBalance;
    private double requestedAmount;

    // Constructor
    public InsufficientBalanceException(String message) {
        super(message);
    }

    // Overloaded Constructor
    public InsufficientBalanceException(String message, double currentBalance, double requestedAmount) {
        super(message);
        this.currentBalance = currentBalance;
        this.requestedAmount = requestedAmount;
    }

    public double getCurrentBalance() {
        return currentBalance;
    }

    public double getRequestedAmount() {
        return requestedAmount;
    }

    @Override
    public String toString() {
        return "InsufficientBalanceException: " + getMessage()
                + " | Balance: " + currentBalance
                + " | Requested: " + requestedAmount;
    }
}

