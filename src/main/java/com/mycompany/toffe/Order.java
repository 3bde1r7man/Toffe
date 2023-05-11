package com.mycompany.toffe;

import java.sql.*;
public class Order {
    public void saveOrder(String userName, String paymentMethod, String billingAddress,String status){
        try{
            Connection conn = DriverManager.getConnection("jdbc:sqlite:db.sqlite3");
            PreparedStatement pstmt = conn.prepareStatement("INSERT INTO Order(owner, paymentMethod, billingAddress, status) VALUES(?,?,?,?)");
            pstmt.setString(1, userName);
            pstmt.setString(2, paymentMethod);
            pstmt.setString(3, billingAddress);
            pstmt.setString(4, status);
            System.out.println("ordred successfully");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

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
    public void changeOrderStatus(int orderId, String newStatus) {
        String sql = "UPDATE Order SET status = ? WHERE order_id = ?";
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:db.sqlite3");
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, newStatus);
            pstmt.setInt(2, orderId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
