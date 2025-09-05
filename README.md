# Contact Manager Application

A simple Java-based Contact Manager application with a graphical user interface.

## Features

- Add, edit, and delete contacts
- Search contacts by name or phone number
- Mark contacts as favorites
- Export/Import contacts to/from CSV files
- Modern and user-friendly interface

## Required Fields
- Name (required)
- Phone Number (required)
- Email (required)
- Address (optional)

## Project Structure

- `Contact.java` - Contact class that stores contact information
- `ContactManager.java` - Manages the collection of contacts
- `ContactManagerGUI.java` - Main application with graphical user interface

## How to Run

1. Make sure you have Java installed on your system
2. Compile the Java files:
   ```
   javac Contact.java ContactManager.java ContactManagerGUI.java
   ```
3. Run the application:
   ```
   java ContactManagerGUI
   ```

## Usage

1. **Adding a Contact**
   - Fill in the required fields (Name, Phone, Email)
   - Click "Add Contact"

2. **Editing a Contact**
   - Select a contact from the table
   - Modify the details in the form
   - Click "Edit Contact"

3. **Deleting a Contact**
   - Select a contact from the table
   - Click "Delete Contact"

4. **Searching Contacts**
   - Use the search field at the top
   - Results will update automatically

5. **Marking Favorites**
   - Use the checkbox in the table to mark/unmark favorites

6. **Export/Import**
   - Use the Export button to save contacts to a CSV file
   - Use the Import button to load contacts from a CSV file

## Note
The application stores contacts in memory only. Contacts will be lost when the application is closed. 