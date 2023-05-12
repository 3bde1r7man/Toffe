package com.mycompany.toffe;


import java.sql.*;
import java.util.Vector;
import java.util.Scanner;

public class Cart {
    //attributes
    double totalPrice;
    LoggedInUser owner;
    Connection conn;
    int cartID;
    Vector<Integer> cartItemIds;
    //constructor
    public Cart(LoggedInUser loggedUser){
        owner = loggedUser;
        totalPrice = 0;
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:db.sqlite3");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT cartId FROM Cart WHERE owner = '" + owner.userName+"'");
            cartID = rs.getInt("cartId");
            rs.close();
            stmt.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        
    }
    
    //Methods
    public void viewCart(){
        int count = 1;
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM CartItems WHERE cartId = '" + cartID+"'");
            Statement stmt2 = conn.createStatement();
            
            while (rs.next()) {
                int itemID = rs.getInt("itemId");
                cartItemIds.add(itemID);
                ResultSet rs2 = stmt2.executeQuery("SELECT name FROM Item WHERE itemId = '" + itemID+"'");
                String itemName = rs2.getString("name");
                double price = rs.getDouble("pricePerItem");
                double totalprice = rs.getDouble("totalPrice");
                int quantity = rs.getInt("quantityOrdered");
                System.out.println((count++) + "Item Name: " + itemName + " | Quantity: " + quantity +" | Price Per Item: "+ price +" | Total Price: " + totalprice);
                totalPrice += totalprice;
                rs2.close();
            }
            System.out.println("total Price for this Cart: " + totalPrice);
            rs.close();
            stmt.close();
            stmt2.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        Scanner scanner = new Scanner(System.in);
        int choose2,choose;
        while(true){
            System.out.println("choose item to edit or "+ count +" to Exit Editing : ");
            choose  = scanner.nextInt();
            if (choose > 0 && choose < count + 1) {
                scanner.close();
                break;
            }
        }
        if(choose == count){
            return;
        }
        while(true){
            System.out.println("1- Change Item Quantity\n2- Delete Item from Cart\n--> ");
            choose2 = scanner.nextInt();
            if(choose2 == 1 || choose2 == 2){
                break;
            }
        }
        if(choose2 == 1){
            System.out.println("Please enter the new quantity: ");
            choose2 = scanner.nextInt();
            updateItemQuantity(cartItemIds.get(choose- 1), choose2);
        }
        else {
            deleteItemFromCart(cartItemIds.get(choose - 1));
        }
    }

    public void addItemToCart(int id, int quantity){
        try {
            PreparedStatement pstmt = conn.prepareStatement("INSERT INTO CartItems(cartId, itemId, quantityOrdered, pricePerItem, totalPrice) VALUES(?,?,?,?,?)");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT price FROM item WHERE itemId = '" + id+"'");
            double price = rs.getDouble("price");
            pstmt.setInt(1, cartID);
            pstmt.setInt(2, id);
            pstmt.setInt(3, quantity);
            pstmt.setDouble(4, price);
            pstmt.setDouble(5, price * quantity);
            pstmt.executeUpdate();
            rs.close();
            stmt.close();
            pstmt.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        
    }

    public void updateItemQuantity(int id, int newQuantity){
        if(newQuantity != 0){
            try {
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT price FROM Item WHERE itemId = '" + id+"'");
                double price = rs.getDouble("price");
                PreparedStatement pstmt = conn.prepareStatement("UPDATE CartItems SET quantityOrdered=? , totalPrice=?  WHERE itemId=? AND cartId=?");
                pstmt.setInt(1, newQuantity);
                pstmt.setDouble(2, price * newQuantity);
                pstmt.setInt(3, id);
                pstmt.setInt(4, cartID);
                pstmt.executeUpdate();
                rs.close();
                stmt.close();
                pstmt.close();
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
        else{
            deleteItemFromCart(id);
        }
        
    }

    public void deleteItemFromCart(int id){
        try {
            PreparedStatement pstmt = conn.prepareStatement("DELETE FROM CartItems WHERE CartId=? AND itemId=?");
            pstmt.setInt(1, cartID);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
}
