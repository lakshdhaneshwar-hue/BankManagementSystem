/**
 * Custom Exception: InvalidAmountException
 * Thrown when user enters a negative or zero amount.
 * Demonstrates: Custom Exceptions, Exception Handling
 */
public class InvalidAmountException extends Exception {

    private double amount; // the invalid amount entered

    // Constructor with message
    public InvalidAmountException(String message) {
        super(message);
    }

    // Overloaded Constructor with message and amount
    public InvalidAmountException(String message, double amount) {
        super(message);
        this.amount = amount;
    }

    // Getter for amount
    public double getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "InvalidAmountException: " + getMessage() + " (Amount: " + amount + ")";
    }
}

