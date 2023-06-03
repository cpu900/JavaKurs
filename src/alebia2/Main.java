/**
 * Inlämningsuppgift 4
 * Kurs: D0018D
 * Datum: 2023-06-03
 * Version: 4
 * @author Alexander Bianca Tofer, alebia-2
 */


package alebia2;

import java.io.FileNotFoundException;
import java.util.Random;

import TestBank.*;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        BankLogic bankLogic = new BankLogic(); // Create an instance of BankLogic

        // lägg till ngra kunder
        bankLogic.createCustomer("Alexander", "Tofer", "8");
        bankLogic.createCustomer("Emma", "Johnson", "77777");
        bankLogic.createCustomer("David", "Ivre", "24680");
        bankLogic.createCustomer("Bo", "Svensson", "12345");
        bankLogic.createCustomer("Maja", "Näslund", "99999");

        // lägg till ngra konton
        for (int i = 0; i < 5; i++) {
            bankLogic.createSavingsAccount("8");
        }
        for (int i = 0; i < 2; i++) {
            bankLogic.createCreditAccount("12345");
            bankLogic.createCreditAccount("8");
            bankLogic.createSavingsAccount("99999");
        }

        // sätt in lite pengar
        Random random = new Random();
        float minAmount = 100;
        float maxAmount = 1000;
        for (int accountNumber = 1001; accountNumber <= 1005; accountNumber++) {
            double amount = minAmount + (maxAmount - minAmount) * random.nextDouble();
            bankLogic.deposit("8", accountNumber, (float) amount);
        }


        // Ladda in GUI med instanss av bankLogic
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                GUI gui = new GUI(bankLogic);
            }
        });
    }

}