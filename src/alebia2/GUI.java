/**
 * Inlämningsuppgift 4
 * Kurs: D0018D
 * Datum: 2023-06-03
 * Version: 4
 * @author Alexander Bianca Tofer, alebia-2
 * IDE: IntelliJ IDEA 2023.1.2 (Community Edition), Open JDK 20.0.1
 */

package alebia2;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;

public class GUI extends JFrame {

    // bankLogic
    private BankLogic bankLogic;

    // Sökväg till lokala filer
    public static final String PATH_TO_FILES = System.getProperty("user.dir") + "/src/alebia2_files/"; // sökväg för att spara alla filer

    //Lista med konton som laddats in
    private ArrayList<String> customerAccList;

    //Komihåg Personummer som laddats
    private String pNo;


    // Paneler
    private JPanel customerPanel;
    private JPanel accountsPanel;
    private JPanel atmPanel;
    private JPanel backOfficePanel;
    private JPanel aboutPanel;


    // Tabeller
    private JTable transactionsTable;
    private JTable customersTable;
    private DefaultTableModel tableModel;


    // Combo box konton
    private JComboBox<String> accountsComboBox;
    private JComboBox<String> transactionsAccountsComboBox;



    // Skapa fönster för GUI med paneler
    public GUI(BankLogic bankLogic) {

        // instans av Banklogic att använda
        this.bankLogic = bankLogic;

        // fönster
        setTitle("BANK in Java D0018D by Alex");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Avsluta om fönster stängs
        setLayout(new BorderLayout()); // Använd BorderLayout

        // meny
        createMenu();

        // paneler
        createCustomerPanel();
        createAtmPanel();
        createAccountsPanel();
        createBackOfficePanel();
        createAboutPanel();

        // Text på paneler
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Customer", customerPanel);
        tabbedPane.addTab("ATM", atmPanel);
        tabbedPane.addTab("Accounts", accountsPanel);
        tabbedPane.addTab("Backoffice", backOfficePanel);
        tabbedPane.addTab("About", aboutPanel);
        add(tabbedPane, BorderLayout.CENTER);

        // inställningar för fönster

        // pack(); // ställ in auto size på fönster
        setSize(500, 400); // ev. om manuell storlek
        setLocationRelativeTo(null); // ställ in center på skärm
        setVisible(true);
    }


    //-----------------------------------------------------------------------------------
    // Customer Panel - login, logout, skapa ny kund, ändra namn
    // Logic - getCustomer, createCustomer, changeCustomerName
    //-----------------------------------------------------------------------------------
    private void createCustomerPanel() {
        customerPanel = new JPanel();
        customerPanel.setLayout(new BorderLayout());

        // Text boxar
        JTextField pNoTextField = new JTextField(12);
        JTextField nameTextField = new JTextField(12);
        JTextField surnameTextField = new JTextField(12);

        // Knappar
        JButton loginButton = new JButton("Login");
        JButton createCustomerButton = new JButton("Create new customer");

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Rita upp grid
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(new JLabel("Personal Number:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        inputPanel.add(pNoTextField, gbc);

        gbc.gridx = 2;
        gbc.gridy = 0;
        inputPanel.add(loginButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(new JLabel("Name:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        inputPanel.add(nameTextField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        inputPanel.add(new JLabel("Surname:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        inputPanel.add(surnameTextField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridwidth = 3;
        inputPanel.add(createCustomerButton, gbc);

        customerPanel.setLayout(new BorderLayout());
        customerPanel.add(inputPanel, BorderLayout.CENTER);

        // Ladda in bild att använda
        final String[] bgImage = {"lock.png"};
        ImageIcon lockIcon = new ImageIcon(PATH_TO_FILES + bgImage[0]);
        JLabel lockLabel = new JLabel(lockIcon);
        lockLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        customerPanel.add(lockLabel, BorderLayout.SOUTH);

        // LOGIN - Knapp
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // starta login här
                if (loginButton.getText().equals("Login")) {
                    pNo = pNoTextField.getText();
                    customerAccList = bankLogic.getCustomer(pNo); // Hämta konto lista för pNo
                    if (customerAccList == null) {
                        // Ingen kunddata fanns
                        JOptionPane.showMessageDialog(null, "Hello " + pNo + "\nYou are not a customer in this bank.\n", "Login Failed", JOptionPane.ERROR_MESSAGE);
                    } else {
                        // kunddata ok
                        StringBuilder message = new StringBuilder();
                        for (String info : customerAccList) {
                            message.append(info).append("\n"); // skriv ut meddeland från banklogic
                        }
                        JOptionPane.showMessageDialog(null, "Welcome to the Bank!\n" + message.toString(), "Login Successful", JOptionPane.INFORMATION_MESSAGE);

                        String[] customerInfo = customerAccList.get(0).split(" ");
                        if (customerInfo.length >= 3) {
                            nameTextField.setText(customerInfo[1]);
                            surnameTextField.setText(customerInfo[2]);
                        }
                        refreshAccountsComboBox(); // uppdatera alla konton
                        pNoTextField.setEditable(false); // lås pno fältet
                        loginButton.setText("Logout"); // Ändra knapp till "Logout"
                        bgImage[0] = "ok.png"; // Ändra login bild
                        createCustomerButton.setText("Update customer name"); // ändra till uppdatera info
                    }
                } else { // Kunden loggar ut här
                    pNo = null; // rensa pno
                    bgImage[0] = "lock.png"; // ändra bild
                    pNoTextField.setText(""); // rensa fält
                    nameTextField.setText("");
                    surnameTextField.setText("");
                    pNoTextField.setEditable(true); // ta in ny text igen
                    loginButton.setText("Login"); // ändra knapp
                    createCustomerButton.setText("Create new customer");
                    refreshAccountsComboBox(); // uppdatera konton = 0
                    refreshTransactionsTable("0",0);
                }
                // rita om bild
                ImageIcon lockIcon = new ImageIcon(PATH_TO_FILES + bgImage[0]);
                lockLabel.setIcon(lockIcon);
            }
        });

        // Create Customer - knapp
        createCustomerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameTextField.getText();
                String surname = surnameTextField.getText();
                pNo = pNoTextField.getText();

                // Kolla om alla fält är ifyllda
                if (pNo.isEmpty() || name.isEmpty() || surname.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please fill in all fields!\n", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Skapa en ny kund
                if (createCustomerButton.getText().equals("Create new customer")) {
                    boolean created = bankLogic.createCustomer(name, surname, pNo); // Skicka till banklogic
                    if (created) {
                        JOptionPane.showMessageDialog(null, "Customer created successfully!\n" + pNo + " " + name + " " + surname + "\nTry to login to the bank now.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Error: Customer already exists.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else if (createCustomerButton.getText().equals("Update customer name")) {
                    // Ändra namn på kund
                    boolean updated = bankLogic.changeCustomerName(name, surname, pNo);
                    if (updated) {
                        JOptionPane.showMessageDialog(null, "Customer name updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Error: Customer not found.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                // Updatera kund listan
                refreshCustomerTable();
            }
        });
    }



    //-----------------------------------------------------------------------------------
    // ATM panel sätta in & ta utt pengar
    // Logic - withdraw, deposit
    //-----------------------------------------------------------------------------------
    private void createAtmPanel() {
        atmPanel = new JPanel();
        atmPanel.setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());

        // ComboBox
        transactionsAccountsComboBox = new JComboBox<>();
        topPanel.add(transactionsAccountsComboBox, BorderLayout.NORTH);

        // TextField
        JTextField amountField = new JTextField();
        amountField.setText("100");
        amountField.setFont(amountField.getFont().deriveFont(22f));
        amountField.setHorizontalAlignment(SwingConstants.CENTER);
        atmPanel.add(amountField, BorderLayout.CENTER);

        // endast siffror kan matas in
        amountField.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) && c != '.') {
                    e.consume(); // ta bort annat som matas in
                }
            }
        });

        // Withdraw knapp
        JButton withdrawButton = new JButton("Withdraw amount");
        withdrawButton.setBackground(Color.PINK);
        withdrawButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedAccount = (String) transactionsAccountsComboBox.getSelectedItem();
                if (selectedAccount != null) {
                    String[] accountInfo = selectedAccount.split(" ");
                    int accountId = Integer.parseInt(accountInfo[0]);
                    float amount = Float.parseFloat(amountField.getText());
                    boolean withdrawSuccess = bankLogic.withdraw(pNo, accountId, amount); // skicka till banklogic här
                    if (withdrawSuccess) {
                        JOptionPane.showMessageDialog(null, "Withdrawal successful.\n" + amount + " from account " + accountId, "Success", JOptionPane.INFORMATION_MESSAGE);
                        refreshAccountsComboBox();
                    } else {
                        JOptionPane.showMessageDialog(null, "Error: Unable to withdraw.\nCheck your account.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        atmPanel.add(withdrawButton, BorderLayout.WEST);

        // Deposit Button
        JButton depositButton = new JButton("Deposit amount");
        depositButton.setBackground(Color.GREEN);
        depositButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedAccount = (String) transactionsAccountsComboBox.getSelectedItem();
                if (selectedAccount != null) {
                    String[] accountInfo = selectedAccount.split(" ");
                    int accountId = Integer.parseInt(accountInfo[0]);
                    float amount = Float.parseFloat(amountField.getText());
                    boolean depositSuccess = bankLogic.deposit(pNo, accountId, amount);
                    if (depositSuccess) {
                        JOptionPane.showMessageDialog(null, "Deposit successful.\n" + amount + " to account " + accountId, "Success", JOptionPane.INFORMATION_MESSAGE);
                        refreshAccountsComboBox();
                    } else {
                        JOptionPane.showMessageDialog(null, "Error: Unable to deposit.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        atmPanel.add(depositButton, BorderLayout.EAST);

        atmPanel.add(topPanel, BorderLayout.NORTH);
    }


    //-----------------------------------------------------------------------------------
    // Accounts panel - hantera konton
    // Logic - closeAccount, createSavingsAccount, createCreditAccount
    //-----------------------------------------------------------------------------------
    private void createAccountsPanel() {
        accountsPanel = new JPanel();
        accountsPanel.setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());

        // combobox med alla konton
        accountsComboBox = new JComboBox<>();
        accountsComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedAccount = (String) accountsComboBox.getSelectedItem();
                if (selectedAccount != null) {
                    // Hämta in konto nummer format = 1001 222,3kr Sparkonto 1,2%
                    String[] accountInfo = selectedAccount.split(" "); // dela upp med mellanslag
                    int accountId = Integer.parseInt(accountInfo[0]); // konto nummer är första index
                    // ladda in nya transaktioner för valt konto
                    refreshTransactionsTable(pNo, accountId);
                }
            }
        });
        topPanel.add(accountsComboBox, BorderLayout.CENTER);

        // Avsluta ett konto
        JButton closeAccountButton = new JButton("Close Account");
        closeAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedAccount = (String) accountsComboBox.getSelectedItem();
                if (selectedAccount != null) {
                    String[] accountInfo = selectedAccount.split(" ");
                    int accountId = Integer.parseInt(accountInfo[0]);
                    // skicka konto till banklogic med pno
                    String closedAccount = bankLogic.closeAccount(pNo, accountId);
                    if (closedAccount != null) {
                        // Dela upp string som returneras av banklogic i format '1001 311,20_kr Sparkonto 3,74_kr'
                        String[] accountClosedInfo = closedAccount.split(" ");
                        String accountName = accountClosedInfo[2]; // tredje är kontotypen

                        // Gör om till float och beräkna summan
                        float initialAmount = Float.parseFloat(accountClosedInfo[1].replaceAll("[^0-9.,]+", "").replace(",", "."));
                        float closingAmount = Float.parseFloat(accountClosedInfo[3].replaceAll("[^0-9.,]+", "").replace(",", "."));
                        float sum = initialAmount + closingAmount;

                        JOptionPane.showMessageDialog(null, "Account closed successfully:\n" + accountClosedInfo[0] + " " + accountName +
                                "\nCash to be returned: " + sum + " kr", "Success", JOptionPane.INFORMATION_MESSAGE);
                        refreshAccountsComboBox();
                    } else {
                        // Något har gått fel här
                        JOptionPane.showMessageDialog(null, "Error: Account not found or does not belong to the customer.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        topPanel.add(closeAccountButton, BorderLayout.EAST);

        // Spara till fil
        JButton saveAccountToFileButton = new JButton("Save records to file");
        saveAccountToFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedAccount = (String) accountsComboBox.getSelectedItem();
                if (selectedAccount != null) {
                    // Get the account number
                    String[] accountInfo = selectedAccount.split(" ");
                    int accountId = Integer.parseInt(accountInfo[0]);

                    // Get the transactions data from the table
                    int rowCount = transactionsTable.getRowCount();
                    int columnCount = transactionsTable.getColumnCount();
                    String[][] transactionsData = new String[rowCount][columnCount];
                    for (int row = 0; row < rowCount; row++) {
                        for (int col = 0; col < columnCount; col++) {
                            transactionsData[row][col] = transactionsTable.getValueAt(row, col).toString();
                        }
                    }

                    // Save the transactions to file
                    boolean saved = FileManager.saveTransactionsToFile(pNo, accountId, transactionsData);

                    if (saved) {
                        JOptionPane.showMessageDialog(null, "Account saved to file: " + PATH_TO_FILES + pNo + "-" + accountId + ".txt", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Error: Account not found or unable to save.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        topPanel.add(saveAccountToFileButton, BorderLayout.WEST);

        accountsPanel.add(topPanel, BorderLayout.NORTH);


        // Create the transactions table
        transactionsTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(transactionsTable);
        accountsPanel.add(scrollPane, BorderLayout.CENTER);

        // Öppna Spar konto
        JButton openSavingsAccountButton = new JButton("Open Savings Account");
        openSavingsAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int accountNo = bankLogic.createSavingsAccount(pNo);
                if (accountNo != -1) {
                    JOptionPane.showMessageDialog(null, "New savings account created. Account number: " + accountNo, "Success", JOptionPane.INFORMATION_MESSAGE);
                    // ladda om listan med konto
                    refreshAccountsComboBox();
                } else {
                    JOptionPane.showMessageDialog(null, "Error: Customer not found.", "Error", JOptionPane.ERROR_MESSAGE);
                }


            }
        });

        // Öppna kredit konto
        JButton openCreditAccountButton = new JButton("Open Credit Account");
        openCreditAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int accountNo = bankLogic.createCreditAccount(pNo);
                if (accountNo != -1) {
                    JOptionPane.showMessageDialog(null, "New credit account created. Account number: " + accountNo, "Success", JOptionPane.INFORMATION_MESSAGE);
                    // ladda om listan med konto
                    refreshAccountsComboBox();
                } else {
                    JOptionPane.showMessageDialog(null, "Error: Customer not found.", "Error", JOptionPane.ERROR_MESSAGE);
                }


            }
        });


        JPanel buttonPanel = new JPanel();
        buttonPanel.add(openSavingsAccountButton);
        buttonPanel.add(openCreditAccountButton);
        accountsPanel.add(buttonPanel, BorderLayout.SOUTH);
    }


    //-----------------------------------------------------------------------------------
    // Backoffice panel - Hantera kunder
    // Logic - getAllCustomers, deleteCustomer
    //-----------------------------------------------------------------------------------

    private void createBackOfficePanel() {
        backOfficePanel = new JPanel();
        backOfficePanel.setLayout(new BorderLayout());

        // Skapa en ny tabellmodell med 3 kolumner
        tableModel = new DefaultTableModel();
        tableModel.addColumn("Personal Number");
        tableModel.addColumn("Name");
        tableModel.addColumn("Surname");
        customersTable = new JTable(tableModel);

        // Ladda in alla kunder
        refreshCustomerTable();

        // Skapa en scrollpanel med tabellen
        JScrollPane scrollPane = new JScrollPane(customersTable);
        backOfficePanel.add(scrollPane, BorderLayout.CENTER);

        // Skapa en knapp för att ta bort en kund
        JButton deleteButton = new JButton("Delete Customer");
        backOfficePanel.add(deleteButton, BorderLayout.SOUTH);

        // Action för knappen
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Hämta den markerade radens index
                int selectedRowIndex = customersTable.getSelectedRow();
                if (selectedRowIndex >= 0) {
                    // Läs in pno från den markerade raden
                    String pNo = tableModel.getValueAt(selectedRowIndex, 0).toString();

                    // Anropa deleteCustomer-metoden i bankLogic
                    ArrayList<String> deletedAccountsList = bankLogic.deleteCustomer(pNo);

                    if (deletedAccountsList != null) {
                        // Ta bort raden från tabellen
                        tableModel.removeRow(selectedRowIndex);

                        // Meddelande om borttagning
                        // Kan formateras om vid behov
                        StringBuilder message = new StringBuilder();
                        for (String accountInfo : deletedAccountsList) {
                            message.append(accountInfo).append("\n");
                        }
                        JOptionPane.showMessageDialog(null, message.toString(), "Customer Deleted", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Error: Customer not found.", "Deletion Failed", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    // Ingen kund är markerad i tabellen
                    JOptionPane.showMessageDialog(null, "Please select a customer to delete.", "No Customer Selected", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
    }




    //-----------------------------------------------------------------------------------
    // About panel
    //-----------------------------------------------------------------------------------
    private void createAboutPanel() {
        aboutPanel = new JPanel();
        aboutPanel.setLayout(new BorderLayout());

        // Text om programet
        JLabel programLabel = new JLabel("<html><center>BANK in Java v4.0<br><br>Course D0018D @ LTU<br><br>© Alexander Bianca Tofer, alebia-2<br><br>Images from www.freepik.com<br><br>Updated 2023-06-03</html>");
        programLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        // Bild laddas lokalt
        ImageIcon backgroundIcon = new ImageIcon(PATH_TO_FILES + "bank.png");
        JLabel backgroundLabel = new JLabel(backgroundIcon);

        // panel för text
        JPanel textPanel = new JPanel(new BorderLayout());
        textPanel.setOpaque(false); // Make the panel transparent
        textPanel.add(programLabel, BorderLayout.EAST);

        // panel för bild
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setOpaque(false); // Make the panel transparent
        imagePanel.add(backgroundLabel, BorderLayout.WEST);

        // lägg till paneler
        aboutPanel.add(textPanel, BorderLayout.CENTER);
        aboutPanel.add(imagePanel, BorderLayout.WEST);

    }


    //-----------------------------------------------------------------------------------
    // Meny system
    //-----------------------------------------------------------------------------------
    private void createMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu dataMenu = new JMenu("File");


        // Skapa en ny tom bank
        JMenuItem newBankMenuItem = new JMenuItem("New Bank");
        newBankMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //Kontroll om någon är inloggad först.
                if (pNo != null && !pNo.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Customer " + pNo + " is logged in.\n\nPlease logout from bank before creating a new bank.\n\n", "New Bank", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Kontroll om det finns kunder som tas bort
                if (customersTable != null && customersTable.getRowCount() > 0) {
                    int option = JOptionPane.showConfirmDialog(null, "Creating a new bank will delete all existing customer data.\n\nAre you sure you want to continue?\n\n", "New Bank", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                    if (option == JOptionPane.NO_OPTION) {
                        return;
                    }
                }

                // Skapa en ny bank med nollställda inställningar.
                bankLogic = new BankLogic();
                BankLogic.setBankAccountsCounter(1001);
                refreshCustomerTable();
                refreshAccountsComboBox();
                refreshTransactionsTable("0",0);
                JOptionPane.showMessageDialog(null, "New empty bank created.", "New Bank", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // läs in ny data för GUI
        JMenuItem loadBankMenuItem = new JMenuItem("Load Bank");
        loadBankMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //Kontroll om någon är inloggad först.
                if (pNo != null && !pNo.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Customer " + pNo + " is logged in.\n\nPlease logout from bank before loading a new bank.\n\n", "Load Bank", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Öppna filväljaren JFileChooser
                JFileChooser fileChooser = new JFileChooser();

                // Starta i rätt sökväg
                File workingDirectory = new File(PATH_TO_FILES);
                fileChooser.setCurrentDirectory(workingDirectory);

                // Filtrerar på filer .data
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Bank Data Files", "data");
                fileChooser.setFileFilter(filter);
                int result = fileChooser.showOpenDialog(null);

                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();

                    bankLogic = FileManager.loadBankObject(selectedFile);
                    if (bankLogic != null) {
                        refreshCustomerTable();
                        refreshAccountsComboBox();
                        refreshTransactionsTable("0",0);
                        JOptionPane.showMessageDialog(null, "Bank has been loaded.", "Load Bank", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        // Gick ej att ladda ladda en tom bank istället.
                        bankLogic = new BankLogic();
                        BankLogic.setBankAccountsCounter(1001);
                        refreshCustomerTable();
                        refreshAccountsComboBox();
                        refreshTransactionsTable("0",0);
                        JOptionPane.showMessageDialog(null, "Error: Failed to load bank.", "Load Bank", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        // Spara all data
        JMenuItem saveMenuItem = new JMenuItem("Save Bank");
        saveMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int saveResult = FileManager.saveBankObject(bankLogic);
                if (saveResult == 0) {
                    JOptionPane.showMessageDialog(null, "Bank has been saved.", "Save Bank", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Error: Failed to save bank.", "Save Bank", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        //lägg till i GUI
        dataMenu.add(newBankMenuItem);
        dataMenu.add(loadBankMenuItem);
        dataMenu.add(saveMenuItem);
        menuBar.add(dataMenu);

        // lägg till meny
        setJMenuBar(menuBar);
    }

    //-----------------------------------------------------------------------------------
    // Privata metoder - uppdatering av listor
    //-----------------------------------------------------------------------------------


    // Uppdatera listor med konton
    private void refreshAccountsComboBox() {
        // Hämta lista på konton
        customerAccList = bankLogic.getCustomer(pNo);

        // Rensa konto listor i combobox
        accountsComboBox.removeAllItems();
        transactionsAccountsComboBox.removeAllItems();

        // ingen kundinfo finns avbryt körningen
        if (customerAccList == null) {
            return;
        }

        // fyll in med ny konto info
        for (int i = 1; i < customerAccList.size(); i++) {
            accountsComboBox.addItem(customerAccList.get(i));
            transactionsAccountsComboBox.addItem(customerAccList.get(i));
        }
    }

    // uppdatera transaktionslistan
    private void refreshTransactionsTable(String pNo, int accountId) {
        // hämta transaktioner från banklogic
        ArrayList<String> transactions = bankLogic.getTransactions(pNo, accountId);

        // Vid args 0+0 skapa enbart en tom tabell
        if (pNo.equals("0") && accountId == 0) {
            transactions = new ArrayList<>(); // Create an empty ArrayList
        } else {
            transactions = bankLogic.getTransactions(pNo, accountId);
        }

        // skapa en tabell
        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("Transactions on account : " + accountId);

        // lägg till tranasktioner
        if (transactions != null && !transactions.isEmpty()) {
            for (String transaction : transactions) {
                tableModel.addRow(new Object[]{transaction});
            }
        } else {
            // inga transaktioner finns
            tableModel.addRow(new Object[]{"No transactions found."});
        }

        // ställ in tableModel
        transactionsTable.setModel(tableModel);
    }

    // Uppdatera alla kunder
    private void refreshCustomerTable() {
        if (tableModel != null) {
            tableModel.setRowCount(0); // Clear the existing table data

            // Load new data with getAllCustomers
            ArrayList<String> allCustomers = bankLogic.getAllCustomers();
            for (String customerInfo : allCustomers) {
                String[] customerData = customerInfo.split(" ");
                tableModel.addRow(customerData);
            }

            // Notify the table that the model has been updated
            tableModel.fireTableDataChanged();
        }
    }


}
