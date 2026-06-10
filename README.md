# 🏦 LAKSHYA National Bank — Management System
### First Year B.Tech Java OOP Project

---

## 📁 Project Structure
```
BankManagementSystem/
├── src/                        ← Java Backend (Core Logic)
│   ├── Account.java            ← Abstract Class (Abstraction)
│   ├── SavingsAccount.java     ← Inheritance + Method Overriding
│   ├── CurrentAccount.java     ← Inheritance + Overdraft
│   ├── Customer.java           ← Encapsulation + Serializable
│   ├── Transaction.java        ← Enum + Date/Time API
│   ├── Bank.java               ← Collections + Streams + Threads
│   ├── FileManager.java        ← File Handling + Serialization
│   ├── InvalidAmountException.java
│   ├── InsufficientBalanceException.java
│   ├── AccountNotFoundException.java
│   └── Main.java               ← Entry Point (Menu-Driven)
│
├── frontend/                   ← HTML/CSS/JS Website
│   ├── index.html              ← Home Page
│   ├── login.html              ← Login Page
│   ├── register.html           ← Registration Page
│   ├── dashboard.html          ← User Dashboard
│   ├── deposit.html            ← Deposit Page
│   ├── withdraw.html           ← Withdraw Page
│   ├── history.html            ← Transaction History
│   ├── css/style.css           ← All Styling
│   └── js/script.js            ← All JS Validation & Logic
│
└── data/                       ← Text File Storage
    ├── accounts.txt
    ├── customers.txt
    └── transactions.txt
```

---

## 🚀 How to Run the Java Backend

### Step 1 — Open Terminal in `src/` folder
```
cd BankManagementSystem/src
```

### Step 2 — Compile ALL Java files
```
javac *.java
```

### Step 3 — Run the Main class
```
java Main
```

---

## 🌐 How to Run the Frontend

Open any HTML file directly in your browser:
- Double-click `frontend/index.html`
- Or right-click → Open with → Chrome/Firefox/Edge

No server required. The frontend uses LocalStorage for demo data.

---

## ✅ Java Concepts Demonstrated

| Concept | Where Used |
|---|---|
| Abstract Class | `Account.java` |
| Inheritance | `SavingsAccount`, `CurrentAccount` extend `Account` |
| Encapsulation | All private fields + getters/setters |
| Polymorphism | `withdraw()` overridden differently in each subclass |
| Method Overloading | `displayAccountInfo()` in SavingsAccount/CurrentAccount |
| Interfaces | `Serializable` implemented in Account, Customer, Transaction |
| Custom Exceptions | `InvalidAmountException`, `InsufficientBalanceException`, `AccountNotFoundException` |
| Enum | `TransactionType` in Transaction.java |
| Collections | `HashMap`, `ArrayList`, `HashSet` in Bank.java |
| Generics | `ArrayList<Transaction>`, `HashMap<String, Account>` |
| Lambda Expressions | `accounts.values().forEach(a -> ...)` in Bank.java |
| Streams API | `.stream().filter().mapToDouble().sum()` in Bank.java |
| File Handling | `FileReader`, `FileWriter`, `BufferedReader`, `BufferedWriter` |
| Serialization | `ObjectOutputStream`, `ObjectInputStream` in FileManager.java |
| Multithreading | `Thread` + `Runnable` for interest calculation in Bank.java |
| Date/Time API | `LocalDate`, `LocalDateTime`, `Period` |
| Wrapper Classes | `Integer.parseInt()`, `Double.parseDouble()`, `Boolean.parseBoolean()` |
| Scanner | User input in Main.java |
| Exception Handling | `try-catch-finally` throughout |

---

## 📋 Sample Usage

```
========== MAIN MENU ==========
1. Register New Customer
2. Login
3. Exit

Enter choice: 1

----- REGISTER NEW CUSTOMER -----
First Name    : Lakshya
Last Name     : Dhaneshwar
Email         : lakshya@bank.com
Phone Number  : 9876543210
Address       : New Delhi
Password      : secure123
Date of Birth : 2005-12-12

✓ Registration successful!
Your Customer ID: CUST1001
```

---

**Built with:** Java JDK 25 | HTML5 | CSS3 | JavaScript
**Author:** First Year B.Tech CS Student Project

