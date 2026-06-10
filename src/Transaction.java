import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Transaction Class
 * Represents a single banking transaction.
 * Demonstrates: Encapsulation, Serialization, Enum, Date/Time API
 */
public class Transaction implements Serializable {

    // Serial version for serialization
    private static final long serialVersionUID = 1L;

    // Enum for transaction types - demonstrates Enum concept
    public enum TransactionType {
        DEPOSIT, WITHDRAWAL, TRANSFER_IN, TRANSFER_OUT, ACCOUNT_CREATED
    }

    // Private fields - Encapsulation
    private String transactionId;
    private String accountId;
    private TransactionType type;
    private double amount;
    private double balanceAfter;
    private String description;
    private LocalDateTime dateTime;

    // Static counter for generating unique IDs
    private static int counter = 1000;

    // Constructor
    public Transaction(String accountId, TransactionType type, double amount,
                       double balanceAfter, String description) {
        this.transactionId = "TXN" + (++counter);
        this.accountId     = accountId;
        this.type          = type;
        this.amount        = amount;
        this.balanceAfter  = balanceAfter;
        this.description   = description;
        this.dateTime      = LocalDateTime.now(); // Date/Time API
    }

    // Constructor used when loading from file
    public Transaction(String transactionId, String accountId, TransactionType type,
                       double amount, double balanceAfter, String description, LocalDateTime dateTime) {
        this.transactionId = transactionId;
        this.accountId     = accountId;
        this.type          = type;
        this.amount        = amount;
        this.balanceAfter  = balanceAfter;
        this.description   = description;
        this.dateTime      = dateTime;
    }

    // ---- Getters (Encapsulation) ----
    public String getTransactionId() { return transactionId; }
    public String getAccountId()     { return accountId; }
    public TransactionType getType() { return type; }
    public double getAmount()        { return amount; }
    public double getBalanceAfter()  { return balanceAfter; }
    public String getDescription()   { return description; }
    public LocalDateTime getDateTime() { return dateTime; }

    // Format date for display
    public String getFormattedDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return dateTime.format(formatter);
    }

    // Convert to CSV line for file storage
    public String toCsvString() {
        return transactionId + "," + accountId + "," + type + "," +
               amount + "," + balanceAfter + "," + description + "," + dateTime;
    }

    // Display transaction as formatted string
    @Override
    public String toString() {
        return String.format("| %-12s | %-15s | %-12s | %10.2f | %10.2f | %s",
                transactionId, accountId, type, amount, balanceAfter, getFormattedDate());
    }
}

