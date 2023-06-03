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
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class BankLogic implements Serializable {

    //-----------------------------------------------------------------------------------
    // Variables
    //-----------------------------------------------------------------------------------
    private ArrayList<Customer> customerList = new ArrayList<>(); // innehåller: pNo, firstName, lastName
    private ArrayList<Account> accountList = new ArrayList<>(); // innehåller: accountNumber, ownerPno, balance, accountType, interestRate
    private static int bankAccountsCounter = 1001; // Bankens första konto är 1001
    private static final Locale LOCALE_SV = Locale.forLanguageTag("sv-SE"); // Formatering till svenska
    private static final BigDecimal ONE_HUNDRED_BIG_D = new BigDecimal("100"); // För att räkna med BigDecimal


    //-----------------------------------------------------------------------------------
    // Public Methods
    //-----------------------------------------------------------------------------------

    public BankLogic() {
    }

    // Presentation av bankens alla kunder
    public ArrayList<String> getAllCustomers() {
        ArrayList<String> customerList = new ArrayList<>(); // Skapa en ny lista att returnera
        for (Customer customer : this.customerList) {
            customerList.add(customer.getPNo() + " " + customer.getFirstName() + " " + customer.getLastName());
        }
        return customerList;
    }

    // Skapar en ny kund
    public boolean createCustomer(String name, String surname, String pNo) {
        // Extra koll om en kund redan finns med det personnumret, returnera FALSE
        for (Customer customer : customerList) {
            if (customer.getPNo().equals(pNo)) {
                return false;
            }
        }

        // Skapar en kund och returnera TRUE
        Customer newCustomer = new Customer(name, surname, pNo);
        customerList.add(newCustomer);
        return true;
    }

    // Informationen om kunden inklusive dennes konton.
    public ArrayList<String> getCustomer(String pNo) {
        Customer customer = findCustomer(pNo);
        if (customer == null) {
            return null;
        }
        ArrayList<String> customerInfoList = new ArrayList<>(); // Skapa en ny ArrayList för att returnera med all info om kunden.
        customerInfoList.add(customer.getPNo() + " " + customer.getFirstName() + " " + customer.getLastName());
        for (Account account : accountList) {
            if (account.getOwnerPno().equals(pNo)) {
                customerInfoList.add(account.getAccountNumber() + " " + formatBalance(account.getBalance()) + " " + account.getAccountType() + " " + formatInterest(account.getInterestRate()));
            }
        }
        return customerInfoList;
    }

    // Byter namn på vald kund
    public boolean changeCustomerName(String name, String surname, String pNo) {
        for (Customer customer : this.customerList) {
            if (customer.getPNo().equals(pNo)) {
                return customer.changeName(name, surname);
            }
        }
        return false;
    }

    // Skapar ett SPAR konto till kund med personnummer pNo
    public int createSavingsAccount(String pNo) {
        for (Customer customer : customerList) {
            if (customer.getPNo().equals(pNo)) {

                int accountNo = bankAccountsCounter++; // Sätt nummer på konto & räkna upp konton som finns i banken

                Account newAccount = new SavingsAccount(pNo, accountNo);
                accountList.add(newAccount);
                return accountNo;
            }
        }
        return -1;
    }


    // Skapar ett KREDIT konto till kund med personnummer pNo
    public int createCreditAccount(String pNo) {
        for (Customer customer : customerList) {
            if (customer.getPNo().equals(pNo)) {

                int accountNo = bankAccountsCounter++; // Sätt nummer på konto & räkna upp konton som finns i banken

                CreditAccount newAccount = new CreditAccount(pNo, accountNo);
                accountList.add(newAccount);
                return accountNo;
            }
        }
        return -1;
    }


    // Hämtar en lista som innehåller presentation av alla transaktioner som gjorts på kontot (datum, tid, belopp, saldo efter transaktionen)
    public ArrayList<String> getTransactions(String pNo, int accountId) {
        Account account = findAccount(pNo, accountId);
        if (account == null) {
            return null;
        }
        return account.getTransactions();
    }


    // Presentation av kontot med kontonummer accountId som tillhör kunden med personnummer pNo
    public String getAccount(String pNo, int accountId) {
        Account account = findAccount(pNo, accountId); // Hitta alla konton till kunden
        if (account == null) {
            return null; // Inget konto hittades
        }

        return account.getAccountNumber() + " " + formatBalance(account.getBalance()) + " " + account.getAccountType() + " " + formatInterest(account.getInterestRate());
    }

    // Gör en insättning på konto med kontonummer accountId som tillhör kunden med personnummer pNo.
    public boolean deposit(String pNo, int accountId, float amount) {
        Account account = findAccount(pNo, accountId);
        if (account == null) {
            return false;
        }
        return account.deposit(convertBigD(amount));
    }

    // Gör ett uttag på konto med kontonummer accountId som tillhör kunden med personnummer pNo.
    public boolean withdraw(String pNo, int accountId, float amount) {
        // Snabbkoll att det är över 0 som tas ut
        if (amount <= 0) {
            return false;
        }

        // Leta upp konto
        Account account = findAccount(pNo, accountId);
        if (account == null) {
            return false;
        }

        // Skicka uttaget till account för behandling
        return account.withdraw(convertBigD(amount));
    }

    // Avslutar ett konto med kontonummer accountId som tillhör kunden med personnummer pNo.
    // När man avslutar ett konto skall räntan beräknas som saldo*räntesats/100.
    public String closeAccount(String pNo, int accountId) {
        // Kolla om konto finns och tillhör kunden.
        Account account = findAccount(pNo, accountId);
        if (account == null) {
            return null;
        }

        // Beräkna ränta innan kontot tas bort
        BigDecimal interest = account.calculateInterest();

        // Ta bort konto
        accountList.remove(account);

        // Returnera avslutat konto med belopp & ränta
        return account.getAccountNumber() + " " + formatBalance(account.getBalance()) + " " + account.getAccountType() + " " + formatBalance(interest);
    }


    // Tar bort en kund med personnummer pNo ur banken, alla kundens eventuella konton tas också bort och resultatet returneras.
    public ArrayList<String> deleteCustomer(String pNo) {
        Customer customer = findCustomer(pNo);
        if (customer == null) {
            return null;
        }

        ArrayList<String> deletedAccountsList = new ArrayList<>();
        for (Account account : accountList) {
            if (account.getOwnerPno().equals(pNo)) {

                // Beräkna räntan först
                BigDecimal interest = account.calculateInterest();

                deletedAccountsList.add(account.getAccountNumber() + " " + formatBalance(account.getBalance()) + " " + account.getAccountType() + " " + formatBalance(interest));
            }
        }
        customerList.remove(customer);
        ArrayList<String> result = new ArrayList<>();
        result.add(customer.getPNo() + " " + customer.getFirstName() + " " + customer.getLastName());
        result.addAll(deletedAccountsList);
        return result;
    }

    public static int getBankAccountsCounter() {
        return bankAccountsCounter;
    }

    public static void setBankAccountsCounter(int bankAccountsCounter) {
        BankLogic.bankAccountsCounter = bankAccountsCounter;
    }


    //-----------------------------------------------------------------------------------
    // PRIVATE Methods - hjälp metoder för BankLogic
    //-----------------------------------------------------------------------------------

    // Hitta kund som matchar pNo
    private Customer findCustomer(String pNo) {
        for (Customer c : customerList) {
            if (c.getPNo().equals(pNo)) {
                return c;
            }
        }
        return null;
    }

    // Hitta alla konton till en kund mha pNo
    private Account findAccount(String pNo, int accountId) {
        // Kolla om kundens pNo finns i banken
        if (findCustomer(pNo) == null) {
            return null;
        }
        // Kolla om kontot accountId stämmer med pNo
        for (Account a : accountList) {
            if (a.getAccountNumber() == accountId && a.getOwnerPno().equals(pNo)) {
                return a;
            }
        }
        return null; // inget konto hittades
    }

    // Formatera beloppet till svensk layout och returnera string med kr
    private String formatBalance(BigDecimal balance) {
        return NumberFormat.getCurrencyInstance(LOCALE_SV).format(balance);
    }

    // Formatera ränta till svensk layout och returnera string med %
    private String formatInterest(BigDecimal interestRate) {
        NumberFormat percentFormat = NumberFormat.getPercentInstance(LOCALE_SV);
        percentFormat.setMaximumFractionDigits(1);
        BigDecimal interestRateDecimal = interestRate.divide(ONE_HUNDRED_BIG_D);
        return percentFormat.format(interestRateDecimal);
    }

    // Gör om en float till BigDecimal
    private BigDecimal convertBigD(float value) {
        BigDecimal valueAsBigD = new BigDecimal(value);
        return valueAsBigD;
    }


}
