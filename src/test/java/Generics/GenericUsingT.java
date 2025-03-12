package Generics;

public class GenericUsingT {


    public static void main(String[] args) {

        System.out.println("Hello, World!");
        GenericUsingT genericUsingT = new GenericUsingT();
        genericUsingT.demonstrateFinancialTransactions();
    }

    /**
     * A generic class representing a financial transaction
     * @param <T> The type of value being transacted (e.g., Integer for cents, Double for dollars)
     */
    private static class Transaction<T extends Number> {
        private String accountId;
        private T amount;
        private String transactionType;

        /**
         * Constructor to create a financial transaction
         * @param accountId The account identifier
         * @param amount The transaction amount
         * @param transactionType The type of transaction (deposit/withdrawal)
         */
        public Transaction(String accountId, T amount, String transactionType) {
            this.accountId = accountId;
            this.amount = amount;
            this.transactionType = transactionType;
        }

        /**
         * Gets the transaction amount
         * @return The amount of the transaction
         */
        public T getAmount() {
            return amount;
        }

        /**
         * Displays transaction details with proper formatting
         */
        public void displayTransaction() {
            System.out.printf("Account: %s%n", accountId);
            System.out.printf("Transaction Type: %s%n", transactionType);
            System.out.printf("Amount: $%.2f%n", amount.doubleValue());
            System.out.println("------------------------");
        }
    }

    /**
     * Demonstrates usage of the generic Transaction class with different numeric types
     */
    public void demonstrateFinancialTransactions() {
        // Transaction with Integer (cents)
        Transaction<Integer> atmWithdrawal = new Transaction<>("ACC001", 5000, "ATM Withdrawal");
        atmWithdrawal.displayTransaction(); // Will show $50.00

        // Transaction with Double (dollars)
        Transaction<Double> wireTransfer = new Transaction<>("ACC002", 1250.75, "Wire Transfer");
        wireTransfer.displayTransaction(); // Will show $1,250.75

        // Transaction with Float (dollars)
        Transaction<Float> checkDeposit = new Transaction<>("ACC003", 525.50f, "Check Deposit");
        checkDeposit.displayTransaction(); // Will show $525.50
    }
    
}
