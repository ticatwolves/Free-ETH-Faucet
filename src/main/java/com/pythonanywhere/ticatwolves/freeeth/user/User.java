package com.pythonanywhere.ticatwolves.freeeth.user;

/**
 * Created by Ticat Wolves on 6/23/2017.
 */

public class User {
    public String dName,Email,PhotoUri,userid,amount,claim;
    public User(String uname,String Email,String puri,String userid){
        this.dName = uname;
        this.Email = Email;
        this.PhotoUri = puri;
        this.userid = userid;
        this.amount = "0";
        this.claim = "0";
    }
}
