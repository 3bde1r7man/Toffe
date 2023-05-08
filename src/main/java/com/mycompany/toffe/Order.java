package com.mycompany.toffe;

import java.sql.*;
public class Order {
    LoggedInUser owner;
    
    public void viewOrder(String name){
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:db.sqlite3");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Order WHERE owner = " + name);
            while (rs.next()) {
                String paymentMethod = rs.getString("paymentMehtod");
                String billingAddress = rs.getString("billingAddress");
                String status = rs.getString("status");
                System.out.println("PaymentMethod: "+ paymentMethod + " Billing Address: " + billingAddress + " Status: " + status);
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        
    }
}
