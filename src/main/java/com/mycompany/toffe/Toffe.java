package com.mycompany.toffe;
import java.util.Scanner; 

public class Toffe {
    public static void main(String[] args) throws Exception {
        User user = new User();

        while (true) {
            System.out.println("1-Order");
            System.out.println("2-View Items");
            System.out.println("3-View Order");
            System.out.println("4-Exit");
        
            if (!user.isAuthenticated()) {
                System.out.println("5-Register");
                System.out.println("6-Login");
            }
        
            Scanner myObj = new Scanner(System.in);
            System.out.println("Please enter your choice: ");
            int choice = myObj.nextInt();
        
            if (choice == 1) {
                if (!user.isAuthenticated()) {
                    System.out.println("Please login or register to place an order.");
                } else {
                    Item a = new Item();
                    a.viewItems();
                    a.getID(user.getUsername());
                }
            } else if (choice == 2) {
                Item a = new Item();
                a.viewItems();
            } else if (choice == 3) {
                if (!user.isAuthenticated()) {
                    System.out.println("Please login or register to view your orders.");
                } else {
                    Order order = new Order(user.getUsername());
                    order.viewOrder();
                }
            } else if (choice == 4) {
                return;
            } else if (choice == 5) {
                user.register();
            } else if (choice == 6) {
                user.login();
            }
        }
            }
}