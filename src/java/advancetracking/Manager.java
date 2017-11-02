/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package advancetracking;

import java.sql.Timestamp;

/**
 *
 * @author GNyabuto
 */
public class Manager {
  
    public String getdatekey(){
     String datekey="";   
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    datekey = (""+timestamp).replace(" ", "").replace("-", "").replace(".", "").replace(":", "");
     
     return datekey;
    }
}
