import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Pattern;

class Contact {
    int id;
    String name;
    String phone;
    String email;

    Contact(int id, String name, String phone, String email) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    void display() {
        System.out.println("ID    : " + id);
        System.out.println("Name  : " + name);
        System.out.println("Phone : " + phone);
        System.out.println("Email : " + email);
        System.out.println("----------------------");
    }

    // for saving to file
    String toFileLine() {
        return id + "|" + name + "|" + phone + "|" + email;
    }

    static Contact fromFileLine(String line) {
        String[] parts = line.split("\\|", -1);
        return new Contact(Integer.parseInt(parts[0]), parts[1], parts[2], parts[3]);
    }
}

public class ContactManagementSystem {

    static ArrayList<Contact> contacts = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);
    static int nextId = 1;
    static final String FILE_NAME = "contacts.txt";

    // Basic email pattern: something@something.something
    static final Pattern EMAIL_PATTERN = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

    // ---------- Validation helpers ----------

    static String readNonEmpty(String prompt) {
        String value;
        while (true) {
            System.out.print(prompt);
            value = sc.nextLine().trim();
            if (!value.isEmpty()) {
                return value;
            }
            System.out.println("This field cannot be empty. Please try again.");
        }
    }

    static String readPhone(String prompt) {
        String value;
        while (true) {
            System.out.print(prompt);
            value = sc.nextLine().trim();
            if (value.matches("\\d{7,15}")) {
                return value;
            }
            System.out.println("Invalid phone number. Enter 7-15 digits, no spaces or symbols.");
        }
    }

    static String readEmail(String prompt) {
        String value;
        while (true) {
            System.out.print(prompt);
            value = sc.nextLine().trim();
            if (EMAIL_PATTERN.matcher(value).matches()) {
                return value;
            }
            System.out.println("Invalid email format. Example: name@example.com");
        }
    }

    // Reads a menu choice safely; never crashes on bad input.
    static int readMenuChoice() {
        while (true) {
            System.out.print("Enter your choice: ");
            String line = sc.nextLine().trim();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number (1-6).");
            }
        }
    }

    // ---------- Core operations ----------

    static void addContact() {
        String name = readNonEmpty("Enter name: ");
        String phone = readPhone("Enter phone number: ");
        String email = readEmail("Enter email: ");

        contacts.add(new Contact(nextId++, name, phone, email));
        saveContacts();

        System.out.println("Contact added successfully!");
    }

    static void viewContacts() {
        if (contacts.isEmpty()) {
            System.out.println("No contacts found.");
            return;
        }

        System.out.println("\n--- All Contacts ---");
        for (Contact c : contacts) {
            c.display();
        }
    }

    static void searchContact() {
        String name = readNonEmpty("Enter name to search: ");
        boolean found = false;

        for (Contact c : contacts) {
            if (c.name.equalsIgnoreCase(name)) {
                c.display();
                found = true;
            }
        }

        if (!found) {
            System.out.println("Contact not found.");
        }
    }

    // Update by ID, so duplicate names are no longer ambiguous.
    static void updateContact() {
        if (contacts.isEmpty()) {
            System.out.println("No contacts found.");
            return;
        }

        viewContacts();
        int id = readContactId("Enter ID of contact to update: ");
        Contact target = findById(id);

        if (target == null) {
            System.out.println("No contact found with that ID.");
            return;
        }

        System.out.println("Leave a field blank to keep its current value.");

        System.out.print("New name (" + target.name + "): ");
        String name = sc.nextLine().trim();
        if (!name.isEmpty()) target.name = name;

        System.out.print("New phone (" + target.phone + "): ");
        String phone = sc.nextLine().trim();
        if (!phone.isEmpty()) {
            if (phone.matches("\\d{7,15}")) {
                target.phone = phone;
            } else {
                System.out.println("Invalid phone entered, keeping previous value.");
            }
        }

        System.out.print("New email (" + target.email + "): ");
        String email = sc.nextLine().trim();
        if (!email.isEmpty()) {
            if (EMAIL_PATTERN.matcher(email).matches()) {
                target.email = email;
            } else {
                System.out.println("Invalid email entered, keeping previous value.");
            }
        }

        saveContacts();
        System.out.println("Contact updated successfully!");
    }

    // Delete by ID, so it always removes the intended contact
    // (previously deleted only the first name match).
    static void deleteContact() {
        if (contacts.isEmpty()) {
            System.out.println("No contacts found.");
            return;
        }

        viewContacts();
        int id = readContactId("Enter ID of contact to delete: ");
        Contact target = findById(id);

        if (target == null) {
            System.out.println("No contact found with that ID.");
            return;
        }

        contacts.remove(target);
        saveContacts();
        System.out.println("Contact deleted successfully!");
    }

    static Contact findById(int id) {
        for (Contact c : contacts) {
            if (c.id == id) {
                return c;
            }
        }
        return null;
    }

    static int readContactId(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = sc.nextLine().trim();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid numeric ID.");
            }
        }
    }

    // ---------- Persistence (fixes contacts being lost on exit) ----------

    static void saveContacts() {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(FILE_NAME))) {
            for (Contact c : contacts) {
                writer.write(c.toFileLine());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Warning: could not save contacts to file (" + e.getMessage() + ")");
        }
    }

    static void loadContacts() {
        Path path = Paths.get(FILE_NAME);
        if (!Files.exists(path)) {
            return;
        }
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;
                Contact c = Contact.fromFileLine(line);
                contacts.add(c);
                if (c.id >= nextId) {
                    nextId = c.id + 1;
                }
            }
        } catch (IOException e) {
            System.out.println("Warning: could not load contacts from file (" + e.getMessage() + ")");
        }
    }

    public static void main(String[] args) {

        loadContacts();

        while (true) {

            System.out.println("\n===== CONTACT MANAGEMENT SYSTEM =====");
            System.out.println("1. Add Contact");
            System.out.println("2. View Contacts");
            System.out.println("3. Search Contact");
            System.out.println("4. Update Contact");
            System.out.println("5. Delete Contact");
            System.out.println("6. Exit");

            int choice = readMenuChoice();

            switch (choice) {

                case 1:
                    addContact();
                    break;

                case 2:
                    viewContacts();
                    break;

                case 3:
                    searchContact();
                    break;

                case 4:
                    updateContact();
                    break;

                case 5:
                    deleteContact();
                    break;

                case 6:
                    System.out.println("Thank you for using Contact Management System!");
                    sc.close();
                    return;

                default:
                    System.out.println("Invalid choice! Try again.");
            }
        }
    }
}
