/**
 * This class represents a user who has successfully logged in to the system.
 * It extends the User class and adds functionality related to a logged in user, such as viewing their order history and cart.
 */
package com.mycompany.toffe;
import java.util.Scanner;

public class LoggedInUser extends User {
    Cart cart;
    String userName;
    Order orders;
    
    /**
     * Constructor for a LoggedInUser object.
     * 
     * @param username The username of the logged in user.
     */
    LoggedInUser(String username){
        userName =  username;
        cart = new Cart(this);
        orders = new Order(username);
    }

    /**
     * Displays the order history of the logged in user.
     */
    public void viewOrderHistory(){
        orders.viewOrder();
    }

    /**
     * Displays the contents of the cart of the logged in user.
     */
    public void viewCart(){
        cart.viewCart();
    }

    /**
     * Places an order using the items in the user's cart and prompts the user for billing information.
     */
    public void placeOrder(){
        cart.viewCart();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter the billing address: ");
        String billingAddress = scanner.nextLine();
        orders.saveOrder(userName, "Cash", billingAddress, "Not Delivered");
        scanner.close();
    }
}