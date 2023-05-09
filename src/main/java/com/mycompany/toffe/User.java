/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.toffe;
import java.sql.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.Scanner;
import java.util.Random;
import com.mycompany.toffe.EmailSerivce;
import javax.mail.MessagingException;


/**
 *
 * @author ABDO
 */

public abstract class User {
    //attrs
    Connection conn;

    //constructor
    public User(){
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:db.sqlite3");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        
    }
    
    public Boolean register(String userName, String email, String password, String address) {
        try {
            // create a statement to execute SQL queries on the database connection
            Statement stmt = conn.createStatement();
    
            // check if the username or email already exists in the LoggedInUser table
            ResultSet rs = stmt.executeQuery("SELECT * FROM LoggedInUser WHERE username = " + userName + "or email = " + email);
    
            if (!rs.next()) { // if the result set is empty, the username or email is not in use
                if (isStrongPassword(password)) { // check if the password meets the strength requirements
                    System.out.println("sending email to " + email + "...");
                    int otp = createOTP(); // generate a random one-time password
                    EmailSerivce emailService = new EmailSerivce();
                    try {
                        // send an email to the user containing the OTP
                        emailService.sendEmail(email,String.valueOf(otp));
                    } catch (MessagingException e) {
                        System.err.println("Failed to send email: " + e.getMessage());
                        return false;
                    }
    
                    Scanner scanner = new Scanner(System.in);
                    int i = 1;
                    int userIn = scanner.nextInt();
    
                    // prompt the user to enter the OTP and repeat for up to 3 attempts
                    while (i <= 3) {
                        if (userIn == otp) { // if the OTP is correct, insert the new user into the LoggedInUser table
                            rs = stmt.executeQuery("insert into LoggedInUser Values(" + userName + "," + email + ","
                            + password + "," + "0);");
                            System.out.println("email created successfully");
                            scanner.close();
                            rs.close();
                            stmt.close();
                            return true;
                        } else {
                            System.out.println("the otp is not correct you have " + String.valueOf(3 - i) + "chance!!..");
                            userIn = scanner.nextInt(); // prompt the user to enter the OTP again
                        }
                        i++;
                    }
    
                    // if the user fails to enter the correct OTP after 3 attempts, return false
                    System.out.println("you tried 3 times try again later!..");
                    scanner.close();
                    rs.close();
                    stmt.close();
                    return false;
                } else { // if the password is not strong enough, return false
                    System.out.println("Password Isn't Strong Enough!!");
                    return false;
                }
            } else { // if the username or email is already in use, return false
                System.out.println("Username Or Email Already in use!..");
                return false;
            }
        } catch (SQLException e) { // if an SQL exception occurs, print the error message and return false
            System.err.println(e.getMessage());
        }
        // if execution reaches this point, return false by default
        return false;
    }
    

    public static boolean isStrongPassword(String password) {
        // Password must be at least 8 characters long and contain at least one uppercase letter, 
        // one lowercase letter, one digit, and one special character.
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
    
    public Boolean login(String username, String password) {
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM LoggedInUser WHERE username = '" + username + "' AND password = '" + password + "'");
            
            if (rs.next()) {
                System.out.println("Login successful!");
                rs.close();
                stmt.close();
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

    public Item searchForItem(String Query){
        return null;
    }

    public Category viewCategories(){
        return null;
    }

    public Item viewCategoryItems(){
        return null;
    }

    public Item viewItem(){
        return null;
    }

    public int createOTP() {
        Random rand = new Random();
        int randomNumber = rand.nextInt(900000) + 100000; // generates a random number between 100000 and 999999
        return randomNumber;
    }
}
