import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * FileManager Class
 * Handles all file read/write operations.
 * Demonstrates: File Handling, BufferedReader/Writer, FileReader/Writer,
 *               Exception Handling, Serialization
 */
public class FileManager {

    // File paths (relative to data/ folder)
    private static final String DATA_DIR          = "data/";
    private static final String ACCOUNTS_FILE     = DATA_DIR + "accounts.txt";
    private static final String CUSTOMERS_FILE    = DATA_DIR + "customers.txt";
    private static final String TRANSACTIONS_FILE = DATA_DIR + "transactions.txt";

    // ---- Constructor: ensures data directory exists ----
    public FileManager() {
        createDataDirectory();
    }

    // Create data directory if it doesn't exist
    private void createDataDirectory() {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
            System.out.println("✓ Data directory created.");
        }
    }

    // ============================================================
    //  CUSTOMER FILE OPERATIONS
    // ============================================================

    /**
     * Save a customer to customers.txt
     * Uses FileWriter + BufferedWriter
     */
    public void saveCustomer(Customer customer) {
        try (FileWriter fw = new FileWriter(CUSTOMERS_FILE, true);         // true = append mode
             BufferedWriter bw = new BufferedWriter(fw)) {

            bw.write(customer.toCsvString());
            bw.newLine();
            System.out.println("✓ Customer saved to file.");

        } catch (IOException e) {
            System.out.println("✗ Error saving customer: " + e.getMessage());
        }
    }

    /**
     * Load all customers from customers.txt
     * Uses FileReader + BufferedReader
     */
    public ArrayList<Customer> loadAllCustomers() {
        ArrayList<Customer> customers = new ArrayList<>();
        File file = new File(CUSTOMERS_FILE);

        if (!file.exists()) return customers; // return empty list if file doesn't exist

        try (FileReader fr = new FileReader(file);
             BufferedReader br = new BufferedReader(fr)) {

            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                Customer c = parseCustomerFromCsv(line);
                if (c != null) customers.add(c);
            }

        } catch (IOException e) {
            System.out.println("✗ Error loading customers: " + e.getMessage());
        }

        return customers;
    }

    // Parse a CSV line into a Customer object
    private Customer parseCustomerFromCsv(String csvLine) {
        try {
            String[] parts = csvLine.split(",", -1);
            // customerId,firstName,lastName,email,phone,address,password,dob,regDate
            String customerId       = parts[0];
            String firstName        = parts[1];
            String lastName         = parts[2];
            String email            = parts[3];
            String phone            = parts[4];
            String address          = parts[5];
            String password         = parts[6];
            LocalDate dob           = LocalDate.parse(parts[7]);
            LocalDate regDate       = LocalDate.parse(parts[8]);

            return new Customer(customerId, firstName, lastName, email,
                    phone, address, password, dob, regDate);
        } catch (Exception e) {
            System.out.println("✗ Error parsing customer: " + e.getMessage());
            return null;
        }
    }

    /**
     * Update all customers (overwrite file)
     */
    public void saveAllCustomers(ArrayList<Customer> customers) {
        try (FileWriter fw = new FileWriter(CUSTOMERS_FILE, false); // false = overwrite
             BufferedWriter bw = new BufferedWriter(fw)) {

            for (Customer c : customers) {
                bw.write(c.toCsvString());
                bw.newLine();
            }

        } catch (IOException e) {
            System.out.println("✗ Error saving customers: " + e.getMessage());
        }
    }

    // ============================================================
    //  ACCOUNT FILE OPERATIONS
    // ============================================================

    /**
     * Save an account to accounts.txt
     */
    public void saveAccount(Account account) {
        try (FileWriter fw = new FileWriter(ACCOUNTS_FILE, true);
             BufferedWriter bw = new BufferedWriter(fw)) {

            bw.write(account.toCsvString());
            bw.newLine();
            System.out.println("✓ Account saved to file.");

        } catch (IOException e) {
            System.out.println("✗ Error saving account: " + e.getMessage());
        }
    }

    /**
     * Load all accounts from accounts.txt
     * Returns ArrayList of Account objects (SavingsAccount / CurrentAccount)
     */
    public ArrayList<Account> loadAllAccounts() {
        ArrayList<Account> accounts = new ArrayList<>();
        File file = new File(ACCOUNTS_FILE);

        if (!file.exists()) return accounts;

        try (FileReader fr = new FileReader(file);
             BufferedReader br = new BufferedReader(fr)) {

            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                Account a = parseAccountFromCsv(line);
                if (a != null) accounts.add(a);
            }

        } catch (IOException e) {
            System.out.println("✗ Error loading accounts: " + e.getMessage());
        }

        return accounts;
    }

    private Account parseAccountFromCsv(String csvLine) {
        try {
            String[] parts = csvLine.split(",", -1);
            // accountId,customerId,type,balance,openingDate,isActive
            String   accountId   = parts[0];
            String   customerId  = parts[1];
            String   type        = parts[2];
            double   balance     = Double.parseDouble(parts[3]);   // Wrapper class: Double.parseDouble
            LocalDate openDate   = LocalDate.parse(parts[4]);
            boolean  isActive    = Boolean.parseBoolean(parts[5]); // Wrapper class: Boolean.parseBoolean

            Account account;
            if (type.equals("SAVINGS")) {
                account = new SavingsAccount(accountId, customerId, balance);
            } else {
                account = new CurrentAccount(accountId, customerId, balance);
            }
            account.setOpeningDate(openDate);
            account.setActive(isActive);
            return account;

        } catch (Exception e) {
            System.out.println("✗ Error parsing account: " + e.getMessage());
            return null;
        }
    }

    /**
     * Overwrite all accounts to file
     */
    public void saveAllAccounts(ArrayList<Account> accounts) {
        try (FileWriter fw = new FileWriter(ACCOUNTS_FILE, false);
             BufferedWriter bw = new BufferedWriter(fw)) {

            for (Account a : accounts) {
                bw.write(a.toCsvString());
                bw.newLine();
            }

        } catch (IOException e) {
            System.out.println("✗ Error saving accounts: " + e.getMessage());
        }
    }

    // ============================================================
    //  TRANSACTION FILE OPERATIONS
    // ============================================================

    /**
     * Append a transaction to transactions.txt
     */
    public void saveTransaction(Transaction txn) {
        try (FileWriter fw = new FileWriter(TRANSACTIONS_FILE, true);
             BufferedWriter bw = new BufferedWriter(fw)) {

            bw.write(txn.toCsvString());
            bw.newLine();

        } catch (IOException e) {
            System.out.println("✗ Error saving transaction: " + e.getMessage());
        }
    }

    /**
     * Load all transactions for a specific account
     */
    public ArrayList<Transaction> loadTransactionsForAccount(String accountId) {
        ArrayList<Transaction> transactions = new ArrayList<>();
        File file = new File(TRANSACTIONS_FILE);

        if (!file.exists()) return transactions;

        try (FileReader fr = new FileReader(file);
             BufferedReader br = new BufferedReader(fr)) {

            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                Transaction txn = parseTransactionFromCsv(line);
                if (txn != null && txn.getAccountId().equals(accountId)) {
                    transactions.add(txn);
                }
            }

        } catch (IOException e) {
            System.out.println("✗ Error loading transactions: " + e.getMessage());
        }

        return transactions;
    }

    private Transaction parseTransactionFromCsv(String csvLine) {
        try {
            String[] parts = csvLine.split(",", -1);
            // txnId,accountId,type,amount,balanceAfter,description,dateTime
            String   txnId        = parts[0];
            String   accountId    = parts[1];
            Transaction.TransactionType type =
                    Transaction.TransactionType.valueOf(parts[2]);
            double   amount       = Double.parseDouble(parts[3]);
            double   balAfter     = Double.parseDouble(parts[4]);
            String   description  = parts[5];
            LocalDateTime dateTime = LocalDateTime.parse(parts[6]);

            return new Transaction(txnId, accountId, type, amount, balAfter, description, dateTime);

        } catch (Exception e) {
            System.out.println("✗ Error parsing transaction: " + e.getMessage());
            return null;
        }
    }

    // ============================================================
    //  SERIALIZATION (Binary File) - Bonus Feature
    // ============================================================

    /**
     * Serialize a Customer object to a binary file.
     * Demonstrates: Java Serialization (ObjectOutputStream)
     */
    public void serializeCustomer(Customer customer) {
        String fileName = DATA_DIR + customer.getCustomerId() + ".ser";
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(fileName))) {

            oos.writeObject(customer);
            System.out.println("✓ Customer serialized to " + fileName);

        } catch (IOException e) {
            System.out.println("✗ Serialization error: " + e.getMessage());
        }
    }

    /**
     * Deserialize a Customer object from binary file.
     * Demonstrates: Java Deserialization (ObjectInputStream)
     */
    public Customer deserializeCustomer(String customerId) {
        String fileName = DATA_DIR + customerId + ".ser";
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(fileName))) {

            Customer c = (Customer) ois.readObject();
            System.out.println("✓ Customer deserialized from " + fileName);
            return c;

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("✗ Deserialization error: " + e.getMessage());
            return null;
        }
    }
}

