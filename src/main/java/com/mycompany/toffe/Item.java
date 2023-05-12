package com.mycompany.toffe;
import java.sql.*;
import java.util.Scanner; 

public class Item {
    public void viewItems() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:db.sqlite3");
            String sql = "SELECT * FROM Item";
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            System.out.println("itemId"+"        "+"name" + "           " +"price");
            while(result.next())
            {
                String itemId = result.getString("itemId");
                // String category = result.getString("category");
                String name = result.getString("name");
                String price = result.getString("price");
                System.out.print("\n");
                System.out.println(itemId+"            "+name + "            " + price);
                System.out.print("\n");
            }
        }catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
    public void getID(String username){
        Scanner myObj = new Scanner(System.in);
        System.out.println("Please enter the itemID : ");
        int itemid = myObj.nextInt();
        int quantity = myObj.nextInt();
        LoggedInUser order = new LoggedInUser(username);
        order.placeOrder();
        Cart item = new Cart(order);
        item.addItemToCart(itemid, quantity);
    }

}