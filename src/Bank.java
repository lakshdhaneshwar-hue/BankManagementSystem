import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Bank Class - Core Business Logic
 * Manages all accounts and customers.
 * Demonstrates: Collections (HashMap, ArrayList, HashSet),
 *               Generics, Lambda Expressions, Streams API,
 *               Multithreading (simple interest calculator thread)
 */
public class Bank {

    private String bankName;

    // HashMap: accountId -> Account object  (Collections Framework + Generics)
    private HashMap<String, Account> accounts;

    // HashMap: customerId -> Customer object
    private HashMap<String, Customer> customers;

    // HashSet: stores all used account IDs (no duplicates)
    private HashSet<String> usedAccountIds;

    // FileManager for persistence
    private FileManager fileManager;

    // Counter for generating account IDs
    private static int accountCounter = 1000;

    // ---- Constructor ----
    public Bank(String bankName) {
        this.bankName       = bankName;
        this.accounts       = new HashMap<>();
        this.customers      = new HashMap<>();
        this.usedAccountIds = new HashSet<>();
        this.fileManager    = new FileManager();

        loadDataFromFiles(); // Load existing data on startup
        System.out.println("✓ " + bankName + " system initialized.");
    }

    // ============================================================
    //  ACCOUNT MANAGEMENT
    // ============================================================

    /**
     * Create a new Savings Account
     */
    public SavingsAccount createSavingsAccount(String customerId, double initialDeposit)
            throws InvalidAmountException, AccountNotFoundException {

        if (!customers.containsKey(customerId)) {
            throw new AccountNotFoundException("Customer not found!", customerId);
        }
        if (initialDeposit < SavingsAccount.class.getName().length()) {
            // just a placeholder check; real minimum is 500
        }
        if (initialDeposit < 500) {
            throw new InvalidAmountException("Minimum initial deposit for Savings is Rs.500!", initialDeposit);
        }

        String accountId = generateAccountId("SAV");
        SavingsAccount account = new SavingsAccount(accountId, customerId, initialDeposit);

        // Record opening transaction
        Transaction openTxn = new Transaction(accountId,
                Transaction.TransactionType.ACCOUNT_CREATED,
                initialDeposit, initialDeposit, "Account Opened");
        account.getTransactions().add(openTxn);
        fileManager.saveTransaction(openTxn);

        accounts.put(accountId, account);
        usedAccountIds.add(accountId);
        fileManager.saveAccount(account);

        System.out.println("✓ Savings Account created: " + accountId);
        return account;
    }

    /**
     * Create a new Current Account
     */
    public CurrentAccount createCurrentAccount(String customerId, double initialDeposit)
            throws InvalidAmountException, AccountNotFoundException {

        if (!customers.containsKey(customerId)) {
            throw new AccountNotFoundException("Customer not found!", customerId);
        }
        if (initialDeposit < 1000) {
            throw new InvalidAmountException("Minimum initial deposit for Current is Rs.1000!", initialDeposit);
        }

        String accountId = generateAccountId("CUR");
        CurrentAccount account = new CurrentAccount(accountId, customerId, initialDeposit);

        Transaction openTxn = new Transaction(accountId,
                Transaction.TransactionType.ACCOUNT_CREATED,
                initialDeposit, initialDeposit, "Account Opened");
        account.getTransactions().add(openTxn);
        fileManager.saveTransaction(openTxn);

        accounts.put(accountId, account);
        usedAccountIds.add(accountId);
        fileManager.saveAccount(account);

        System.out.println("✓ Current Account created: " + accountId);
        return account;
    }

    /**
     * Deposit money into an account
     */
    public void deposit(String accountId, double amount)
            throws AccountNotFoundException, InvalidAmountException {

        Account account = getAccountOrThrow(accountId);
        account.deposit(amount);
        fileManager.saveTransaction(account.getTransactions()
                .get(account.getTransactions().size() - 1));
        fileManager.saveAllAccounts(new ArrayList<>(accounts.values()));
    }

    /**
     * Withdraw money from an account
     */
    public void withdraw(String accountId, double amount)
            throws AccountNotFoundException, InvalidAmountException, InsufficientBalanceException {

        Account account = getAccountOrThrow(accountId);
        account.withdraw(amount);
        fileManager.saveTransaction(account.getTransactions()
                .get(account.getTransactions().size() - 1));
        fileManager.saveAllAccounts(new ArrayList<>(accounts.values()));
    }

    /**
     * Transfer money between accounts
     */
    public void transfer(String fromAccountId, String toAccountId, double amount)
            throws AccountNotFoundException, InvalidAmountException, InsufficientBalanceException {

        Account from = getAccountOrThrow(fromAccountId);
        Account to   = getAccountOrThrow(toAccountId);

        from.transferOut(amount, toAccountId);
        to.transferIn(amount, fromAccountId);

        // Save both transactions
        ArrayList<Transaction> fromTxns = from.getTransactions();
        ArrayList<Transaction> toTxns   = to.getTransactions();
        fileManager.saveTransaction(fromTxns.get(fromTxns.size() - 1));
        fileManager.saveTransaction(toTxns.get(toTxns.size() - 1));
        fileManager.saveAllAccounts(new ArrayList<>(accounts.values()));

        System.out.println("✓ Transfer of Rs." + amount + " from " + fromAccountId
                + " to " + toAccountId + " successful!");
    }

    /**
     * Check balance of an account
     */
    public double checkBalance(String accountId) throws AccountNotFoundException {
        Account account = getAccountOrThrow(accountId);
        double balance = account.getBalance();
        System.out.println("  Balance of " + accountId + " : Rs. " + String.format("%.2f", balance));
        return balance;
    }

    /**
     * Close an account
     */
    public void closeAccount(String accountId) throws AccountNotFoundException {
        Account account = getAccountOrThrow(accountId);
        account.setActive(false);
        fileManager.saveAllAccounts(new ArrayList<>(accounts.values()));
        System.out.println("✓ Account " + accountId + " closed.");
    }

    // ============================================================
    //  CUSTOMER MANAGEMENT
    // ============================================================

    /**
     * Register a new customer
     */
    public Customer registerCustomer(String firstName, String lastName, String email,
                                     String phone, String address, String password,
                                     LocalDate dateOfBirth) {
        Customer customer = new Customer(firstName, lastName, email,
                phone, address, password, dateOfBirth);
        customers.put(customer.getCustomerId(), customer);
        fileManager.saveCustomer(customer);
        fileManager.serializeCustomer(customer); // also serialize for demo
        System.out.println("✓ Customer registered: " + customer.getCustomerId());
        return customer;
    }

    /**
     * Login validation
     */
    public Customer login(String customerId, String password) throws AccountNotFoundException {
        if (!customers.containsKey(customerId)) {
            throw new AccountNotFoundException("Customer ID not found!", customerId);
        }
        Customer c = customers.get(customerId);
        if (!c.validatePassword(password)) {
            System.out.println("✗ Invalid password!");
            return null;
        }
        System.out.println("✓ Login successful! Welcome, " + c.getFullName());
        return c;
    }

    // ============================================================
    //  DISPLAY / REPORTS
    // ============================================================

    /**
     * Show transaction history using Streams API + Lambda
     */
    public void showTransactionHistory(String accountId) throws AccountNotFoundException {
        Account account = getAccountOrThrow(accountId);
        ArrayList<Transaction> txns = account.getTransactions();

        System.out.println("\n====== TRANSACTION HISTORY for " + accountId + " ======");
        System.out.printf("| %-12s | %-15s | %-12s | %10s | %10s | %s%n",
                "TXN ID", "ACCOUNT", "TYPE", "AMOUNT", "BALANCE", "DATE & TIME");
        System.out.println("-".repeat(85));

        if (txns.isEmpty()) {
            System.out.println("  No transactions found.");
        } else {
            // Lambda + Streams API to display transactions
            txns.stream()
                .forEach(t -> System.out.println(t));

            // Streams: calculate total deposits using lambda
            double totalDeposits = txns.stream()
                .filter(t -> t.getType() == Transaction.TransactionType.DEPOSIT)
                .mapToDouble(Transaction::getAmount)   // method reference
                .sum();

            double totalWithdrawals = txns.stream()
                .filter(t -> t.getType() == Transaction.TransactionType.WITHDRAWAL)
                .mapToDouble(Transaction::getAmount)
                .sum();

            System.out.println("-".repeat(85));
            System.out.printf("  Total Deposits: Rs.%.2f  |  Total Withdrawals: Rs.%.2f%n",
                    totalDeposits, totalWithdrawals);
        }
        System.out.println("=".repeat(85));
    }

    /**
     * Show all accounts using Streams + Lambda
     */
    public void showAllAccounts() {
        System.out.println("\n========== ALL ACCOUNTS ==========");
        if (accounts.isEmpty()) {
            System.out.println("  No accounts found.");
            return;
        }

        // Lambda expression to print each account
        accounts.values().forEach(a ->
            System.out.printf("  %-15s | %-10s | %-10s | Rs.%10.2f | %s%n",
                a.getAccountId(), a.getAccountType(),
                a.isActive() ? "Active" : "Closed",
                a.getBalance(), a.getOpeningDate())
        );
        System.out.println("==================================");
    }

    /**
     * Show accounts for a specific customer using Streams
     */
    public void showCustomerAccounts(String customerId) {
        System.out.println("\n=== Accounts for Customer: " + customerId + " ===");

        // Streams: filter by customerId + lambda
        List<Account> myAccounts = accounts.values().stream()
                .filter(a -> a.getCustomerId().equals(customerId))
                .collect(Collectors.toList());

        if (myAccounts.isEmpty()) {
            System.out.println("  No accounts found for this customer.");
        } else {
            myAccounts.forEach(Account::displayAccountInfo); // method reference
        }
    }

    /**
     * Apply interest to all savings accounts - Multithreading demo
     */
    public void applyInterestToAll() {
        // Runnable lambda - simple Multithreading example
        Runnable interestTask = () -> {
            System.out.println("\n[Thread] Applying interest to all Savings Accounts...");
            accounts.values().stream()
                .filter(a -> a instanceof SavingsAccount && a.isActive())
                .forEach(a -> a.applyInterest());
            fileManager.saveAllAccounts(new ArrayList<>(accounts.values()));
            System.out.println("[Thread] Interest applied successfully.");
        };

        // Create and start a Thread
        Thread interestThread = new Thread(interestTask, "InterestThread");
        interestThread.start();

        try {
            interestThread.join(); // wait for thread to finish
        } catch (InterruptedException e) {
            System.out.println("Interest thread interrupted: " + e.getMessage());
        }
    }

    // ============================================================
    //  HELPER METHODS
    // ============================================================

    private Account getAccountOrThrow(String accountId) throws AccountNotFoundException {
        Account account = accounts.get(accountId);
        if (account == null) {
            throw new AccountNotFoundException("Account not found!", accountId);
        }
        if (!account.isActive()) {
            throw new AccountNotFoundException("Account is closed!", accountId);
        }
        return account;
    }

    private String generateAccountId(String prefix) {
        String id;
        do {
            id = prefix + (++accountCounter);
        } while (usedAccountIds.contains(id)); // HashSet check for uniqueness
        return id;
    }

    private void loadDataFromFiles() {
        // Load customers
        ArrayList<Customer> loadedCustomers = fileManager.loadAllCustomers();
        for (Customer c : loadedCustomers) {
            customers.put(c.getCustomerId(), c);
        }

        // Load accounts
        ArrayList<Account> loadedAccounts = fileManager.loadAllAccounts();
        for (Account a : loadedAccounts) {
            accounts.put(a.getAccountId(), a);
            usedAccountIds.add(a.getAccountId());

            // Load transactions for each account
            ArrayList<Transaction> txns =
                    fileManager.loadTransactionsForAccount(a.getAccountId());
            a.setTransactions(txns);
        }

        System.out.println("✓ Loaded " + customers.size() + " customer(s) and "
                + accounts.size() + " account(s) from files.");
    }

    // Getters
    public String getBankName()                      { return bankName; }
    public HashMap<String, Account> getAccounts()    { return accounts; }
    public HashMap<String, Customer> getCustomers()  { return customers; }
    public Account getAccount(String id)             { return accounts.get(id); }
    public Customer getCustomer(String id)           { return customers.get(id); }
}

