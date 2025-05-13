import java.io.*;
import java.util.HashMap;

public class UserManager {
    private HashMap<String, User> users;
    private static final String FILE_PATH = "users.txt";

    public UserManager() {
        users = new HashMap<>();
        loadUsersFromFile();
    }

    public void registerUser(User user) {
        if (users.containsKey(user.getUsername())) {
            System.out.println("Username already exists. Please choose a different username.");
        } else {
            users.put(user.getUsername(), user);
            saveUsersToFile();
            System.out.println("User registered successfully.");
        }
    }

    public User loginUser(String username, String password) {
        User user = users.get(username);
        if (user != null && user.getPassword().equals(password)) {
            System.out.println("Login successful. Welcome, " + username + "!");
            return user;
        } else {
            System.out.println("Invalid username or password.");
            return null;
        }
    }

    public void viewAllUsers() {
        if (users.isEmpty()) {
            System.out.println("No registered users.");
        } else {
            System.out.println("\n--- Registered Users ---");
            for (String username : users.keySet()) {
                System.out.println("Username: " + username);
            }
        }
    }

    public void resetPassword(String username, String oldPassword, String newPassword) {
        User user = users.get(username);
        if (user != null && user.getPassword().equals(oldPassword)) {
            user.setPassword(newPassword);
            saveUsersToFile();
            System.out.println("Password reset successfully.");
        } else {
            System.out.println("Invalid username or old password.");
        }
    }

    private void saveUsersToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (User user : users.values()) {
                writer.write(user.getUsername() + "," + user.getPassword());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving users to file: " + e.getMessage());
        }
    }

    private void loadUsersFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    users.put(parts[0], new User(parts[0], parts[1]));
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading users from file: " + e.getMessage());
        }
    }
}