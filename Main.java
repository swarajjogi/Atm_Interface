import java.util.Scanner;

// Account holder class
class AccountHolder {
    private String id;
    private String pin;

    public AccountHolder(String id, String pin) {
        this.id = id;
        this.pin = pin;
    }

    public String getId() {
        return id;
    }

    public String getPin() {
        return pin;
    }
}

// Bank transaction class
class BankTransaction {
    private String type;
    private double amount;
    private String source;
    private String target;

    public BankTransaction(String type, double amount, String source, String target) {
        this.type = type;
        this.amount = amount;
        this.source = source;
        this.target = target;
    }

    public String getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public String getSource() {
        return source;
    }

    public String getTarget() {
        return target;
    }

    public String toString() {
        return "Type: " + type + ", Amount: " + amount + ", Source: " + source + ", Target: " + target;
    }
}

// Account class
class Account {
    private String number;
    private double balance;
    private BankTransaction[] transactions;

    public Account(String number, double balance) {
        this.number = number;
        this.balance = balance;
        transactions = new BankTransaction[100];
    }

    public String getNumber() {
        return number;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        balance += amount;
        addTransaction(new BankTransaction("DEPOSIT", amount, null, number));
        System.out.println("Deposit successful. New balance: " + balance);
    }

    public void withdraw(double amount) {
        if (balance >= amount) {
            balance -= amount;
            addTransaction(new BankTransaction("WITHDRAWAL", amount, number, null));
            System.out.println("Withdrawal successful. New balance: " + balance);
        } else {
            System.out.println("Insufficient funds.");
        }
    }

    public void transfer(double amount, Account target) {
        if (balance >= amount) {
            balance -= amount;
            target.deposit(amount);
            addTransaction(new BankTransaction("TRANSFER", amount, number, target.getNumber()));
            System.out.println("Transfer successful. New balance: " + balance);
        } else {
            System.out.println("Insufficient funds.");
        }
    }

    public void addTransaction(BankTransaction transaction) {
        for (int i = 0; i < transactions.length; i++) {
            if (transactions[i] == null) {
                transactions[i] = transaction;
                break;
            }
        }
    }

    public void showTransactions() {
        for (int i = 0; i < transactions.length; i++) {
            if (transactions[i] != null) {
                System.out.println(transactions[i]);
            }
        }
    }
}

// Bank class
class Bank {
    private String name;
    private Account[] accounts;

    public Bank(String name) {
        this.name = name;
        accounts = new Account[100];
    }

    public void addAccount(Account account) {
        for (int i = 0; i < accounts.length; i++) {
            if (accounts[i] == null) {
                accounts[i] = account;
                break;
            }
        }
    }

    public Account findAccount(String number) {
        for (int i = 0; i < accounts.length; i++) {
            if (accounts[i] != null && accounts[i].getNumber().equals(number)) {
                return accounts[i];
            }
        }
        return null;
    }
}

// ATM class
class ATM {
    private AccountHolder accountHolder;
    private Bank bank;

    public ATM(AccountHolder accountHolder, Bank bank) {
        this.accountHolder = accountHolder;
        this.bank = bank;
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to Trusted bank");
        System.out.println("Enter user id:");
        String id = scanner.nextLine();
        System.out.println("Enter user pin:");
        String pin = scanner.nextLine();

        // Authenticate user
        if (id.equals(accountHolder.getId()) && pin.equals(accountHolder.getPin())) {
            System.out.println("Welcome " + accountHolder.getId() + "!");

            boolean quit = false;
            while (!quit) {
                // Display menu
                System.out.println("Select operation:");
                System.out.println("1. Show transactions history");
                System.out.println("2. Withdraw");
                System.out.println("3. Deposit");
                System.out.println("4. Transfer");
                System.out.println("5. Quit");

                // Read user input
                String choice = scanner.nextLine();

                // Execute selected operation
                switch (choice) {
                    case "1":
                        showTransactions();
                        break;
                    case "2":
                        withdraw();
                        break;
                    case "3":
                        deposit();
                        break;
                    case "4":
                        transfer();
                        break;
                    case "5":
                        quit = true;
                        System.out.println("Goodbye! Have a nice day..");
                        break;
                    default:
                        System.out.println("Invalid choice.");
                        break;
                }
            }
        } else {
            System.out.println("Authentication failed. Please try again.");
        }
    }

    private void showTransactions() {
        Account account = getAccount();
        if (account != null) {
            account.showTransactions();
        }
    }

    private void withdraw() {
        Scanner scanner = new Scanner(System.in);

        Account account = getAccount();
        if (account != null) {
            System.out.println("Enter withdrawal amount:");
            double amount = Double.parseDouble(scanner.nextLine());
            account.withdraw(amount);
        }
    }

    private void deposit() {
        Scanner scanner = new Scanner(System.in);

        Account account = getAccount();
        if (account != null) {
            System.out.println("Enter deposit amount:");
            double amount = Double.parseDouble(scanner.nextLine());
            account.deposit(amount);
        }
    }

    private void transfer() {
        Scanner scanner = new Scanner(System.in);

        Account source = getAccount();
        if (source != null) {
            System.out.println("Enter target account number:");
            String targetNumber = scanner.nextLine();
            Account target = bank.findAccount(targetNumber);
            if (target != null) {
                System.out.println("Enter transfer amount:");
                double amount = Double.parseDouble(scanner.nextLine());
                source.transfer(amount, target);
            } else {
                System.out.println("Invalid target account number.");
            }
        }
    }

    private Account getAccount() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter account number:");
        String number = scanner.nextLine();
        Account account = bank.findAccount(number);
        if (account == null) {
            System.out.println("Invalid account number.");
        }
        return account;
    }
}

// Main class
public class Main {
    public static void main(String[] args) {
        // Create bank
        Bank bank = new Bank("Trusted bank");
        // Create accounts
        Account account1 = new Account("1234567890", 1000);
        Account account2 = new Account("0987654321", 500);

        // Add accounts to bank
        bank.addAccount(account1);
        bank.addAccount(account2);

        // Create account holder
        AccountHolder accountHolder = new AccountHolder("swaraj", "1234");
        // Create ATM
        ATM atm = new ATM(accountHolder, bank);

        // Start ATM
        atm.start();
    }
}
