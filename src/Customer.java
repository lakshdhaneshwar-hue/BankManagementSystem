import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;

/**
 * Customer Class
 * Represents a bank customer with personal details.
 * Demonstrates: Encapsulation, Constructors, toString(),
 *               Serialization, Date/Time API, Wrapper Classes
 */
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;

    // Private fields - Encapsulation
    private String customerId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private String password;      // stored as plain text for simplicity
    private LocalDate dateOfBirth;
    private LocalDate registrationDate;
    private boolean isActive;

    // Static counter for generating customer IDs
    private static int idCounter = 1000;

    // ---- Constructors ----

    // Default Constructor
    public Customer() {
        this.registrationDate = LocalDate.now();
        this.isActive         = true;
        this.customerId       = "CUST" + (++idCounter);
    }

    // Parameterized Constructor
    public Customer(String firstName, String lastName, String email,
                    String phone, String address, String password, LocalDate dateOfBirth) {
        this();   // chain to default constructor
        this.firstName   = firstName;
        this.lastName    = lastName;
        this.email       = email;
        this.phone       = phone;
        this.address     = address;
        this.password    = password;
        this.dateOfBirth = dateOfBirth;
    }

    // Constructor for loading from file (ID already known)
    public Customer(String customerId, String firstName, String lastName,
                    String email, String phone, String address,
                    String password, LocalDate dateOfBirth, LocalDate registrationDate) {
        this.customerId       = customerId;
        this.firstName        = firstName;
        this.lastName         = lastName;
        this.email            = email;
        this.phone            = phone;
        this.address          = address;
        this.password         = password;
        this.dateOfBirth      = dateOfBirth;
        this.registrationDate = registrationDate;
        this.isActive         = true;
    }

    // ---- Getters and Setters (Encapsulation) ----
    public String getCustomerId()             { return customerId; }
    public void setCustomerId(String id)      { this.customerId = id; }

    public String getFirstName()              { return firstName; }
    public void setFirstName(String name)     { this.firstName = name; }

    public String getLastName()               { return lastName; }
    public void setLastName(String name)      { this.lastName = name; }

    public String getFullName()               { return firstName + " " + lastName; }

    public String getEmail()                  { return email; }
    public void setEmail(String email)        { this.email = email; }

    public String getPhone()                  { return phone; }
    public void setPhone(String phone)        { this.phone = phone; }

    public String getAddress()                { return address; }
    public void setAddress(String address)    { this.address = address; }

    public String getPassword()               { return password; }
    public void setPassword(String password)  { this.password = password; }

    public LocalDate getDateOfBirth()         { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dob) { this.dateOfBirth = dob; }

    public LocalDate getRegistrationDate()    { return registrationDate; }

    public boolean isActive()                 { return isActive; }
    public void setActive(boolean active)     { this.isActive = active; }

    // ---- Business Methods ----

    // Calculate age using Period - Date/Time API
    public int getAge() {
        if (dateOfBirth == null) return 0;
        return Period.between(dateOfBirth, LocalDate.now()).getYears();
    }

    // Validate password
    public boolean validatePassword(String inputPassword) {
        return this.password.equals(inputPassword);
    }

    // Convert to CSV for file storage
    public String toCsvString() {
        return customerId + "," + firstName + "," + lastName + "," +
               email + "," + phone + "," + address + "," +
               password + "," + dateOfBirth + "," + registrationDate;
    }

    // Display customer info
    public void displayCustomerInfo() {
        System.out.println("\n========== CUSTOMER DETAILS ==========");
        System.out.println("  Customer ID  : " + customerId);
        System.out.println("  Name         : " + getFullName());
        System.out.println("  Email        : " + email);
        System.out.println("  Phone        : " + phone);
        System.out.println("  Address      : " + address);
        System.out.println("  Date of Birth: " + dateOfBirth);
        System.out.println("  Age          : " + getAge() + " years");
        System.out.println("  Member Since : " + registrationDate);
        System.out.println("  Status       : " + (isActive ? "Active" : "Inactive"));
        System.out.println("======================================");
    }

    @Override
    public String toString() {
        // Wrapper class usage: Integer.toString()
        return "Customer[" + customerId + " | " + getFullName() + " | " + email + "]";
    }

    // equals() override - two customers are equal if same ID
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Customer other = (Customer) obj;
        return customerId.equals(other.customerId);
    }

    @Override
    public int hashCode() {
        // Wrapper class: Integer.hashCode
        return Integer.hashCode(customerId.hashCode());
    }
}

