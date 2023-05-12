/**
 * This class represents an item object with a view method to display all available items in the database,
 * and a method to get the item ID and quantity from the user, and add the item to the user's cart.
 */

package com.mycompany.toffe;

import java.sql.*;
import java.util.Scanner; 

public class Item {

    /**
      * Displays all items available in the database.
      * 
      * @throws SQLException If there is an error accessing the database.
      */
    public void viewItems() throws SQLException {
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:db.sqlite3");
            String sql = "SELECT * FROM Item";
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            System.out.println("itemId"+"        "+"name" + "           " +"price");
            while(result.next())
            {
                String itemId = result.getString("itemId");
                String name = result.getString("name");
                String price = result.getString("price");
                System.out.print("\n");
                System.out.println(itemId+"            "+name + "            " + price);
                System.out.print("\n");
            }
        }catch (SQLException e) {
            System.err.println(e.getMessage());
            throw e;
        }
    }

    /**
      * Gets the item ID and quantity from the user, and adds the item to the user's cart.
      * 
      * @param username The username of the logged in user.
      */
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