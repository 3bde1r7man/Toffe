/**
 * The Cart class represents a user's shopping cart in the Toffee application.
 * It allows the user to add, update and delete items from the cart, and view the current contents of the cart.
 */
package com.mycompany.toffe;

import java.sql.*;
import java.util.Vector;
import java.util.Scanner;

public class Cart {
    
    // Attributes
    double totalPrice;     // The total price of all items in the cart
    LoggedInUser owner;    // The user who owns the cart
    Connection conn;       // The database connection object
    int cartID;            // The unique ID of the cart
    Vector<Integer> cartItemIds; // A list of item IDs in the cart
    
    /**
     * Constructs a new Cart object with the given LoggedInUser as the owner.
     * Initializes the owner and totalPrice variables.
     *
     * @param loggedUser The LoggedInUser object that owns the cart.
     */
    public Cart(LoggedInUser loggedUser) {
        owner = loggedUser;
        totalPrice = 0;
        cartItemIds = new Vector<Integer>();
        // Attempts to connect to a SQLite database
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:db.sqlite3");

            // Creates a statement and executes a query to retrieve the cart ID from the Cart table where the owner is the currently logged in user
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT cartId FROM Cart WHERE owner = '" + owner.userName+"'");
            cartID = rs.getInt("cartId");
            rs.close();
            stmt.close();
        } catch (Exception e) {
            // If an exception is caught, an error message is printed to the console
            System.err.println(e.getMessage());
        }
    }

    /**
     * Prints the contents of the user's cart, and allows the user to select an item to edit or delete.
     */
    public void viewCart() {
        // Initializes a counter variable to keep track of the number of items in the cart
        int count = 1;
        try {
            // Creates a statement and executes a query to retrieve all cart items from the CartItems table for this cart
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM CartItems WHERE cartId = '" + cartID+"'");
            // Creates a second statement to execute a query later on
            Statement stmt2 = conn.createStatement();
            
            // Loops through each row of the result set, and prints out information about each item
            while (rs.next()) {
                // Retrieves various fields from the current row of the result set
                int itemID = rs.getInt("itemId");
                cartItemIds.add(itemID);
                ResultSet rs2 = stmt2.executeQuery("SELECT name FROM Item WHERE itemId = '" + itemID+"'");
                String itemName = rs2.getString("name");
                double price = rs.getDouble("pricePerItem");
                double totalprice = rs.getDouble("totalPrice");
                int quantity = rs.getInt("quantityOrdered");
                // Prints out information about the current item
                System.out.println((count++) + "Item Name: " + itemName + " | Quantity: " + quantity +" | Price Per Item: "+ price +" | Total Price: " + totalprice);
                // Adds the total price of the current item to the cart's total price
                totalPrice += totalprice;
                rs2.close();
            }
            // Prints out the total price of the cart
            System.out.println("total Price for this Cart: " + totalPrice);
            // Closes the result set and statements
            rs.close();
            stmt.close();
            stmt2.close();
        } catch (SQLException e) {
            // If an exception occurs during database access, an error message is printed to the console
            System.err.println(e.getMessage());
        }

        // Allows the user to select an item to edit or delete
        Scanner scanner = new Scanner(System.in);
        int choose2, choose;
        while(true){
            System.out.println("choose item to edit or -1 to Exit : ");
            choose  = scanner.nextInt();
            if(choose == -1){
                return;
            }
            if (choose > 0 && choose < count ) {
                break;
            }
        }
        // Prompts the user to select the type of edit to perform
        while(true){
            System.out.println("1- Change Item Quantity\n2- Delete Item from Cart\n--> ");
            choose2 = scanner.nextInt();
            if(choose2 == 1 || choose2 == 2){
                break;
            }
        }
        // Performs the selected edit (either updating the quantity or deleting the item)
        if(choose2 == 1){
            System.out.println("Please enter the new quantity: ");
            choose2 = scanner.nextInt();
            updateItemQuantity(cartItemIds.get(choose - 1), choose2);
        }
        else {
            deleteItemFromCart(cartItemIds.get(choose - 1));
        }
    }

    /**
     * Adds an item to the cart with the given item ID and quantity.
     *
     * @param id The ID of the item to be added to the cart.
     * @param quantity The quantity of the item to be added to the cart.
     */
    public void addItemToCart(int id, int quantity) {
        try {
            PreparedStatement pstmt = conn.prepareStatement("INSERT INTO CartItems(cartId, itemId, quantityOrdered, pricePerItem, totalPrice) VALUES(?,?,?,?,?)");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT price FROM item WHERE itemId = '" + id+ "'");
            double price = rs.getDouble("price");
            
            // Set parameters for the prepared statement and execute it.
            pstmt.setInt(1, cartID);
            pstmt.setInt(2, id);
            pstmt.setInt(3, quantity);
            pstmt.setDouble(4, price);
            pstmt.setDouble(5, price * quantity);
            pstmt.executeUpdate();
            
            // Close the ResultSet, Statement, and PreparedStatement.
            rs.close();
            stmt.close();
            pstmt.close();
        } catch (SQLException e) {
            // Print the error message to the standard error stream.
            System.err.println(e.getMessage());
        }
    }

    /**
     * Updates the quantity of an item in the cart with the given item ID to the new quantity.
     * If the new quantity is zero, the item is removed from the cart.
     *
     * @param id The ID of the item to be updated.
     * @param newQuantity The new quantity of the item.
     */
    public void updateItemQuantity(int id, int newQuantity) {
        if(newQuantity != 0){
            try {
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT price FROM Item WHERE itemId = '" + id+"'");
                double price = rs.getDouble("price");
                PreparedStatement pstmt = conn.prepareStatement("UPDATE CartItems SET quantityOrdered=? , totalPrice=?  WHERE itemId=? AND cartId=?");
                
                // Set parameters for the prepared statement and execute it.
                pstmt.setInt(1, newQuantity);
                pstmt.setDouble(2, price * newQuantity);
                pstmt.setInt(3, id);
                pstmt.setInt(4, cartID);
                pstmt.executeUpdate();
                
                // Close the ResultSet, Statement, and PreparedStatement.
                rs.close();
                stmt.close();
                pstmt.close();
            } catch (SQLException e) {
                // Print the error message to the standard error stream.
                System.err.println(e.getMessage());
            }
        }
        else{
            // If the new quantity is zero, remove the item from the cart.
            deleteItemFromCart(id);
        }
    }

    /**
     * Deletes an item from the cart with the given item ID.
     *
     * @param id The ID of the item to be deleted from the cart.
     */
    public void deleteItemFromCart(int id) {
        try {
            // Create a prepared statement with the SQL query to delete the item from the cart
            PreparedStatement pstmt = conn.prepareStatement("DELETE FROM CartItems WHERE CartId=? AND itemId=?");
            // Set the parameters for the prepared statement
            pstmt.setInt(1, cartID);
            pstmt.setInt(2, id);
            // Execute the prepared statement to delete the item from the cart
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException e) {
            // Catch any SQL exceptions and print the error message to the console
            System.err.println(e.getMessage());
        }
    }

}