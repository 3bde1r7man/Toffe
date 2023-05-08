/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.toffe;



/**
 *
 * @author ABDO
 */

public abstract class User {
    public Boolean login(String userName, String password){
        return false;
    }

    public Boolean register(String userName, String email, String password, String address){
        return false;
    }

    public Item searchForItem(String Query){
        return null;
    }

    public Category viewCategories(){
        return null;
    }

    public Item viewCategoryItems(){
        return null;
    }

    public Item viewItem(){
        return null;
    }

    public void enterData(String data){
        
    }

}
