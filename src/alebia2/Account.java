/**
 * Inlämningsuppgift 2
 * Kurs: D0018D
 * Datum: 2023-02-24
 * Version: 2
 * @author Alexander Bianca Tofer, alebia-2
 */

package alebia2;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Locale;


abstract public class Account implements Serializable {

    //-----------------------------------------------------------------------------------
    // Variables
    //-----------------------------------------------------------------------------------

    private int accountNumber; // Konto nr
    private String ownerPno; // Pnr på ägaren till kontot
    private BigDecimal balance; // Belopp på kontot
    private String accountType; // Typ: Sparkonto / Kreditkonto
    private BigDecimal interestRate; // Ränta
    private BigDecimal creditLimit; // Limit på minus värde på kontot
    private int noOfWithdraws; // Antalet uttag från kontot som har genomförts
    private ArrayList<String[]> transactionLog = new ArrayList<>(); // Lista med transaktioner
    private static final Locale LOCALE_SV = Locale.forLanguageTag("sv-SE"); // Formatering till svenska
    private static final BigDecimal ONE_HUNDRED_BIG_D = new BigDecimal("100"); // För att räkna med BigDecimal


    //-----------------------------------------------------------------------------------
    // Getters
    //-----------------------------------------------------------------------------------

    public int getAccountNumber() {
        return accountNumber;
    }

    public String getOwnerPno() {
        return ownerPno;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public String getAccountType() {
        return accountType;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public BigDecimal getCreditLimit() {
        return creditLimit;
    }

    public int getAccountWithdrawals() {
        return noOfWithdraws;
    }

    //-----------------------------------------------------------------------------------
    // Abstract Methods
    //-----------------------------------------------------------------------------------

    // Alla underklasser ska ha en uttags funktion
    abstract public boolean withdraw(BigDecimal amount);

    //-----------------------------------------------------------------------------------
    // Public Methods
    //-----------------------------------------------------------------------------------

    // Skapar ett konto
    public Account(String ownerPno, int accountNumber, BigDecimal balance, String accountType, BigDecimal interestRate, BigDecimal creditLimit, int noOfWithdraws) {
        this.ownerPno = ownerPno;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.accountType = accountType;
        this.interestRate = interestRate;
        this.creditLimit = creditLimit;
        this.noOfWithdraws = noOfWithdraws;
    }

    // Insättning med en kontroll
    boolean deposit(BigDecimal amount) {
        // Koll om beloppet är större än 0
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }

        // Sätt in på kontot
        this.balance = balance.add(amount);

        // Skriv in insättning i log boken
        logTransaction("DEPOSIT", amount);

        return true;
    }

    // Beräkna räntan för ett konto
    BigDecimal calculateInterest() {
        return getBalance().multiply(getInterestRate()).divide(ONE_HUNDRED_BIG_D, RoundingMode.HALF_EVEN);
    }

    // Uppdatera saldo på kontot
    void updateBalanceSubtract(BigDecimal amount) {
        setBalance(getBalance().subtract(amount));
    }

    // Uppdatera antalet uttag med 1
    void updateNoOfWithdraws() {
        addOneWithdraw();
    }

    // Uppdatera Listan med transaktioner
    void logTransaction(String transactionType, BigDecimal amount) {
        String[] logEntry = new String[4];
        logEntry[0] = LocalDateTime.now().toString(); // Spara aktuell tid för transaktionen
        logEntry[1] = transactionType;
        logEntry[2] = amount.toString();
        logEntry[3] = getBalance().toString();
        transactionLog.add(logEntry);
    }


    // Returnera transaktionslista för ett konto
    ArrayList<String> getTransactions() {
        ArrayList<String> transactionList = new ArrayList<>(); // Skapa en ny lista att returnera
        for (String[] logEntry : transactionLog) {
            String[] dateAndTime = logEntry[0].split("T"); // dela på datum & tid
            String transactionType = logEntry[1].equals("DEPOSIT") ? "" : "-"; // Lägg till minus om uttag
            String amount = formatBalance(logEntry[2]); // formatera belopp
            String balance = "Saldo: " + formatBalance(logEntry[3]); // Skriv ut saldo efter transaktionen
            String transactionInfo = dateAndTime[0] + " " + dateAndTime[1].substring(0, 8) + " " + transactionType + amount + " " + balance;
            transactionList.add(transactionInfo);
        }
        return transactionList;
    }


    //-----------------------------------------------------------------------------------
    // Private Methods
    //-----------------------------------------------------------------------------------

    // För ändring av saldo
    private void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    // Räkna upp uttag
    private void addOneWithdraw() {
        this.noOfWithdraws++;
    }

    // Formatera belopp till svensk layout
    private String formatBalance(String balance) {
        BigDecimal balanceDecimal = new BigDecimal(balance);
        return NumberFormat.getCurrencyInstance(LOCALE_SV).format(balanceDecimal);
    }

}



