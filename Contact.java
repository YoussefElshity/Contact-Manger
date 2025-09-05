import java.util.Objects;

public class Contact {
    private String name;
    private String phoneNumber;
    private String email;
    private String address;

    public Contact(String name, String phoneNumber, String email, String address) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contact contact = (Contact) o;
        return Objects.equals(name, contact.name) &&
               Objects.equals(phoneNumber, contact.phoneNumber) &&
               Objects.equals(email, contact.email) &&
               Objects.equals(address, contact.address);
    }


    public int hashCode() {
        return Objects.hash(name, phoneNumber, email, address);
    }
    public String toString() {
        return name + " - " + phoneNumber;
    }
} 