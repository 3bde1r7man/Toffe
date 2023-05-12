package com.mycompany.toffe;
import java.util.Scanner;
public class LoggedInUser extends User {
    Cart cart;
    String userName;
    Order orders;
    LoggedInUser(String username){
        userName =  username;
        cart = new Cart(this);
    }

    public void viewOrderHistory(){
        orders.viewOrder(userName);
    }
    public void viewCart(){
        cart.viewCart();
    }
    public void placeOrder(){
        cart.viewCart();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter the billing address: ");
        String billingAddress = scanner.nextLine();
        orders.saveOrder(userName, "Cash", billingAddress, "Not Delivered");
        scanner.close();
    }
}
