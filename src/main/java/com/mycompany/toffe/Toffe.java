package com.mycompany.toffe;
import java.util.Scanner; 

public class Toffe {
    public static void main(String[] args) throws Exception {
        User user = new User();
        if(user.register()){
            user.login(); 
        }
        while(true){
            System.out.println("1-order\n2-view items\n3-view order");
            Scanner myObj = new Scanner(System.in);
            System.out.println("Please enter your choice : ");
            int choice = myObj.nextInt();
            if(choice == 1)
            {
                Item a = new Item();
                a.viewItems();
                a.getID(user.Username);
            }
            else if(choice == 2)
            {
                Item a = new Item();
                a.viewItems();
            }
            else if(choice == 3)
            {
                Order order = new Order(user.Username);
                order.viewOrder();
            }
        }
    }
}