package com.mycompany.toffe;

import java.sql.*;
public class Order {
    String userName;
    public Order(String userName) {
        this.userName = userName;
    }
    public void saveOrder(String userName, String paymentMethod, String billingAddress, String status){
        int cartID=0, orderId=0;
        try{
            Connection conn = DriverManager.getConnection("jdbc:sqlite:db.sqlite3");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT cartId FROM Cart WHERE owner = " + userName);
            cartID = rs.getInt("cartId");
            rs.close();
            stmt.close();
            conn.close();
        }
        catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        try{
            Connection conn = DriverManager.getConnection("jdbc:sqlite:db.sqlite3");
            PreparedStatement pstmt = conn.prepareStatement("INSERT INTO Order(owner, paymentMethod, billingAddress, status) VALUES(?,?,?,?)");
            pstmt.setString(1, userName);
            pstmt.setString(2, paymentMethod);
            pstmt.setString(3, billingAddress);
            pstmt.setString(4, status);
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        try{
            Connection conn = DriverManager.getConnection("jdbc:sqlite:db.sqlite3");
            String sql = "SELECT max(orderId) FROM Order WHERE owner = ";
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
            Connection conn = DriverManager.getConnection("jdbc:sqlite:db.sqlite3");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM CartItems WHERE cartId = " + cartID);
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
            PreparedStatement pstmt = conn.prepareStatement("DELETE FROM CartItems WHERE cartId = ?");
            pstmt.setInt(1, cartID);
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public void viewOrder(){
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:db.sqlite3");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Order WHERE owner = " + userName);
            while (rs.next()) {
                int orderId = rs.getInt("orderId");
                ResultSet rs2 = stmt.executeQuery("SELECT * FROM OrderItems WHERE orderId = " + orderId);
                while (rs2.next()) {
                    int itemID = rs2.getInt("itemId");
                    ResultSet rs3 = stmt.executeQuery("SELECT name FROM Item WHERE itemId = " + itemID);
                    String itemName = rs3.getString("name");
                    int quantity = rs2.getInt("quantityOrdered");
                    double price = rs2.getDouble("pricePerItem");
                    double totalprice = rs2.getDouble("totalPrice");
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

    public void changeOrderStatus(String newStatus) {
        String sql = "UPDATE Order SET status = ? WHERE order_id = ?";
        String sql2 = "SELECT max(orderId) FROM Order WHERE owner = ";

        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:db.sqlite3");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql2 + userName);
            int orderId = rs.getInt("orderId"); 
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, newStatus);
            pstmt.setInt(2, orderId);
            pstmt.executeUpdate();
            orderId++;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
