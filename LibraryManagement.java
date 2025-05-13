import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class LibraryManagement {
    private static List<BookRequest> bookRequests = new ArrayList<>();

    public static void main(String[] args) {
        Library library = new Library();
        UserManager userManager = new UserManager();
        Scanner scanner = new Scanner(System.in);
        User currentUser = null;

        // Predefined admin credentials
        String adminUsername = "admin";
        String adminPassword = "admin123";

        while (true) {
            System.out.println("\n--- Welcome to the Library Management System ---");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Admin Panel");
            System.out.println("4. Reset Password");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            int userChoice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (userChoice) {
                case 1:
                    handleRegistration(scanner, userManager);
                    break;
                case 2:
                    currentUser = handleLogin(scanner, userManager);
                    if (currentUser != null) {
                        libraryMenu(scanner, library, currentUser);
                    }
                    break;
                case 3:
                    handleAdminPanel(scanner, library, adminUsername, adminPassword);
                    break;
                case 4:
                    handlePasswordReset(scanner, userManager);
                    break;
                case 5:
                    System.out.println("Exiting system. Goodbye!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void handleRegistration(Scanner scanner, UserManager userManager) {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        userManager.registerUser(new User(username, password));
    }

    private static User handleLogin(Scanner scanner, UserManager userManager) {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        User user = userManager.loginUser(username, password);
        if (user == null) {
            System.out.println("Invalid credentials. Try again.");
        } else {
            System.out.println("Login successful!");
        }
        return user;
    }

    private static void handleAdminPanel(Scanner scanner, Library library, String adminUsername, String adminPassword) {
        System.out.print("Enter admin username: ");
        String inputAdminUsername = scanner.nextLine();
        System.out.print("Enter admin password: ");
        String inputAdminPassword = scanner.nextLine();
        if (inputAdminUsername.equals(adminUsername) && inputAdminPassword.equals(adminPassword)) {
            int adminChoice;
            do {
                System.out.println("\n--- Admin Panel ---");
                System.out.println("1. View All Books");
                System.out.println("2. Add Book");
                System.out.println("3. Remove Book");
                System.out.println("4. View Book Requests");
                System.out.println("5. Exit Admin Panel");
                System.out.print("Enter your choice: ");
                adminChoice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (adminChoice) {
                    case 1:
                        library.displayBooks();
                        break;
                    case 2:
                        System.out.print("Enter book title: ");
                        String title = scanner.nextLine();
                        System.out.print("Enter author name: ");
                        String author = scanner.nextLine();
                        library.addBook(new Book(title, author));
                        break;
                    case 3:
                        System.out.print("Enter book title to remove: ");
                        String removeTitle = scanner.nextLine();
                        library.removeBook(removeTitle);
                        break;
                    case 4:
                        handleBookRequests(scanner, library);
                        break;
                    case 5:
                        System.out.println("Exiting Admin Panel...");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } while (adminChoice != 5);
        } else {
            System.out.println("Invalid admin credentials.");
        }
    }

    private static void handleBookRequests(Scanner scanner, Library library) {
        if (bookRequests.isEmpty()) {
            System.out.println("No book requests available.");
            return;
        }
        for (int i = 0; i < bookRequests.size(); i++) {
            BookRequest request = bookRequests.get(i);
            System.out.println((i + 1) + ". Title: " + request.getTitle() + ", Author: " + request.getAuthor());
        }
        System.out.print("Enter the request number to approve/reject (or 0 to exit): ");
        int requestChoice = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        if (requestChoice > 0 && requestChoice <= bookRequests.size()) {
            BookRequest selectedRequest = bookRequests.get(requestChoice - 1);
            System.out.println("1. Approve");
            System.out.println("2. Reject");
            System.out.print("Enter your choice: ");
            int decision = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            if (decision == 1) {
                library.addBook(new Book(selectedRequest.getTitle(), selectedRequest.getAuthor()));
                System.out.println("Book approved and added to the library.");
            } else if (decision == 2) {
                System.out.println("Book request rejected.");
            } else {
                System.out.println("Invalid choice.");
            }
            bookRequests.remove(requestChoice - 1);
        } else if (requestChoice != 0) {
            System.out.println("Invalid request number.");
        }
    }

    private static void handlePasswordReset(Scanner scanner, UserManager userManager) {
        System.out.print("Enter your username: ");
        String username = scanner.nextLine();
        System.out.print("Enter your old password: ");
        String oldPassword = scanner.nextLine();
        System.out.print("Enter your new password: ");
        String newPassword = scanner.nextLine();
        userManager.resetPassword(username, oldPassword, newPassword);
    }

    private static void libraryMenu(Scanner scanner, Library library, User currentUser) {
        int choice;
        do {
            System.out.println("\n--- Library Management System ---");
            System.out.println("1. Display All Books");
            System.out.println("2. Search Book by Title");
            System.out.println("3. Borrow Book");
            System.out.println("4. Return Book");
            System.out.println("5. View Borrowed Books");
            System.out.println("6. Request Book Addition");
            System.out.println("7. Logout");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    library.displayBooks();
                    break;
                case 2:
                    System.out.print("Enter book title to search: ");
                    String searchTitle = scanner.nextLine();
                    library.searchBook(searchTitle);
                    break;
                case 3:
                    System.out.print("Enter book title to borrow: ");
                    String borrowTitle = scanner.nextLine();
                    library.borrowBook(borrowTitle, currentUser);
                    break;
                case 4:
                    System.out.print("Enter book title to return: ");
                    String returnTitle = scanner.nextLine();
                    library.returnBook(returnTitle, currentUser);
                    break;
                case 5:
                    library.viewBorrowedBooks(currentUser);
                    break;
                case 6:
                    handleBookAdditionRequest(scanner);
                    break;
                case 7:
                    System.out.println("Logging out...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 7);
    }

    private static void handleBookAdditionRequest(Scanner scanner) {
        System.out.print("Enter book title: ");
        String title = scanner.nextLine();
        System.out.print("Enter author name: ");
        String author = scanner.nextLine();
        bookRequests.add(new BookRequest(title, author));
        System.out.println("Book request submitted for admin approval.");
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