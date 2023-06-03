/**
 * Inlämningsuppgift 4
 * Kurs: D0018D
 * Datum: 2023-06-03
 * Version: 4
 * @author Alexander Bianca Tofer, alebia-2
 * IDE: IntelliJ IDEA 2023.1.2 (Community Edition), Open JDK 20.0.1
 */


package alebia2;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static alebia2.GUI.PATH_TO_FILES;

public class FileManager {
    public static final String BANK_FILE = "Bank.data";


    // Spara en data fil med bank objekt
    public static int saveBankObject(BankLogic bank) {
        try {
            FileOutputStream fileOut = new FileOutputStream(PATH_TO_FILES + BANK_FILE);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);

            // Spara ner konto räknare först
            objectOut.writeInt(BankLogic.getBankAccountsCounter());

            // Spara bank objektet
            objectOut.writeObject(bank);

            objectOut.close();
            fileOut.close();
            return 0; // Return code 0 det gick bra
        } catch (IOException e) {
            e.printStackTrace();
            return 9; // Return code 9 för IO fel
        }
    }

    // Ladda in en fil med bank data
    public static BankLogic loadBankObject(File file) {
        try {
            FileInputStream fileIn = new FileInputStream(file);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);

            // Läs in konto räknare
            int bankAccountsCounter = objectIn.readInt();
            BankLogic.setBankAccountsCounter(bankAccountsCounter);

            // läs in bank objekt
            BankLogic loadedBank = (BankLogic) objectIn.readObject();

            objectIn.close();
            fileIn.close();
            return loadedBank;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean saveTransactionsToFile(String pNo, int accountId, String[][] transactionsData) {
        String fileName = PATH_TO_FILES + pNo + "-" + accountId + ".txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {

            // Skriv ut header först
            writer.write("Account: " + accountId);
            writer.newLine();
            writer.write("===============================================");
            writer.newLine();

            // Skriv ut lagrade transaktioner för kontot
            for (String[] transaction : transactionsData) {
                for (String field : transaction) {
                    writer.write(field + " ");
                }
                writer.newLine();
            }

            // Skriv ut footer mad datum
            writer.write("===============================================");
            writer.newLine();
            LocalDateTime currentDateTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDateTime = currentDateTime.format(formatter);
            writer.write("Date saved to file: " + formattedDateTime);

            return true; // File saved successfully
        } catch (IOException e) {
            e.printStackTrace();
            return false; // Error saving file
        }
    }

}
