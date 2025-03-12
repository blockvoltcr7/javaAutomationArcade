package Generics;

import java.util.ArrayList;
import java.util.List;

/**
 * A generic interface for financial instruments.
 * @param <T> The type of the financial instrument.
 */
interface FinancialInstrument<T> {
    String getName();
    T getValue();
}

/**
 * Represents a stock with a name and a price.
 */
class Stock implements FinancialInstrument<Double> {
    private String name;
    private double price;

    public Stock(String name, double price) {
        this.name = name;
        this.price = price;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Double getValue() {
        return price;
    }
}

/**
 * Represents a bond with a name and a face value.
 */
class Bond implements FinancialInstrument<Double> {
    private String name;
    private double faceValue;

    public Bond(String name, double faceValue) {
        this.name = name;
        this.faceValue = faceValue;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Double getValue() {
        return faceValue;
    }
}

/**
 * A generic portfolio class that can hold different types of financial instruments.
 * @param <T> The type of financial instrument.
 */
class Portfolio<T extends FinancialInstrument<?>> {
    private List<T> instruments;

    public Portfolio() {
        instruments = new ArrayList<>();
    }

    /**
     * Adds a financial instrument to the portfolio.
     * @param instrument The financial instrument to add.
     */
    public void addInstrument(T instrument) {
        instruments.add(instrument);
    }

    /**
     * Calculates the total value of the portfolio.
     * @return The total value of all instruments in the portfolio.
     */
    public double getTotalValue() {
        double totalValue = 0.0;
        for (T instrument : instruments) {
            totalValue += instrument.getValue();
        }
        return totalValue;
    }

    /**
     * Displays the instruments in the portfolio.
     */
    public void displayInstruments() {
        for (T instrument : instruments) {
            System.out.println("Instrument: " + instrument.getName() + ", Value: " + instrument.getValue());
        }
    }
}

/**
 * Main class to demonstrate the portfolio management system.
 */
public class GenericFinancialInstruExample {
    public static void main(String[] args) {
        // Create a portfolio for financial instruments
        Portfolio<FinancialInstrument<?>> portfolio = new Portfolio<>();

        // Add stocks to the portfolio
        Stock appleStock = new Stock("Apple Inc.", 150.00);
        Stock googleStock = new Stock("Alphabet Inc.", 2800.00);
        portfolio.addInstrument(appleStock);
        portfolio.addInstrument(googleStock);

        // Add bonds to the portfolio
        Bond treasuryBond = new Bond("US Treasury Bond", 1000.00);
        portfolio.addInstrument(treasuryBond);

        // Display instruments and total value
        portfolio.displayInstruments();
        System.out.println("Total Portfolio Value: $" + portfolio.getTotalValue());
    }
}
