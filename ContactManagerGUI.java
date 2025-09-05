import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;
import java.io.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.*;

public class ContactManagerGUI extends JFrame {
    private ContactManager contactManager;
    private JTable contactTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JTextField nameField, phoneField, emailField, addressField;
    private Contact selectedContact;
    private boolean isFavorite = false;

    // Enhanced color scheme
    private static final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private static final Color SECONDARY_COLOR = new Color(52, 152, 219);
    private static final Color BACKGROUND_COLOR = new Color(240, 242, 245);
    private static final Color TEXT_COLOR = new Color(44, 62, 80);
    private static final Color BUTTON_COLOR = new Color(52, 152, 219);
    private static final Color ACCENT_COLOR = new Color(231, 76, 60);
    private static final Color SUCCESS_COLOR = new Color(46, 204, 113);
    private static final Color PANEL_BACKGROUND = new Color(255, 255, 255);
    private static final Color BORDER_COLOR = new Color(218, 220, 224);
    private static final Color FAVORITE_COLOR = new Color(255, 193, 7);
    private static final Color HOVER_COLOR = new Color(41, 128, 185, 20);
    private static final Color SHADOW_COLOR = new Color(0, 0, 0, 30);

    public ContactManagerGUI() {
        contactManager = new ContactManager();
        initializeGUI();
        loadContacts();
    }

    private void initializeGUI() {
        setTitle("CALL ME EL-SHITY");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setBackground(BACKGROUND_COLOR);

        // Set modern look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Create main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Create and add components
        JPanel topPanel = createTopPanel();
        JPanel centerPanel = createCenterPanel();
        JPanel buttonPanel = createButtonPanel();

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(BACKGROUND_COLOR);

        // Create logo and title panel
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        titlePanel.setBackground(BACKGROUND_COLOR);

        // Create logo panel with custom phone icon
        JPanel logoPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Draw phone body
                g2d.setColor(PRIMARY_COLOR);
                g2d.fillRoundRect(5, 5, 40, 70, 10, 10);

                // Draw screen
                g2d.setColor(Color.WHITE);
                g2d.fillRoundRect(10, 10, 30, 40, 5, 5);

                // Draw button
                g2d.setColor(PRIMARY_COLOR);
                g2d.fillOval(20, 55, 10, 10);
            }
        };
        logoPanel.setPreferredSize(new Dimension(50, 80));
        logoPanel.setBackground(BACKGROUND_COLOR);

        // Create title label with custom font
        JLabel titleLabel = new JLabel("CALL ME EL-SHITY");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(PRIMARY_COLOR);

        // Add logo and title to panel
        titlePanel.add(logoPanel);
        titlePanel.add(titleLabel);

        // Create search panel with modern styling
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setBackground(BACKGROUND_COLOR);

        // Create a custom search panel with rounded corners
        JPanel searchBox = new JPanel(new BorderLayout(5, 0));
        searchBox.setBackground(Color.WHITE);
        searchBox.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        searchField = createStyledTextField(25);
        searchField.setPreferredSize(new Dimension(250, 35));
        searchField.setBorder(null);

        JButton searchButton = createStyledButton("Search", BUTTON_COLOR);
        searchButton.setPreferredSize(new Dimension(100, 35));
        searchButton.addActionListener(e -> searchContacts());

        searchBox.add(searchField, BorderLayout.CENTER);
        searchBox.add(searchButton, BorderLayout.EAST);

        // Add placeholder text to search field
        searchField.addFocusListener(new FocusAdapter() {

            public void focusGained(FocusEvent e) {
                if (searchField.getText().equals("Search contacts...")) {
                    searchField.setText("");
                    searchField.setForeground(TEXT_COLOR);
                }
            }

            public void focusLost(FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setForeground(Color.GRAY);
                    searchField.setText("Search contacts...");
                }
            }
        });
        searchField.setForeground(Color.GRAY);
        searchField.setText("Search contacts...");

        searchPanel.add(searchBox);

        panel.add(titlePanel, BorderLayout.WEST);
        panel.add(searchPanel, BorderLayout.EAST);

        return panel;
    }

    private JPanel createCenterPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(BACKGROUND_COLOR);

        // Create form panel with shadow effect
        JPanel formPanel = createFormPanel();
        formPanel.setBorder(createPanelBorder("Contact Information"));

        // Create table panel with shadow effect
        createTable();
        JScrollPane scrollPane = new JScrollPane(contactTable);
        scrollPane.setBorder(createPanelBorder("Contact List"));
        scrollPane.getViewport().setBackground(Color.WHITE);

        // Add components
        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(PANEL_BACKGROUND);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Create form fields with modern styling
        nameField = createStyledTextField(30);
        phoneField = createNumericTextField(30);
        emailField = createStyledTextField(30);
        addressField = createStyledTextField(30);

        // Add email validation
        emailField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                validateEmailField();
            }
        });

        // Add fields with labels
        addFormField(panel, "Name:", nameField, gbc, 0);
        addFormField(panel, "Phone:", phoneField, gbc, 1);
        addFormField(panel, "Email:", emailField, gbc, 2);
        addFormField(panel, "Address:", addressField, gbc, 3);

        return panel;
    }

    private JTextField createNumericTextField(int columns) {
        JTextField textField = createStyledTextField(columns);
        PlainDocument doc = (PlainDocument) textField.getDocument();
        doc.setDocumentFilter(new NumericDocumentFilter());
        return textField;
    }

    private class NumericDocumentFilter extends DocumentFilter {

        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            if (string == null) return;
            if (string.matches("\\d*")) {
                super.insertString(fb, offset, string, attr);
            }
        }


        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            if (text == null) return;
            if (text.matches("\\d*")) {
                super.replace(fb, offset, length, text, attrs);
            }
        }
    }

    private void validateEmailField() {
        String email = emailField.getText().trim();
        if (email.isEmpty()) {
            emailField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDER_COLOR),
                    BorderFactory.createEmptyBorder(8, 10, 8, 10)
            ));
        } else if (!email.endsWith("@gmail.com")) {
            emailField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(ACCENT_COLOR),
                    BorderFactory.createEmptyBorder(8, 10, 8, 10)
            ));
        } else {
            emailField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDER_COLOR),
                    BorderFactory.createEmptyBorder(8, 10, 8, 10)
            ));
        }
    }

    private Border createPanelBorder(String title) {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(5, 5, 5, 5),
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDER_COLOR, 1),
                        BorderFactory.createCompoundBorder(
                                BorderFactory.createEmptyBorder(15, 15, 15, 15),
                                BorderFactory.createTitledBorder(
                                        BorderFactory.createEmptyBorder(),
                                        title,
                                        TitledBorder.DEFAULT_JUSTIFICATION,
                                        TitledBorder.DEFAULT_POSITION,
                                        new Font("Segoe UI", Font.BOLD, 16),
                                        PRIMARY_COLOR
                                )
                        )
                )
        );
    }

    private void addFormField(JPanel panel, String labelText, JComponent field, GridBagConstraints gbc, int row) {
        // Add asterisk to required fields (all except address)
        if (!labelText.equals("Address:")) {
            labelText = labelText + " *";
        }
        JLabel label = createStyledLabel(labelText);

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.2;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(label, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.8;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(field, gbc);
    }

    private void createTable() {
        String[] columnNames = {"Favorite", "Name", "Phone", "Email", "Address"};
        tableModel = new DefaultTableModel(columnNames, 0) {

            public boolean isCellEditable(int row, int column) {
                return column == 0;
            }


            public Class<?> getColumnClass(int column) {
                if (column == 0) {
                    return Boolean.class;
                }
                return String.class;
            }


            public void setValueAt(Object value, int row, int column) {
                if (column == 0) {
                    super.setValueAt(Boolean.valueOf(String.valueOf(value)), row, column);
                } else {
                    super.setValueAt(value, row, column);
                }
            }
        };

        contactTable = new JTable(tableModel);

        // Enhanced table styling
        contactTable.setBackground(Color.WHITE);
        contactTable.setForeground(TEXT_COLOR);
        contactTable.setSelectionBackground(new Color(232, 240, 254));
        contactTable.setSelectionForeground(TEXT_COLOR);
        contactTable.setRowHeight(35);
        contactTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        contactTable.setGridColor(BORDER_COLOR);
        contactTable.setShowGrid(true);
        contactTable.setIntercellSpacing(new Dimension(1, 1));

        // Add checkbox renderer for favorite column
        contactTable.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
            private final JCheckBox checkBox = new JCheckBox();
            {
                checkBox.setHorizontalAlignment(SwingConstants.CENTER);
                checkBox.setBackground(Color.WHITE);
            }


            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                checkBox.setSelected(Boolean.TRUE.equals(value));
                checkBox.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
                return checkBox;
            }
        });

        // Style table header
        JTableHeader header = contactTable.getTableHeader();
        header.setBackground(PANEL_BACKGROUND);
        header.setForeground(TEXT_COLOR);
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, BORDER_COLOR));
        header.setPreferredSize(new Dimension(header.getWidth(), 40));

        // Set column widths
        int[] columnWidths = {80, 180, 150, 250, 300};
        for (int i = 0; i < columnWidths.length; i++) {
            contactTable.getColumnModel().getColumn(i).setPreferredWidth(columnWidths[i]);
        }

        // Add selection listener
        contactTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        contactTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = contactTable.getSelectedRow();
                if (selectedRow != -1) {
                    selectedContact = new Contact(
                            (String) tableModel.getValueAt(selectedRow, 1),
                            (String) tableModel.getValueAt(selectedRow, 2),
                            (String) tableModel.getValueAt(selectedRow, 3),
                            (String) tableModel.getValueAt(selectedRow, 4)
                    );
                    isFavorite = (Boolean) tableModel.getValueAt(selectedRow, 0);
                    populateFields(selectedContact);
                }
            }
        });

        // Add row striping
        contactTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value,
                        isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(249, 249, 249));
                }
                setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
                return c;
            }
        });
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JButton addButton = createStyledButton("Add Contact", SUCCESS_COLOR);
        JButton editButton = createStyledButton("Edit Contact", BUTTON_COLOR);
        JButton deleteButton = createStyledButton("Delete Contact", ACCENT_COLOR);
        JButton clearButton = createStyledButton("Clear Fields", new Color(108, 117, 125));
        JButton favoriteButton = createStyledButton("Toggle Favorite", FAVORITE_COLOR);
        JButton exportButton = createStyledButton("Export", BUTTON_COLOR);
        JButton importButton = createStyledButton("Import", BUTTON_COLOR);
        JButton helpButton = createStyledButton("Help", BUTTON_COLOR);

        addButton.addActionListener(e -> addContact());
        editButton.addActionListener(e -> editContact());
        deleteButton.addActionListener(e -> deleteContact());
        clearButton.addActionListener(e -> clearFields());
        favoriteButton.addActionListener(e -> toggleFavorite());
        exportButton.addActionListener(e -> exportContacts());
        importButton.addActionListener(e -> importContacts());
        helpButton.addActionListener(e -> showHelp());

        panel.add(addButton);
        panel.add(editButton);
        panel.add(deleteButton);
        panel.add(clearButton);
        panel.add(favoriteButton);
        panel.add(exportButton);
        panel.add(importButton);
        panel.add(helpButton);

        return panel;
    }

    private JTextField createStyledTextField(int columns) {
        JTextField textField = new JTextField(columns);
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textField.setForeground(TEXT_COLOR);
        textField.setBackground(Color.WHITE);
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));

        // Add focus listener for highlight effect
        textField.addFocusListener(new FocusAdapter() {

            public void focusGained(FocusEvent e) {
                textField.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(PRIMARY_COLOR),
                        BorderFactory.createEmptyBorder(8, 10, 8, 10)
                ));
            }


            public void focusLost(FocusEvent e) {
                textField.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDER_COLOR),
                        BorderFactory.createEmptyBorder(8, 10, 8, 10)
                ));
            }
        });

        return textField;
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(TEXT_COLOR);
        return label;
    }

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Enhanced hover effect
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(color.darker());
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(color);
            }
            public void mousePressed(MouseEvent e) {
                button.setBackground(color.darker().darker());
            }
            public void mouseReleased(MouseEvent e) {
                button.setBackground(color);
            }
        });

        return button;
    }

    private void toggleFavorite() {
        if (selectedContact == null) {
            return;
        }
        isFavorite = !isFavorite;
        int selectedRow = contactTable.getSelectedRow();
        if (selectedRow != -1) {
            tableModel.setValueAt(isFavorite, selectedRow, 0);
        }
    }

    private void exportContacts() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Export Contacts");
        fileChooser.setFileFilter(new FileNameExtensionFilter("CSV Files", "csv"));

        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if (!file.getName().toLowerCase().endsWith(".csv")) {
                file = new File(file.getAbsolutePath() + ".csv");
            }

            try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
                // Write header
                writer.println("Name,Phone,Email,Address,Favorite");

                // Write contacts
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    String name = (String) tableModel.getValueAt(i, 1);
                    String phone = (String) tableModel.getValueAt(i, 2);
                    String email = (String) tableModel.getValueAt(i, 3);
                    String address = (String) tableModel.getValueAt(i, 4);
                    boolean favorite = (Boolean) tableModel.getValueAt(i, 0);

                    writer.println(String.format("%s,%s,%s,%s,%s",
                            escapeCsv(name),
                            escapeCsv(phone),
                            escapeCsv(email),
                            escapeCsv(address),
                            favorite));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void importContacts() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Import Contacts");
        fileChooser.setFileFilter(new FileNameExtensionFilter("CSV Files", "csv"));

        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();

            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                // Skip header
                reader.readLine();

                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = parseCsvLine(line);
                    if (parts.length >= 5) {
                        String name = unescapeCsv(parts[0]);
                        String phone = unescapeCsv(parts[1]);
                        String email = unescapeCsv(parts[2]);
                        String address = unescapeCsv(parts[3]);
                        boolean favorite = Boolean.parseBoolean(parts[4]);

                        Contact contact = new Contact(name, phone, email, address);
                        contactManager.addContact(contact);
                        tableModel.addRow(new Object[]{favorite, name, phone, email, address});
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String escapeCsv(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }

    private String unescapeCsv(String value) {
        if (value == null) return "";
        if (value.startsWith("\"") && value.endsWith("\"")) {
            value = value.substring(1, value.length() - 1);
            value = value.replace("\"\"", "\"");
        }
        return value;
    }

    private String[] parseCsvLine(String line) {
        List<String> result = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder current = new StringBuilder();

        for (char c : line.toCharArray()) {
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                result.add(current.toString());
                current = new StringBuilder();
            } else {
                current.append(c);
            }
        }
        result.add(current.toString());

        return result.toArray(new String[0]);
    }

    private void showHelp() {
        JOptionPane.showMessageDialog(this,
                "Contact Manager Help:\n\n" +
                        "1. Add Contact: Fill in the fields and click 'Add Contact'\n" +
                        "2. Edit Contact: Select a contact, modify fields, click 'Edit Contact'\n" +
                        "3. Delete Contact: Select a contact and click 'Delete Contact'\n" +
                        "4. Search: Type in the search field to filter contacts\n" +
                        "5. Phone: Must contain only numbers\n" +
                        "6. Email: Must end with '@gmail.com'\n" +
                        "7. Export/Import: Save or load contacts from CSV files",
                "Help",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void searchContacts() {
        String keyword = searchField.getText().trim();
        if (keyword.equals("Search contacts...")) {
            keyword = "";
        }
        List<Contact> contacts = contactManager.searchContact(keyword);
        updateTable(contacts);
    }

    private void loadContacts() {
        List<Contact> contacts = contactManager.getAllContacts();
        updateTable(contacts);
    }

    private void updateTable(List<Contact> contacts) {
        tableModel.setRowCount(0);
        for (Contact contact : contacts) {
            tableModel.addRow(new Object[]{
                    false,
                    contact.getName(),
                    contact.getPhoneNumber(),
                    contact.getEmail(),
                    contact.getAddress()
            });
        }
    }

    private void populateFields(Contact contact) {
        nameField.setText(contact.getName());
        phoneField.setText(contact.getPhoneNumber());
        emailField.setText(contact.getEmail());
        addressField.setText(contact.getAddress());
    }

    private void clearFields() {
        nameField.setText("");
        phoneField.setText("");
        emailField.setText("");
        addressField.setText("");
        selectedContact = null;
        contactTable.clearSelection();
    }

    private void addContact() {
        String name = nameField.getText().trim();
        String phone = phoneField.getText().trim();
        String email = emailField.getText().trim();
        String address = addressField.getText().trim();

        if (name.isEmpty() || phone.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Name, Phone and Email are required fields",
                    "Missing Information",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!email.endsWith("@gmail.com")) {
            JOptionPane.showMessageDialog(this,
                    "Email must end with @gmail.com",
                    "Invalid Email",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        Contact contact = new Contact(name, phone, email, address);
        contactManager.addContact(contact);
        tableModel.addRow(new Object[]{false, name, phone, email, address});
        clearFields();
    }

    private void editContact() {
        if (selectedContact == null) {
            return;
        }

        String name = nameField.getText().trim();
        String phone = phoneField.getText().trim();
        String email = emailField.getText().trim();
        String address = addressField.getText().trim();

        if (name.isEmpty() || phone.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Name, Phone and Email are required fields",
                    "Missing Information",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!email.endsWith("@gmail.com")) {
            JOptionPane.showMessageDialog(this,
                    "Email must end with @gmail.com",
                    "Invalid Email",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        Contact updatedContact = new Contact(name, phone, email, address);
        contactManager.editContact(selectedContact, updatedContact);
        loadContacts();
        clearFields();
    }

    private void deleteContact() {
        if (selectedContact == null) {
            return;
        }

        contactManager.deleteContact(selectedContact);
        loadContacts();
        clearFields();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ContactManagerGUI().setVisible(true);
        });
    }
}