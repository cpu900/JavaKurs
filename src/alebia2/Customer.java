/**
 * Inlämningsuppgift 2
 * Kurs: D0018D
 * Datum: 2023-02-24
 * Version: 2
 * @author Alexander Bianca Tofer, alebia-2
 */

package alebia2;

import java.io.Serializable;

public class Customer implements Serializable {

    //-----------------------------------------------------------------------------------
    // Variables
    //-----------------------------------------------------------------------------------

    private String pNo;
    private String firstName;
    private String lastName;

    //-----------------------------------------------------------------------------------
    // Getters & Setters
    //-----------------------------------------------------------------------------------

    public String getPNo() {
        return pNo;
    }

    private void setPNo(String pNo) {
        this.pNo = pNo;
    }

    public String getFirstName() {
        return firstName;
    }

    private void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    private void setLastName(String lastName) {
        this.lastName = lastName;
    }

    //-----------------------------------------------------------------------------------
    // Public Methods
    //-----------------------------------------------------------------------------------

    // Skapar en kund
    public Customer(String firstName, String lastName, String pNo) {
        setFirstName(firstName);
        setLastName(lastName);
        setPNo(pNo);
    }

    // För ändring av namn
    public boolean changeName(String firstName, String lastName) {
        // Kontroll om båda fält är tomma
        if (firstName.isEmpty() && lastName.isEmpty()) {
            return false;
        }
        // Ändra förnamn
        if (!firstName.isEmpty()) {
            setFirstName(firstName);
        }
        // Ändra efternamn
        if (!lastName.isEmpty()) {
            setLastName(lastName);
        }
        return true;
    }
}
