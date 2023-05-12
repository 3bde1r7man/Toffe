/**
 * The User class represents a user of the Toffee application.
 * It allows for registration, login, and authentication of users.
 * It also includes methods for generating and verifying OTPs, and for checking the strength of passwords.
 */
package com.mycompany.toffe;

import java.sql.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.Scanner;
import java.util.Random;
import javax.mail.MessagingException;

public class User {
    // Attributes
    Connection conn;
    String Username;
    boolean loggedIn;

    /**
     * Constructor for the User class.
     */
    public User() {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:db.sqlite3");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        loggedIn = false;
    }

    /**
     * Allows the user to register for a new account.
     *
     * @return true if registration is successful, false otherwise.
     */
    public boolean register() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter username: ");
        String userName = scanner.nextLine();
    
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
    
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
    
        System.out.print("Enter address: ");
        String address = scanner.nextLine();
    
        if (userName.isEmpty() || email.isEmpty() || password.isEmpty() || address.isEmpty() ){
            System.out.println("Invalid input parameters");
            return false;
        }

        try {
            // Using prepared statement to prevent SQL injection
            PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM LoggedInUser WHERE username = ? or email = ?");
            pstmt.setString(1, userName);
            pstmt.setString(2, email);

            // Using try-with-resources to automatically close resources
            try (ResultSet rs = pstmt.executeQuery()) {
                if (!rs.next()) {
                    if (isStrongPassword(password)) {
                        System.out.println("Sending email to " + email + "...");
                        int otp = createOTP();
                        EmailService emailService = new EmailService();

                        // Use try-catch to handle exceptions when sending email
                        try {
                            emailService.sendEmail(email, String.valueOf(otp));
                        } catch (MessagingException e) {
                            System.err.println("Failed to send email: " + e.getMessage());
                            return false;
                        }
                        
                        System.out.print("Enter OTP Code: ");
                        int i = 1;
                        int userIn = scanner.nextInt();
                        while (i <= 3) {
                            if (userIn == otp) {
                                // Use prepared statement to prevent SQL injection
                                pstmt = conn.prepareStatement("INSERT INTO LoggedInUser VALUES(?, ?, ?, 0,?)");
                                pstmt.setString(1, userName);
                                pstmt.setString(2, email);
                                pstmt.setString(3, password);
                                pstmt.setString(4, address);
                                pstmt.executeUpdate();
                                System.out.println("Email created successfully");
                                
                                return true;
                            } else {
                                System.out.println("The OTP is not correct. You have " + String.valueOf(3 - i) + " chance(s)...");
                                userIn = scanner.nextInt();
                            }
                            i++;
                        }
                        System.out.println("You have tried 3 times. Please try again later!");
                        
                        return false;
                    } else {
                        System.out.println("Password isn't strong enough!");
                        return false;
                    }
                } else {
                    System.out.println("Username or email already in use!");
                    return false;
                }
            }
        } catch (SQLException e) {
            System.err.println("Failed to execute SQL query: " + e.getMessage());
            
            return false;
        }
    }

    /**
     * Checks the strength of a given password.
     *
     * @param password the password to check.
     * @return true if the password is strong, false otherwise.
     */
    public static boolean isStrongPassword(String password) {
        // Password must be at least 8 characters long and contain at least one uppercase letter, 
        // one lowercase letter, one digit, and one special character.
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    /**
     * Allows the user to login to their account.
     *
     * @return true if login is successful, false otherwise.
     */
    public Boolean login() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter username: ");
        String username = scanner.nextLine();
    
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        
        
        
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM LoggedInUser WHERE username = '" + username + "' AND password = '" + password + "'");
            
            if (rs.next()) {
                System.out.println("Login successful!");
                rs.close();
                stmt.close();
                Username = username;
                loggedIn = true;
                return true;
            } else {
                System.out.println("Incorrect username or password.");
                rs.close();
                stmt.close();
                return false;
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        
        return false;
    }

    /**
     * Creates a random OTP (one-time password) for the user.
     *
     * @return the OTP as an integer.
     */
    public int createOTP() {
        Random rand = new Random();
        int randomNumber = rand.nextInt(900000) + 100000; // generates a random number between 100000 and 999999
        return randomNumber;
    }

    /**
     * Checks if the user is currently authenticated.
     *
     * @return true if the user is authenticated, false otherwise.
     */
    public boolean isAuthenticated() {
        return loggedIn;
    }

    /**
     * Gets the username of the current user.
     *
     * @return the username as a String.
     */
    public String getUsername() {
        return Username;
    }
}