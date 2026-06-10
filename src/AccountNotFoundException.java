/**
 * Custom Exception: AccountNotFoundException
 * Thrown when an account with the given ID is not found in the system.
 * Demonstrates: Custom Exceptions, toString() Override
 */
public class AccountNotFoundException extends Exception {

    private String accountId;

    // Constructor
    public AccountNotFoundException(String message) {
        super(message);
    }

    // Overloaded Constructor
    public AccountNotFoundException(String message, String accountId) {
        super(message);
        this.accountId = accountId;
    }

    public String getAccountId() {
        return accountId;
    }

    @Override
    public String toString() {
        return "AccountNotFoundException: " + getMessage() + " (Account ID: " + accountId + ")";
    }
}

