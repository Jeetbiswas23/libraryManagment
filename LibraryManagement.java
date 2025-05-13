import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class LibraryManagement {
    private static List<BookRequest> bookRequests = new ArrayList<>();
    private static Library library = new Library();
    private static UserManager userManager = new UserManager();
    private static User currentUser = null;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createMainMenu());
    }

    private static void createMainMenu() {
        JFrame frame = new JFrame("Library Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new GridLayout(5, 1));

        JButton registerButton = new JButton("Register");
        JButton loginButton = new JButton("Login");
        JButton adminPanelButton = new JButton("Admin Panel");
        JButton resetPasswordButton = new JButton("Reset Password");
        JButton exitButton = new JButton("Exit");

        registerButton.addActionListener(e -> handleRegistrationGUI(frame));
        loginButton.addActionListener(e -> handleLoginGUI(frame));
        adminPanelButton.addActionListener(e -> handleAdminPanelGUI(frame));
        resetPasswordButton.addActionListener(e -> handlePasswordResetGUI(frame));
        exitButton.addActionListener(e -> System.exit(0));

        frame.add(registerButton);
        frame.add(loginButton);
        frame.add(adminPanelButton);
        frame.add(resetPasswordButton);
        frame.add(exitButton);

        frame.setVisible(true);
    }

    private static void handleRegistrationGUI(JFrame parentFrame) {
        JFrame frame = new JFrame("Register");
        frame.setSize(300, 200);
        frame.setLayout(new GridLayout(3, 2));

        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();
        JButton registerButton = new JButton("Register");

        registerButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            userManager.registerUser(new User(username, password));
            JOptionPane.showMessageDialog(frame, "Registration successful!");
            frame.dispose();
        });

        frame.add(usernameLabel);
        frame.add(usernameField);
        frame.add(passwordLabel);
        frame.add(passwordField);
        frame.add(new JLabel());
        frame.add(registerButton);

        frame.setVisible(true);
    }

    private static void handleLoginGUI(JFrame parentFrame) {
        JFrame frame = new JFrame("Login");
        frame.setSize(300, 200);
        frame.setLayout(new GridLayout(3, 2));

        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();
        JButton loginButton = new JButton("Login");

        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            currentUser = userManager.loginUser(username, password);
            if (currentUser != null) {
                JOptionPane.showMessageDialog(frame, "Login successful!");
                frame.dispose();
                createLibraryMenu();
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid credentials. Try again.");
            }
        });

        frame.add(usernameLabel);
        frame.add(usernameField);
        frame.add(passwordLabel);
        frame.add(passwordField);
        frame.add(new JLabel());
        frame.add(loginButton);

        frame.setVisible(true);
    }

    private static void handleAdminPanelGUI(JFrame parentFrame) {
        String adminUsername = JOptionPane.showInputDialog("Enter admin username:");
        String adminPassword = JOptionPane.showInputDialog("Enter admin password:");
        if ("admin".equals(adminUsername) && "admin123".equals(adminPassword)) {
            JFrame frame = new JFrame("Admin Panel");
            frame.setSize(400, 300);
            frame.setLayout(new GridLayout(5, 1));

            JButton viewBooksButton = new JButton("View All Books");
            JButton addBookButton = new JButton("Add Book");
            JButton removeBookButton = new JButton("Remove Book");
            JButton viewRequestsButton = new JButton("View Book Requests");
            JButton exitButton = new JButton("Exit");

            viewBooksButton.addActionListener(e -> displayBooksGUI()); // Use the GUI view instead
            addBookButton.addActionListener(e -> {
                String title = JOptionPane.showInputDialog("Enter book title:");
                String author = JOptionPane.showInputDialog("Enter author name:");
                library.addBook(new Book(title, author));
                JOptionPane.showMessageDialog(frame, "Book added successfully!");
            });
            removeBookButton.addActionListener(e -> {
                String title = JOptionPane.showInputDialog("Enter book title to remove:");
                library.removeBook(title);
                JOptionPane.showMessageDialog(frame, "Book removed successfully!");
            });
            viewRequestsButton.addActionListener(e -> handleBookRequestsGUI(frame));
            exitButton.addActionListener(e -> frame.dispose());

            frame.add(viewBooksButton);
            frame.add(addBookButton);
            frame.add(removeBookButton);
            frame.add(viewRequestsButton);
            frame.add(exitButton);

            frame.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(parentFrame, "Invalid admin credentials.");
        }
    }

    private static void handlePasswordResetGUI(JFrame parentFrame) {
        JFrame frame = new JFrame("Reset Password");
        frame.setSize(300, 200);
        frame.setLayout(new GridLayout(4, 2));

        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField();
        JLabel oldPasswordLabel = new JLabel("Old Password:");
        JPasswordField oldPasswordField = new JPasswordField();
        JLabel newPasswordLabel = new JLabel("New Password:");
        JPasswordField newPasswordField = new JPasswordField();
        JButton resetButton = new JButton("Reset");

        resetButton.addActionListener(e -> {
            String username = usernameField.getText();
            String oldPassword = new String(oldPasswordField.getPassword());
            String newPassword = new String(newPasswordField.getPassword());
            userManager.resetPassword(username, oldPassword, newPassword);
            JOptionPane.showMessageDialog(frame, "Password reset successful!");
            frame.dispose();
        });

        frame.add(usernameLabel);
        frame.add(usernameField);
        frame.add(oldPasswordLabel);
        frame.add(oldPasswordField);
        frame.add(newPasswordLabel);
        frame.add(newPasswordField);
        frame.add(new JLabel());
        frame.add(resetButton);

        frame.setVisible(true);
    }

    private static void createLibraryMenu() {
        JFrame frame = new JFrame("Library Menu");
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel(new GridLayout(7, 1));
        JButton displayBooksButton = new JButton("Display All Books");
        JButton searchBookButton = new JButton("Search Book by Title");
        JButton borrowBookButton = new JButton("Borrow Book");
        JButton returnBookButton = new JButton("Return Book");
        JButton viewBorrowedBooksButton = new JButton("View Borrowed Books");
        JButton requestBookButton = new JButton("Request Book Addition");
        JButton logoutButton = new JButton("Logout");

        displayBooksButton.addActionListener(e -> displayBooksGUI());
        searchBookButton.addActionListener(e -> searchBookGUI());
        borrowBookButton.addActionListener(e -> borrowBookGUI());
        returnBookButton.addActionListener(e -> returnBookGUI());
        viewBorrowedBooksButton.addActionListener(e -> viewBorrowedBooksGUI());
        requestBookButton.addActionListener(e -> requestBookGUI());
        logoutButton.addActionListener(e -> {
            currentUser = null;
            frame.dispose();
            createMainMenu();
        });

        buttonPanel.add(displayBooksButton);
        buttonPanel.add(searchBookButton);
        buttonPanel.add(borrowBookButton);
        buttonPanel.add(returnBookButton);
        buttonPanel.add(viewBorrowedBooksButton);
        buttonPanel.add(requestBookButton);
        buttonPanel.add(logoutButton);

        frame.add(buttonPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private static void displayBooksGUI() {
        JFrame frame = new JFrame("All Books");
        frame.setSize(600, 400);

        String[] columnNames = {"Title", "Author", "Availability"};
        List<Book> books = library.getBooks();
        if (books == null) books = new ArrayList<>();
        String[][] data = new String[books.size()][3];
        for (int i = 0; i < books.size(); i++) {
            Book book = books.get(i);
            data[i][0] = book.getTitle();
            data[i][1] = book.getAuthor();
            data[i][2] = book.isAvailable() ? "Available" : "Borrowed";
        }

        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane);

        frame.setVisible(true);
    }

    private static void searchBookGUI() {
        String title = JOptionPane.showInputDialog("Enter book title to search:");
        if (title != null && !title.trim().isEmpty()) {
            Book book = library.searchBook(title);
            if (book != null) {
                JOptionPane.showMessageDialog(null, "Book Found:\nTitle: " + book.getTitle() + "\nAuthor: " + book.getAuthor() + "\nAvailability: " + (book.isAvailable() ? "Available" : "Borrowed"));
            } else {
                JOptionPane.showMessageDialog(null, "Book not found.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Please enter a valid book title.");
        }
    }

    private static void borrowBookGUI() {
        String title = JOptionPane.showInputDialog("Enter book title to borrow:");
        if (title != null && !title.trim().isEmpty()) {
            boolean success = library.borrowBook(title, currentUser);
            if (success) {
                JOptionPane.showMessageDialog(null, "Book borrowed successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "Book is not available or does not exist.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Please enter a valid book title.");
        }
    }

    private static void returnBookGUI() {
        String title = JOptionPane.showInputDialog("Enter book title to return:");
        if (title != null && !title.trim().isEmpty()) {
            boolean success = library.returnBook(title, currentUser);
            if (success) {
                JOptionPane.showMessageDialog(null, "Book returned successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "You have not borrowed this book.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Please enter a valid book title.");
        }
    }

    private static void viewBorrowedBooksGUI() {
        JFrame frame = new JFrame("Borrowed Books");
        frame.setSize(600, 400);

        String[] columnNames = {"Title", "Author"};
        List<Book> borrowedBooks = library.getBorrowedBooks(currentUser);
        if (borrowedBooks == null) borrowedBooks = new ArrayList<>();
        String[][] data = new String[borrowedBooks.size()][2];
        for (int i = 0; i < borrowedBooks.size(); i++) {
            Book book = borrowedBooks.get(i);
            data[i][0] = book.getTitle();
            data[i][1] = book.getAuthor();
        }

        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane);

        frame.setVisible(true);
    }

    private static void requestBookGUI() {
        JTextField titleField = new JTextField();
        JTextField authorField = new JTextField();
        Object[] message = {
            "Book Title:", titleField,
            "Author Name:", authorField
        };

        int option = JOptionPane.showConfirmDialog(null, message, "Request Book Addition", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String title = titleField.getText().trim();
            String author = authorField.getText().trim();
            if (!title.isEmpty() && !author.isEmpty()) {
                bookRequests.add(new BookRequest(title, author));
                JOptionPane.showMessageDialog(null, "Book request submitted for admin approval.");
            } else {
                JOptionPane.showMessageDialog(null, "Please fill in all fields.");
            }
        }
    }

    private static void handleBookRequestsGUI(JFrame parentFrame) {
        if (bookRequests.isEmpty()) {
            JOptionPane.showMessageDialog(parentFrame, "No book requests available.");
            return;
        }
        String[] requestOptions = new String[bookRequests.size()];
        for (int i = 0; i < bookRequests.size(); i++) {
            BookRequest request = bookRequests.get(i);
            requestOptions[i] = "Title: " + request.getTitle() + ", Author: " + request.getAuthor();
        }
        String selectedRequest = (String) JOptionPane.showInputDialog(
                parentFrame,
                "Select a request to approve/reject:",
                "Book Requests",
                JOptionPane.QUESTION_MESSAGE,
                null,
                requestOptions,
                requestOptions[0]
        );
        if (selectedRequest != null) {
            int index = -1;
            for (int i = 0; i < requestOptions.length; i++) {
                if (requestOptions[i].equals(selectedRequest)) {
                    index = i;
                    break;
                }
            }
            if (index != -1) {
                BookRequest request = bookRequests.get(index);
                int decision = JOptionPane.showConfirmDialog(
                        parentFrame,
                        "Approve this request?\n" + selectedRequest,
                        "Approve/Reject",
                        JOptionPane.YES_NO_OPTION
                );
                if (decision == JOptionPane.YES_OPTION) {
                    library.addBook(new Book(request.getTitle(), request.getAuthor()));
                    JOptionPane.showMessageDialog(parentFrame, "Book approved and added to the library.");
                } else {
                    JOptionPane.showMessageDialog(parentFrame, "Book request rejected.");
                }
                bookRequests.remove(index);
            }
        }
    }
}

class BookRequest {
    private String title;
    private String author;

    public BookRequest(String title, String author) {
        this.title = title;
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }
}