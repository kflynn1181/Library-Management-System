/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Main;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import static java.util.Collections.list;

/**
 *
 * @author herdm
 */
public class Library {
    DatabaseConnecter DCC;
    public String FirstName;
    public String LastName;
    public String listString;
    public String Age;
    public String Fname;
    public String Lname;
    public String Age_;
    public String Addr;
    
    
    
        //Open Connection
    public Library() throws SQLException, ClassNotFoundException {
        DCC=new DatabaseConnecter("Library.accdb");
    }
    
    
    //Gets users from database
    public String getUsers() throws SQLException, ClassNotFoundException{      
        FirstName="";
        LastName="";
        Age="";
        String listString = null;
        ArrayList<String> list = new ArrayList<>();
        ResultSet result=DCC.query("SELECT * FROM Users");      
        while(result.next()){
            FirstName=result.getString("FirstName");
            LastName=result.getString("LastName");
            Age=result.getString("Age");
            System.out.println(FirstName);
            System.out.println(LastName);
            System.out.println(Age);
            list.add(FirstName + " " + LastName + " " + Age + "\n");
            listString = String.join("", list);
        }   
        System.out.println(list);
        return listString;
    }
    
    
    //Used for login inserts student details into database
    public void setUser(String Fname, String Lname, String Age_, String Addr) throws SQLException{
    this.Fname = Fname;
    this.Lname = Lname;
    this.Age_ = Age_;   
    this.Addr = Addr;   
    System.out.println("TEST");
    int done =DCC.update("INSERT INTO Users (FirstName, LastName, Age, Address) VALUES ('"+Fname+"', '"+Lname+"', '"+Age_+"', '"+Addr+"')");
    }
    
    
    
    
    
}
