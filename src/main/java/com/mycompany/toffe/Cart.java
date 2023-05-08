package com.mycompany.toffe;

import java.util.Vector;
public class Cart {
    //attributes
    Vector<Item> items;
    double totalPrice;
    LoggedInUser owner;
    //constructor
    Cart(LoggedInUser loggedUser){
        owner = loggedUser;
        items = new Vector<Item>();    
    }
    
    //Methods
    public void viewCart(){
        for (Item item : items) {
            System.out.println("Item Name: " + item.name + "| Quantity: " + item.quantity + "| Total Price: " + item.price);
        }
    }

    public void addItemToCart(Item item, int quantity){
        int place = items.indexOf(item);
        if(place != -1 ){
            items.get(place).quantity += quantity;
        }
        else
        {
            item.quantity = quantity;
            items.add(item);
        }
    }

    public void updateItemQuantity(Item item, int newQuantity){
        int place = items.indexOf(item);
        if(place != -1 ){
            items.get(place).quantity = newQuantity;
        }
        else{
            System.out.println("Item is not in cart!!");
        }
    }

    public void deleteItemFromCart(Item item){
        int place = items.indexOf(item);
        if(place != -1 ){
            items.remove(place);
        }
        else{
            System.out.println("Item is not in cart!!");
        }
    }
}
