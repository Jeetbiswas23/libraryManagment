import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;

public class User {
    private String username;
    private String password;
    private LinkedList<Book> borrowedBooks;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.borrowedBooks = new LinkedList<>();
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String newPassword) {
        this.password = newPassword;
    }

    public void borrowBook(Book book) {
        borrowedBooks.add(book);
    }

    public Book returnBook(String title) {
        for (Book book : borrowedBooks) {
            if (book.getTitle().equalsIgnoreCase(title)) {
                borrowedBooks.remove(book);
                return book;
            }
        }
        return null;
    }

    public void viewBorrowedBooks() {
        if (borrowedBooks.isEmpty()) {
            System.out.println("No borrowed books.");
        } else {
            System.out.println("\n--- Borrowed Books ---");
            for (Book book : borrowedBooks) {
                System.out.println(book);
            }
        }
    }
    
    // Added for GUI
    public List<Book> getBorrowedBooksList() {
        return new ArrayList<>(borrowedBooks);
    }
}