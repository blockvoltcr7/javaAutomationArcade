package Encapsulation;

import java.util.HashSet;
import java.util.Set;

public class EncapsulationEx1 {

    /**
     * This class demonstrates the concept of encapsulation in Java.
     * 
     * Encapsulation is showcased through the following:
     * 1. **Private Class Members**: The class has private variables (portfolioId, investorName, totalBalance, investments) 
     *    that cannot be accessed directly from outside the class, ensuring data hiding.
     * 2. **Public Getter and Setter Methods**: The class provides public methods (getPortfolioId, getInvestorName, 
     *    getTotalBalance, setInvestorName) to access and modify the private variables. This allows for controlled access 
     *    and validation of data.
     * 3. **Validation in Setter and Other Methods**: The setter method for investorName and the addInvestment method 
     *    include validation checks to ensure that only valid data is assigned to the private members, further enforcing 
     *    the principles of encapsulation.
     */
    
    // Private class members - data hiding
    private int portfolioId;
    private String investorName;
    private double totalBalance;
    private Set<String> investments;
    
    // Constructor
    public EncapsulationEx1(int portfolioId, String investorName, double initialBalance) {
        this.portfolioId = portfolioId;
        this.investorName = investorName;
        this.totalBalance = initialBalance;
        this.investments = new HashSet<>();
    }
    
    // Getter methods
    public int getPortfolioId() {
        return portfolioId;
    }
    
    public String getInvestorName() {
        return investorName;
    }
    
    public double getTotalBalance() {
        return totalBalance;
    }
    
    // Setter methods with validation
    public void setInvestorName(String investorName) {
        if (investorName != null && !investorName.trim().isEmpty()) {
            this.investorName = investorName;
        }
    }
    
    // Method to add investment with validation
    public void addInvestment(String investment) {
        if (investment != null && !investment.trim().isEmpty()) {
            this.investments.add(investment);
        }
    }
    
    // Method to update balance
    public void updateBalance(double amount) {
        if (amount + this.totalBalance >= 0) {
            this.totalBalance += amount;
        } else {
            throw new IllegalArgumentException("Insufficient funds for withdrawal");
        }
    }
    
    // Method to get total number of investments
    public int getTotalInvestments() {
        int count = 0;
        // Iterating over HashSet
        for (String investment : investments) {
            count++;
        }
        return count;
    }
    
    // Method to display portfolio information
    public String getPortfolioInfo() {
        StringBuilder info = new StringBuilder();
        info.append("Portfolio ID: ").append(portfolioId).append("\n");
        info.append("Investor Name: ").append(investorName).append("\n");
        info.append("Total Balance: $").append(String.format("%.2f", totalBalance)).append("\n");
        info.append("Investments: ").append("\n");
        
        for (String investment : investments) {
            info.append("- ").append(investment).append("\n");
        }
        
        return info.toString();
    }
    
    // Main method to demonstrate the usage
    public static void main(String[] args) {
        // Creating portfolio object
        EncapsulationEx1 portfolio = new EncapsulationEx1(1001, "Jane Smith", 100000.00);
        
        // Adding investments
        portfolio.addInvestment("S&P 500 Index Fund");
        portfolio.addInvestment("Government Bonds");
        portfolio.addInvestment("Tech Sector ETF");
        
        // Displaying initial portfolio information
        System.out.println("Initial Portfolio Status:");
        System.out.println(portfolio.getPortfolioInfo());
        
        // Performing transactions
        portfolio.updateBalance(25000.00);  // Adding funds
        portfolio.updateBalance(-10000.00); // Withdrawing funds
        
        // Getting total number of investments
        System.out.println("\nTotal number of investments: " + portfolio.getTotalInvestments());
        
        // Displaying updated portfolio information
        System.out.println("\nUpdated Portfolio Status:");
        System.out.println(portfolio.getPortfolioInfo());
    }
}


