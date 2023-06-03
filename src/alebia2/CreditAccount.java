/**
 * Inlämningsuppgift 4
 * Kurs: D0018D
 * Datum: 2023-06-03
 * Version: 4
 * @author Alexander Bianca Tofer, alebia-2
 */

package alebia2;

import java.io.Serializable;
import java.math.BigDecimal;

public class CreditAccount extends Account implements Serializable {

    //-----------------------------------------------------------------------------------
    // Variables
    //-----------------------------------------------------------------------------------

    private static final BigDecimal CREDIT_LIMIT = new BigDecimal("5000"); // Kredit limit upp till 5000
    private static final BigDecimal INTEREST_RATE = new BigDecimal("0.5"); // Spar Ränta 0,5 % om saldo >=0
    private static final BigDecimal DEBT_INTEREST_RATE = new BigDecimal("7"); // Kredit ränta 7 % saldo <0
    private static final BigDecimal ZERO_BALANCE = BigDecimal.ZERO; // Saldo startar på 0


    //-----------------------------------------------------------------------------------
    // Public Methods
    //-----------------------------------------------------------------------------------

    public CreditAccount(String ownerPno, int accountNumber) {
        super(ownerPno, accountNumber, ZERO_BALANCE, "Kreditkonto", INTEREST_RATE, CREDIT_LIMIT,0);
    }

    // Kolla räntan på kreditkontot, beror på om saldot är under 0
    @Override
    public BigDecimal getInterestRate() {
        if (getBalance().compareTo(ZERO_BALANCE) < 0) {
            return DEBT_INTEREST_RATE;
        }
        return INTEREST_RATE;
    }

    // Uttag från kreditkonto behandlas här
    @Override
    public boolean withdraw(BigDecimal amount) {
        if (amount.compareTo(ZERO_BALANCE) <= 0) {
            return false;
        }
        BigDecimal newBalance = getBalance().subtract(amount);
        if (newBalance.compareTo(CREDIT_LIMIT.negate()) >= 0) {

            // Ändra saldo på konto
            updateBalanceSubtract(amount);

            // Skriv in i log
            logTransaction("WITHDRAW", amount);

            return true;
        }
        return false;
    }

}