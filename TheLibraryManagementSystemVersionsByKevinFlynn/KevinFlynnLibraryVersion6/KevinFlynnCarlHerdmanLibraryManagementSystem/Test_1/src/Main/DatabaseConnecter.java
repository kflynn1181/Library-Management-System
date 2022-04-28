/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Main;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSetMetaData;

/**
 *
 * @author herdm
 */
public class DatabaseConnecter {
    
    
    private Connection conn;
    
 public DatabaseConnecter(String urlToDatabase) throws ClassNotFoundException, SQLException {
     String driver="net.ucanaccess.jdbc.UcanaccessDriver";
     Class.forName(driver);
     conn=DriverManager.getConnection("jdbc:ucanaccess://"+urlToDatabase);
     
 }
 
 public ResultSet query(String SQL) throws SQLException{
     Statement stmt=conn.createStatement();
     ResultSet result=stmt.executeQuery(SQL);
     return result;
 }
 
 public int update(String SQL) throws SQLException{
     Statement stmt=conn.createStatement();
     int done=stmt.executeUpdate(SQL);
     return done;
 }
 
 public int updateReturnID(String SQL) throws SQLException{
     Statement stmt=conn.createStatement();
     int id=-1;
     stmt.executeUpdate(SQL, Statement.RETURN_GENERATED_KEYS);
     ResultSet result=stmt.getGeneratedKeys();
     if(result.next()){
         id=result.getInt(1);
     }
     return id;
 }
    
    
    
}
