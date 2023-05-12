/**
 * The Order class represents an order placed by a user. It provides methods for saving an order to the database,
 * retrieving and displaying orders for a particular user, and changing the status of the most recent order for the user.
 */
package com.mycompany.toffe;

import java.sql.*;

public class Order {

    /**
     * The name of the user placing the order.
     */
    String userName;

    /**
     * Constructs a new Order object with the given user name.
     *
     * @param userName the name of the user placing the order
     */
    public Order(String userName) {
        this.userName = userName;
    }

    /**
     * Saves an order to the database with the given information.
     *
     * @param userName      the name of the user placing the order
     * @param paymentMethod the payment method used for the order
     * @param billingAddress the billing address used for the order
     * @param status        the status of the order
     */
    public void saveOrder(String userName, String paymentMethod, String billingAddress, String status) {
        int cartID=0, orderId=0;
        try{
            //Get the cart ID for the specified user
            Connection conn = DriverManager.getConnection("jdbc:sqlite:db.sqlite3");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT cartId FROM Cart WHERE owner = '" + userName + "'");
            cartID = rs.getInt("cartId");
            rs.close();
            stmt.close();
            conn.close();
        }
        catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        
        try{
            //Insert the order into the Order table
            Connection conn = DriverManager.getConnection("jdbc:sqlite:db.sqlite3");
            PreparedStatement pstmt = conn.prepareStatement("INSERT INTO 'Order'(owner, paymentMethod, billingAddress, status) VALUES(?,?,?,?)");
            pstmt.setString(1, userName);
            pstmt.setString(2, paymentMethod);
            pstmt.setString(3, billingAddress);
            pstmt.setString(4, status);
            pstmt.executeUpdate();
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        
        try{
            //Get the ID of the newly inserted order
            Connection conn = DriverManager.getConnection("jdbc:sqlite:db.sqlite3");
            String sql = "SELECT max(orderId) FROM 'Order' WHERE owner = ";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql + userName);
            orderId = rs.getInt("orderId");
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        
        try{
            //Insert the items from the user's cart into the OrderItems table
            Connection conn = DriverManager.getConnection("jdbc:sqlite:db.sqlite3");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM CartItems WHERE cartId = '" + cartID + "'");
            int itemID, quantity;
            double price, totalprice;
            
            while (rs.next()) {
                itemID = rs.getInt("itemId");
                price = rs.getDouble("pricePerItem");
                totalprice = rs.getDouble("totalPrice");
                quantity = rs.getInt("quantityOrdered");
                PreparedStatement pstmt = conn.prepareStatement("INSERT INTO OrderItems(orderId, itemId, quantityOrdered, pricePerItem, totalPrice) VALUES(?,?,?,?,?)");  
                pstmt.setInt(1, orderId);
                pstmt.setInt(2, itemID);
                pstmt.setInt(3, quantity);
                pstmt.setDouble(4, price);
                pstmt.setDouble(5, totalprice);
                pstmt.executeUpdate();
                pstmt.close();
            }
            //Delete the items from the user's cart
            PreparedStatement pstmt = conn.prepareStatement("DELETE FROM CartItems WHERE cartId = ?");
            pstmt.setInt(1, cartID);
            pstmt.executeUpdate();
            rs.close();
            stmt.close();
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Retrieves and displays all orders for a particular user from the database.
     * It queries the 'Order' table to get the order ids and then queries the 'OrderItems' table
     * to get the details of the items in each order. It then joins the 'Item' table to get the
     * name of each item and displays the order details to the console.
     */
    public void viewOrder() {
        try {
            // establish connection to the database
            Connection conn = DriverManager.getConnection("jdbc:sqlite:db.sqlite3");
            Statement stmt = conn.createStatement();
            
            // query the 'Order' table to get all orders for the user
            ResultSet rs = stmt.executeQuery("SELECT * FROM 'Order' WHERE owner = '" + userName + "'");
            while (rs.next()) {
                int orderId = rs.getInt("orderId");
                
                // query the 'OrderItems' table to get details of items in the order
                ResultSet rs2 = stmt.executeQuery("SELECT * FROM OrderItems WHERE orderId = '" + orderId + "'");
                while (rs2.next()) {
                    int itemID = rs2.getInt("itemId");
                    
                    // join the 'Item' table to get the name of the item
                    ResultSet rs3 = stmt.executeQuery("SELECT name FROM Item WHERE itemId = '" + itemID + "'");
                    String itemName = rs3.getString("name");
                    int quantity = rs2.getInt("quantityOrdered");
                    double price = rs2.getDouble("pricePerItem");
                    double totalprice = rs2.getDouble("totalPrice");
                    
                    // display the order details
                    System.out.println(orderId + " " + itemName + " " + quantity + " " + price + " " + totalprice);
                    rs3.close();
                }
                rs2.close();
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Changes the status of the most recent order for the current user.
     *
     * @param newStatus the new status to set for the order
     */
    public void changeOrderStatus(String newStatus) {
        // SQL query to update the status of an order
        String sql = "UPDATE 'Order' SET status = ? WHERE order_id = ?";
        
        // SQL query to get the ID of the most recent order for the current user
        String sql2 = "SELECT max(orderId) FROM 'Order' WHERE owner = ";

        try {
            // Establish a connection to the database
            Connection conn = DriverManager.getConnection("jdbc:sqlite:db.sqlite3");
            
            // Create a statement object
            Statement stmt = conn.createStatement();
            
            // Execute the SQL query to get the ID of the most recent order
            ResultSet rs = stmt.executeQuery(sql2 + userName);
            int orderId = rs.getInt("orderId"); // Get the ID of the most recent order
            
            // Create a prepared statement object to update the status of the order
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, newStatus); // Set the new status parameter
            pstmt.setInt(2, orderId); // Set the order ID parameter
            
            // Execute the prepared statement to update the status of the order
            pstmt.executeUpdate();
            
            // Increment the order ID (not sure what this is for?)
            orderId++;
            
            // Close the result set, statement, and connection objects
            rs.close();
            pstmt.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}