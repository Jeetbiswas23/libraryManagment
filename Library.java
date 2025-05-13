import java.io.*;
import java.util.LinkedList;
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
                System.out.println(book);
            }
        }
    }

    public void searchBook(String title) {
        for (Book book : books) {
            if (book.getTitle().equalsIgnoreCase(title)) {
                System.out.println("Book found: " + book);
                return;
            }
        }
        System.out.println("Book not found.");
    }

    public void borrowBook(String title, User user) {
        for (Book book : books) {
            if (book.getTitle().equalsIgnoreCase(title)) {
                user.borrowBook(book);
                books.remove(book);
                recentlyBorrowedBooks.push(book); // Add to recently borrowed stack
                saveBooksToFile();
                System.out.println("Book borrowed successfully: " + book.getTitle());
                return;
            }
        }
        System.out.println("Book not available for borrowing.");
    }

    public void returnBook(String title, User user) {
        Book returnedBook = user.returnBook(title);
        if (returnedBook != null) {
            books.add(returnedBook);
            saveBooksToFile();
            System.out.println("Book returned successfully: " + returnedBook.getTitle());
        } else {
            System.out.println("You did not borrow this book.");
        }
    }

    public void viewBorrowedBooks(User user) {
        user.viewBorrowedBooks();
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
                writer.write(book.getTitle() + "," + book.getAuthor());
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
                if (parts.length == 2) {
                    books.add(new Book(parts[0], parts[1]));
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading books from file: " + e.getMessage());
        }
    }
}