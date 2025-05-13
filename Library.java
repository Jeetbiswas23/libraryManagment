import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.util.Stack;

public class Library {
    private LinkedList<Book> books;
    private Stack<Book> recentlyBorrowedBooks; // Stack to track recently borrowed books
    private static final String BOOKS_FILE_PATH = "books.txt"; // Separate file for books

    public Library() {
        books = new LinkedList<>();
        recentlyBorrowedBooks = new Stack<>();
        loadBooksFromFile();
    }

    public void addBook(Book book) {
        books.add(book);
        saveBooksToFile();
        System.out.println("Book added successfully: " + book.getTitle());
    }

    public void removeBook(String title) {
        boolean removed = books.removeIf(book -> book.getTitle().equalsIgnoreCase(title));
        if (removed) {
            saveBooksToFile();
            System.out.println("Book removed successfully.");
        } else {
            System.out.println("Book not found.");
        }
    }

    public void displayBooks() {
        if (books.isEmpty()) {
            System.out.println("No books in the library.");
        } else {
            System.out.println("\n--- List of Books ---");
            for (Book book : books) {
                System.out.println(book + " - " + (book.isAvailable() ? "Available" : "Borrowed"));
            }
        }
    }
    
    // Added for GUI
    public List<Book> getBooks() {
        return new ArrayList<>(books);
    }

    // Modified to return Book object
    public Book searchBook(String title) {
        for (Book book : books) {
            if (book.getTitle().equalsIgnoreCase(title)) {
                System.out.println("Book found: " + book);
                return book;
            }
        }
        System.out.println("Book not found.");
        return null;
    }

    // Modified to return success status
    public boolean borrowBook(String title, User user) {
        for (Book book : books) {
            if (book.getTitle().equalsIgnoreCase(title) && book.isAvailable()) {
                book.setAvailable(false);
                user.borrowBook(book);
                recentlyBorrowedBooks.push(book);
                saveBooksToFile();
                System.out.println("Book borrowed successfully: " + book.getTitle());
                return true;
            } else if (book.getTitle().equalsIgnoreCase(title) && !book.isAvailable()) {
                System.out.println("Book is already borrowed.");
                return false;
            }
        }
        System.out.println("Book not available for borrowing.");
        return false;
    }

    // Modified to return success status
    public boolean returnBook(String title, User user) {
        Book returnedBook = user.returnBook(title);
        if (returnedBook != null) {
            returnedBook.setAvailable(true);
            books.add(returnedBook);
            saveBooksToFile();
            System.out.println("Book returned successfully: " + returnedBook.getTitle());
            return true;
        } else {
            System.out.println("You did not borrow this book.");
            return false;
        }
    }

    public void viewBorrowedBooks(User user) {
        user.viewBorrowedBooks();
    }
    
    // Added for GUI
    public List<Book> getBorrowedBooks(User user) {
        return user.getBorrowedBooksList();
    }

    public void viewRecentlyBorrowedBooks() {
        if (recentlyBorrowedBooks.isEmpty()) {
            System.out.println("No recently borrowed books.");
        } else {
            System.out.println("\n--- Recently Borrowed Books ---");
            for (Book book : recentlyBorrowedBooks) {
                System.out.println(book);
            }
        }
    }

    private void saveBooksToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(BOOKS_FILE_PATH))) {
            for (Book book : books) {
                writer.write(book.getTitle() + "," + book.getAuthor() + "," + book.isAvailable());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving books to file: " + e.getMessage());
        }
    }

    private void loadBooksFromFile() {
        File file = new File(BOOKS_FILE_PATH);
        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(BOOKS_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    Book book = new Book(parts[0], parts[1]);
                    if (parts.length == 3) {
                        book.setAvailable(Boolean.parseBoolean(parts[2]));
                    }
                    books.add(book);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading books from file: " + e.getMessage());
        }
    }
}