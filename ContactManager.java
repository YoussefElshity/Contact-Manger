import java.util.ArrayList;
import java.util.List;

public class ContactManager {
    private List<Contact> contacts;

    public ContactManager() {
        contacts = new ArrayList<>();
    }

    public void addContact(Contact contact) {
        contacts.add(contact);
    }

    public List<Contact> searchContact(String keyword) {
        List<Contact> results = new ArrayList<>();
        String searchTerm = keyword.toLowerCase();
        
        for (Contact contact : contacts) {
            if (contact.getName().toLowerCase().contains(searchTerm) ||
                contact.getPhoneNumber().contains(searchTerm)) {
                results.add(contact);
            }
        }
        return results;
    }

    public void editContact(Contact oldContact, Contact updatedContact) {
        int index = contacts.indexOf(oldContact);
        if (index != -1) {
            contacts.set(index, updatedContact);
        }
    }

    public void deleteContact(Contact contact) {
        contacts.remove(contact);
    }

    public List<Contact> getAllContacts() {
        return new ArrayList<>(contacts);
    }
} 