package org.wonder;

public class Salary {
    private final String personName;
    private int amount;
    private String currency;

    public Salary(int amount, String currency) {
        this(amount, currency, null);
    }

    public Salary(int amount, String currency, String personName) {
        this.amount = amount;
        this.currency = currency;
        this.personName = personName;
    }

    public int getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }
}
