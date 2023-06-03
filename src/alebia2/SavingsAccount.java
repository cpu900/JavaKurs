/**
 * Inlämningsuppgift 4
 * Kurs: D0018D
 * Datum: 2023-06-03
 * Version: 4
 * @author Alexander Bianca Tofer, alebia-2
 */


package alebia2;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;


public class SavingsAccount extends Account implements Serializable {

    //-----------------------------------------------------------------------------------
    // Variables
    //-----------------------------------------------------------------------------------

    private static final BigDecimal INTEREST_RATE = new BigDecimal("1.2"); // Spar Ränta i procent %
    private static final BigDecimal CREDIT_LIMIT = new BigDecimal("0"); // Kredit limit 0
    private static final int FREE_WITHDRAWALS_PER_YEAR = 1; // Antal gratis uttag per år
    private static final BigDecimal WITHDRAWAL_FEE = new BigDecimal("2.0"); // Avgift i procent % vid uttag efter gratis uttag
    private static final BigDecimal ZERO_BALANCE = BigDecimal.ZERO; // Saldo startar på 0

    //-----------------------------------------------------------------------------------
    // Public Methods
    //-----------------------------------------------------------------------------------

    // Skapar ett nytt sparkonto
    public SavingsAccount(String ownerPno, int accountNumber) {
        super(ownerPno, accountNumber, ZERO_BALANCE, "Sparkonto", INTEREST_RATE, CREDIT_LIMIT, 0);
    }


    // Uttags funktionen för sparkonto
    // Kollar om det finns fria uttag kvar
    // Uttag efter det första fria uttaget beläggs med en WITHDRAWAL_FEE i procent på uttaget belopp.
    @Override
    public boolean withdraw(BigDecimal amount) {
        if (amount.compareTo(ZERO_BALANCE) <= 0) {
            return false;
        }

        if (getAccountWithdrawals() >= FREE_WITHDRAWALS_PER_YEAR) {
            BigDecimal ONE_HUNDRED_BIG_D = new BigDecimal("100");
            BigDecimal withdrawalFee = amount.multiply(WITHDRAWAL_FEE.divide(ONE_HUNDRED_BIG_D));
            amount = amount.add(withdrawalFee);
        }

        if (getBalance().subtract(amount).compareTo(ZERO_BALANCE) < 0) {
            return false;
        }

        // Ändra saldo på konto
        updateBalanceSubtract(amount);

        // Räkna upp uttag
        updateNoOfWithdraws();

        // Skriv in i log
        logTransaction("WITHDRAW", amount);

        return true;
    }

}


