/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package gui_tutorial;

//Code created by Kevin Flynn C18502203 and Carl Herdman C18703995, inspired by DataHandler.java code on Brightspace

//import the necessary modules
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class DataHandler {
    // DB details

    //necessary variables used throughout DataHandler
    private static final String dbURL = "jdbc:ucanaccess://DatabaseTest15.accdb;sysSchema=true";
    private static java.sql.Connection con;
    private static java.sql.Statement stm;
    private static java.sql.PreparedStatement pstm; //prepared statement variable, used extensively throughout this file
    private static int numUpd;
    private static int strUpd;
    public static java.sql.ResultSet rs;
    public static java.sql.ResultSet rs2;
    private static java.sql.ResultSetMetaData rsMeta;
    private static int columnCount;
    private static int counter = 1;
    public static String question_description; //questiondescription variables store the output of the select queries into variables
    public static String question_descriptionextra;
    public static String question_description2;
    public static String question_description22;
    public static String question_description3;
    public static String question_description4;
    public static String question_descriptionr;
    public static String question_descriptionr2;
    public static String question_descriptionr3;
    public static String the_correct_answer;

    public static Vector<String> getTables() { //getTables function
        Vector<String> l = new Vector<>();
        String sqlQuery = "SELECT Name FROM sys.MSysObjects WHERE Type=1 AND Flags=0";
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            con = DriverManager.getConnection(dbURL, "", "");
            stm = con.createStatement();
            rs = stm.executeQuery(sqlQuery);
            while (rs.next()) {
            // each row is an array of objects
                    l.add((String) rs.getObject(1));
            }
        } catch (ClassNotFoundException cnfex) {
            System.err.println("Issue with the JDBC driver.");
            System.exit(1); // terminate program - cannot recover
        } catch (java.sql.SQLException sqlex) {
            System.err.println(sqlex);
        } catch (Exception ex) {
            System.err.println(ex);
            //ex.printStackTrace();
        } finally {
            try {
                if (null != con) {
                    // cleanup resources, once after processing
                    rs.close();
                    stm.close();
                    // and then finally close connection
                    con.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(DataHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return l;
    }

    public static void searchRecords(String table) { //searchRecords function
        String sqlQuery = "SELECT * FROM " + table;
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            con = DriverManager.getConnection(dbURL, "", "");
            stm = con.createStatement(
                    java.sql.ResultSet.TYPE_SCROLL_INSENSITIVE,
                    java.sql.ResultSet.CONCUR_READ_ONLY);
            rs = stm.executeQuery(sqlQuery);
            rsMeta = rs.getMetaData();
            columnCount = rsMeta.getColumnCount();
        } catch (ClassNotFoundException cnfex) {
            System.err.println("Issue with the JDBC driver.");
            System.exit(1); // terminate program - cannot recover
        } catch (java.sql.SQLException sqlex) {
            System.err.println(sqlex);
        } catch (Exception ex) {
            System.err.println(ex);
            //ex.printStackTrace();
        }
    }

    public static Object[] getTitles(String table) { //getTitles function
        Object[] columnNames = new Object[columnCount];
        try {
            for (int col = columnCount; col > 0; col--) {
                columnNames[col - 1]
                        = rsMeta.getColumnName(col);
            }
        } catch (java.sql.SQLException sqlex) {
            System.err.println(sqlex);
        } finally {
            try {
                if (null != con) {
                    // cleanup resources, once after processing
                    rs.close();
                    stm.close();
                    // and then finally close connection
                    con.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(DataHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return columnNames;
    }

    public static Object[][] getRows(String table) { //getRows function
        searchRecords(table);
        Object[][] content;
        try {
// determine the number of rows
            rs.last();
            int number = rs.getRow();
            content = new Object[number][columnCount];
            rs.beforeFirst();
            int i = 0;
            while (rs.next()) {
// each row is an array of objects
                for (int col = 1; col <= columnCount; col++) {
                    content[i][col - 1] = rs.getObject(col);
                }
                i++;
            }
            return content;
        } catch (java.sql.SQLException sqlex) {
            System.err.println(sqlex);
        }
        return null;
    }
    
    //AddBook function, used to add a book to the Books table
    public static void AddBook(String bookTitleStr, String bookAuthourStr, String bookPublisherStr, String bookGenreStr, String bookPriceStr, Integer stockInt) { //JDBCConnect2 function, used for login, to store student name and student id in student table of database
        //System.out.println(stockInt);
        try {
            con = DriverManager.getConnection(dbURL, "", "");

            pstm = con.prepareStatement(
            "UPDATE Books SET Stock = Stock + ? WHERE Title=?"); //query to update stock
            pstm.setInt(1,stockInt);       //first value to first parameter
            pstm.setString(2,bookTitleStr);         //second value to second parameter
            numUpd = pstm.executeUpdate();   //first update
            pstm.close();                    //Close the PreparedStatement object
            
            pstm = con.prepareStatement(
            "INSERT INTO Books (Title, Authour, Publisher, Genre, Price, Stock) VALUES (?, ?, ?, ?, ?, ?)"); //inserting values into Books using prepared statements
            pstm.setString(1,bookTitleStr);       //first value to first parameter
            pstm.setString(2,bookAuthourStr);         //second value to second parameter
            pstm.setString(3,bookPublisherStr);       //third value to third parameter
            pstm.setString(4,bookGenreStr);         //fourth value to fourth parameter
            pstm.setString(5,bookPriceStr);       //fifth value to fifth parameter
            pstm.setInt(6,stockInt);       //fifth value to fifth parameter
            numUpd = pstm.executeUpdate();   //first update
            pstm.close();                    //Close the PreparedStatement object
            
        } catch (SQLException sqlex) {
            //System.err.println("SQL statement issue " + sqlex.getMessage());
        } finally {

            try {
                if (null != stm) {
                    stm.close();
                }
                if (null != con) {
                    con.close();
                }
            } catch (SQLException sqlex) {
                //System.err.println(sqlex.getMessage());
            }
        }
    }
    
        //RemoveBook function, used to remove a book to the Books table
        public static void RemoveBook(String bookTitleStr, String bookAuthourStr, String bookPublisherStr, String bookGenreStr, String bookPriceStr, Integer stockInt) { //JDBCConnect2 function, used for login, to store student name and student id in student table of database
        try {
            con = DriverManager.getConnection(dbURL, "", "");
            pstm = con.prepareStatement(
            "SELECT Title from Books where Title=?;"); //selects title from the Books table
            pstm.setString(1,bookTitleStr);       //first value to first parameter
            rs2 = pstm.executeQuery();   //first update
            while (rs2.next()) {         //Position the cursor
            question_descriptionr = rs2.getString(1);
            }
            rs2.close();                    //Close the PreparedStatement object
            pstm.close();                    //Close the PreparedStatement object
            
            pstm = con.prepareStatement(
            "SELECT Stock from Books where Title=?;");
            pstm.setString(1,bookTitleStr);       //first value to first parameter
            rs2 = pstm.executeQuery();   //first update
            while (rs2.next()) {         //Position the cursor
            question_descriptionr2 = rs2.getString(1);
            }
            rs2.close();                    //Close the PreparedStatement object
            pstm.close();                    //Close the PreparedStatement object
            
            pstm = con.prepareStatement(
            "SELECT Title from Books where Title=?;");
            pstm.setString(1,bookTitleStr);       //first value to first parameter
            rs2 = pstm.executeQuery();   //first update
            while (rs2.next()) {         //Position the cursor
            question_descriptionr3 = rs2.getString(1);
            }
            rs2.close();                    //Close the PreparedStatement object
            pstm.close();                    //Close the PreparedStatement object
                    } catch (SQLException sqlex) {
            System.err.println("SQL statement issue " + sqlex.getMessage());
        } finally {

           try {
                if (null != stm) {
                    stm.close();
                }
                if (null != con) {
                    con.close();
                }
            } catch (SQLException sqlex) {
                System.err.println(sqlex.getMessage());
            }
        }
        //JOptionPane.showMessageDialog(null, question_description3);
            
            if (!bookTitleStr.equals(question_descriptionr)){
                JOptionPane.showMessageDialog(null, "This book is not contained in the database");
                return;
            }
            
            int new_question_descriptionr2 = Integer.parseInt(question_descriptionr2);
            
            if (stockInt>new_question_descriptionr2){
                JOptionPane.showMessageDialog(null, "The database do not have this quantity of books available");
            }

            if (bookTitleStr.equals(question_descriptionr) && stockInt<=new_question_descriptionr2 && bookTitleStr.equals(question_descriptionr3)) { //if book is in issued books
            JOptionPane.showMessageDialog(null, bookTitleStr + " successfully removed");         
            
            
        try {
            con = DriverManager.getConnection(dbURL, "", "");

            pstm = con.prepareStatement(
            "UPDATE Books SET Stock = Stock - ? WHERE Title=?"); //query to update stock
            pstm.setInt(1,stockInt);       //first value to first parameter
            pstm.setString(2,bookTitleStr);         //second value to second parameter
            numUpd = pstm.executeUpdate();   //first update
            pstm.close();                    //Close the PreparedStatement object
            
            pstm = con.prepareStatement(
            "DELETE FROM Books WHERE Title=? AND Authour=? AND Publisher=? AND Genre=? AND Price=? AND Stock<1");
            pstm.setString(1,bookTitleStr);       //first value to first parameter
            pstm.setString(2,bookAuthourStr);         //second value to second parameter
            pstm.setString(3,bookPublisherStr);       //third value to third parameter
            pstm.setString(4,bookGenreStr);         //fourth value to fourth parameter
            pstm.setString(5,bookPriceStr);       //fifth value to fifth parameter
            numUpd = pstm.executeUpdate();   //first update
            pstm.close();                    //Close the PreparedStatement object
            
        } catch (SQLException sqlex) {
            System.err.println("SQL statement issue " + sqlex.getMessage());
        } finally {

            try {
                if (null != stm) {
                    stm.close();
                }
                if (null != con) {
                    con.close();
                }
            } catch (SQLException sqlex) {
                System.err.println(sqlex.getMessage());
            }
        }
    }}
    
    //IssueBookForBuy function, used to issue a book to a user
    public static void IssueBookForBuy(String bookTitleStr, String bookAuthourStr, String bookPublisherStr, String bookGenreStr, String bookPriceStr, Integer stockInt, String personNameStr, String personAddressStr, String personEmailStr, String personPhoneStr) { //JDBCConnect2 function, used for login, to store student name and student id in student table of database
        String question_description3=""; //resetting variables to empty strings
        String question_description4="";
        String question_descriptionr="";
        try {
            con = DriverManager.getConnection(dbURL, "", "");
            pstm = con.prepareStatement(
            "SELECT Title from Books where Title=?;");
            pstm.setString(1,bookTitleStr);       //first value to first parameter
            rs2 = pstm.executeQuery();   //first update
            while (rs2.next()) {         //Position the cursor
            question_description = rs2.getString(1);
            }
            rs2.close();                    //Close the PreparedStatement object
            pstm.close();                    //Close the PreparedStatement object
            
            pstm = con.prepareStatement(
            "SELECT Stock from Books where Title=?;");
            pstm.setString(1,bookTitleStr);       //first value to first parameter
            rs2 = pstm.executeQuery();   //first update
            while (rs2.next()) {         //Position the cursor
            question_description2 = rs2.getString(1);
            }
            rs2.close();                    //Close the PreparedStatement object
            pstm.close();                    //Close the PreparedStatement object
            
            pstm = con.prepareStatement(
            "SELECT BookTitle from BooksIssued where BookTitle=? and UserName=?;");
            pstm.setString(1,bookTitleStr);       //first value to first parameter
            pstm.setString(2,personNameStr);       //first value to first parameter
            rs2 = pstm.executeQuery();   //first update
            while (rs2.next()) {         //Position the cursor
            question_description3 = rs2.getString(1);
            }
            rs2.close();                    //Close the PreparedStatement object
            pstm.close();                    //Close the PreparedStatement object
            
            pstm = con.prepareStatement(
            "SELECT Name from Users where Name=? and Address=? and Email=? and Phone=?;");
            pstm.setString(1,personNameStr);       //first value to first parameter
            pstm.setString(2,personAddressStr);       //first value to first parameter
            pstm.setString(3,personEmailStr);       //first value to first parameter
            pstm.setString(4,personPhoneStr);       //first value to first parameter
            rs2 = pstm.executeQuery();   //first update
            while (rs2.next()) {         //Position the cursor
            question_descriptionr = rs2.getString(1);
            }
            rs2.close();                    //Close the PreparedStatement object
            pstm.close();                    //Close the PreparedStatement object
                    } catch (SQLException sqlex) {
            System.err.println("SQL statement issue " + sqlex.getMessage());
        } finally {

           try {
                if (null != stm) {
                    stm.close();
                }
                if (null != con) {
                    con.close();
                }
            } catch (SQLException sqlex) {
                System.err.println(sqlex.getMessage());
            }
        }
        //JOptionPane.showMessageDialog(null, question_description3);
            
            if (!bookTitleStr.equals(question_description)){ //if the book cannot be found in the database
                JOptionPane.showMessageDialog(null, "This book is not available for purchase as it is not contained in our database");
                return; //return statement
            }
            
            if (!personNameStr.equals(question_descriptionr)){ //if the user cannot be found in the database
                JOptionPane.showMessageDialog(null, "This user does not belong in the database");
                return; //return statement
            }
            
            int new_question_description2 = Integer.parseInt(question_description2); //convert string to int
            
            if (stockInt>new_question_description2){ //if the quantity of books is larger than what is available within the database
                JOptionPane.showMessageDialog(null, "The database do not have this quantity of books available for purchase/rent");
            }

            if (bookTitleStr.equals(question_description) && stockInt<=new_question_description2 && bookTitleStr.equals(question_description3)) { //if book is in issued books
            JOptionPane.showMessageDialog(null, bookTitleStr + " issued successfully to " + personNameStr);
            try {
            con = DriverManager.getConnection(dbURL, "", "");
            pstm = con.prepareStatement(
            "UPDATE Books SET Stock = Stock - ? WHERE Title=?"); //query to update stock
            pstm.setInt(1,stockInt);       //first value to first parameter
            pstm.setString(2,bookTitleStr);         //second value to second parameter
            numUpd = pstm.executeUpdate();   //first update
            pstm.close();                    //Close the PreparedStatement object
            
            pstm = con.prepareStatement(
            "DELETE FROM Books WHERE Title=? AND Stock<1"); //if stock is below one then delete from Books table
            pstm.setString(1,bookTitleStr);       //first value to first parameter
            numUpd = pstm.executeUpdate();   //first update
            pstm.close();                    //Close the PreparedStatement object
            
            pstm = con.prepareStatement(
            "UPDATE BooksIssued SET BookStock = BookStock + ? WHERE BookTitle=? AND UserName = ?"); //update stock
            pstm.setInt(1,stockInt);       //first value to first parameter
            pstm.setString(2,bookTitleStr);         //second value to second parameter
            pstm.setString(3,personNameStr);         //second value to second parameter
            numUpd = pstm.executeUpdate();   //first update
            pstm.close();                    //Close the PreparedStatement object
            }
         catch (SQLException sqlex) {
            System.err.println("SQL statement issue " + sqlex.getMessage());
        } finally {

           try {
                if (null != stm) {
                    stm.close();
                }
                if (null != con) {
                    con.close();
                }
            } catch (SQLException sqlex) {
                System.err.println(sqlex.getMessage());
            }
        }
    }
    
        if (bookTitleStr.equals(question_description) && stockInt<=new_question_description2 && "".equals(question_description3) ) { //if book is not in issuedbooks
            JOptionPane.showMessageDialog(null, bookTitleStr + " issued successfully to " + personNameStr);
            try {
            con = DriverManager.getConnection(dbURL, "", "");
            
            pstm = con.prepareStatement(
            "UPDATE Books SET Stock = Stock - ? WHERE Title=?"); //query to update stock
            pstm.setInt(1,stockInt);       //first value to first parameter
            pstm.setString(2,bookTitleStr);         //second value to second parameter
            numUpd = pstm.executeUpdate();   //first update
            pstm.close();                    //Close the PreparedStatement object
            
            pstm = con.prepareStatement(
            "DELETE FROM Books WHERE Title=? AND Stock<1"); //delete from books if the stock is less than one
            pstm.setString(1,bookTitleStr);       //first value to first parameter
            numUpd = pstm.executeUpdate();   //first update
            pstm.close();                    //Close the PreparedStatement object
            
            pstm = con.prepareStatement(
            "INSERT INTO BooksIssued (BookTitle, BookAuthour, BookPublisher, BookGenre, BookPrice, UserName, UserAddress, UserEmail, UserPhone, BuyOrRent, DatePurchased, BookStock) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, 'Buy', now(), ?)"); //insert into BooksIssued statement
            pstm.setString(1,bookTitleStr);       //first value to first parameter
            pstm.setString(2,bookAuthourStr);         //second value to second parameter
            pstm.setString(3,bookPublisherStr);       //third value to third parameter
            pstm.setString(4,bookGenreStr);         //fourth value to fourth parameter
            pstm.setString(5,bookPriceStr);       //fifth value to fifth parameter
            pstm.setString(6,personNameStr);         //second value to second parameter
            pstm.setString(7,personAddressStr);       //third value to third parameter
            pstm.setString(8,personEmailStr);         //fourth value to fourth parameter
            pstm.setString(9,personPhoneStr);       //fifth value to fifth parameter
            pstm.setInt(10,stockInt);       //fifth value to fifth parameter
            numUpd = pstm.executeUpdate();   //first update
            pstm.close();                    //Close the PreparedStatement object
            
            question_description="";
            
            //pstm = con.prepareStatement(
            //"DELETE FROM BooksIssued WHERE DatePurchased=(SELECT MAX(DatePurchased) FROM BooksIssued)");
            //numUpd = pstm.executeUpdate();   //first update
            //pstm.close();                    //Close the PreparedStatement object
            }
         catch (SQLException sqlex) {
            System.err.println("SQL statement issue " + sqlex.getMessage());
        } finally {

           try {
                if (null != stm) {
                    stm.close();
                }
                if (null != con) {
                    con.close();
                }
            } catch (SQLException sqlex) {
                System.err.println(sqlex.getMessage());
            }
        }
    }
    question_description="";
    
    
    }
       
    //IssueBookForRent function, used to issue a book for rent to a user
    public static void IssueBookForRent(String bookTitleStr, String bookAuthourStr, String bookPublisherStr, String bookGenreStr, String bookPriceStr, Integer stockInt, String personNameStr, String personAddressStr, String personEmailStr, String personPhoneStr) { //JDBCConnect2 function, used for login, to store student name and student id in student table of database
        String question_description3=""; //resetting variables to empty strings
        String question_description4="";
        String question_descriptionr="";
        try {
            con = DriverManager.getConnection(dbURL, "", "");
            pstm = con.prepareStatement(
            "SELECT Title from Books where Title=?;");
            pstm.setString(1,bookTitleStr);       //first value to first parameter
            rs2 = pstm.executeQuery();   //first update
            while (rs2.next()) {         //Position the cursor
            question_description = rs2.getString(1);
            }
            rs2.close();                    //Close the PreparedStatement object
            pstm.close();                    //Close the PreparedStatement object
            
            pstm = con.prepareStatement(
            "SELECT Stock from Books where Title=?;");
            pstm.setString(1,bookTitleStr);       //first value to first parameter
            rs2 = pstm.executeQuery();   //first update
            while (rs2.next()) {         //Position the cursor
            question_description2 = rs2.getString(1);
            }
            rs2.close();                    //Close the PreparedStatement object
            pstm.close();                    //Close the PreparedStatement object
            
            pstm = con.prepareStatement(
            "SELECT BookTitle from BooksIssued where BookTitle=? and UserName=?;");
            pstm.setString(1,bookTitleStr);       //first value to first parameter
            pstm.setString(2,personNameStr);       //first value to first parameter
            rs2 = pstm.executeQuery();   //first update
            while (rs2.next()) {         //Position the cursor
            question_description3 = rs2.getString(1);
            }
            rs2.close();                    //Close the PreparedStatement object
            pstm.close();                    //Close the PreparedStatement object
            
            pstm = con.prepareStatement(
            "SELECT Name from Users where Name=? and Address=? and Email=? and Phone=?;");
            pstm.setString(1,personNameStr);       //first value to first parameter
            pstm.setString(2,personAddressStr);       //first value to first parameter
            pstm.setString(3,personEmailStr);       //first value to first parameter
            pstm.setString(4,personPhoneStr);       //first value to first parameter
            rs2 = pstm.executeQuery();   //first update
            while (rs2.next()) {         //Position the cursor
            question_descriptionr = rs2.getString(1);
            }
            rs2.close();                    //Close the PreparedStatement object
            pstm.close();                    //Close the PreparedStatement object
                    } catch (SQLException sqlex) {
            System.err.println("SQL statement issue " + sqlex.getMessage());
        } finally {

           try {
                if (null != stm) {
                    stm.close();
                }
                if (null != con) {
                    con.close();
                }
            } catch (SQLException sqlex) {
                System.err.println(sqlex.getMessage());
            }
        }
        //JOptionPane.showMessageDialog(null, question_description3);
            
            if (!bookTitleStr.equals(question_description)){ //if the book cannot be found in the database 
                JOptionPane.showMessageDialog(null, "This book is not available for purchase as it is not contained in our database");
                return;
            }
            
            if (!personNameStr.equals(question_descriptionr)){ //if the user cannot be found in the database
                JOptionPane.showMessageDialog(null, "This user does not belong in the database");
                return;
            }
            
            int new_question_description2 = Integer.parseInt(question_description2); //converts string to int
            
            if (stockInt>new_question_description2){ //if the quantity of books entered is larger than what is available in the library
                JOptionPane.showMessageDialog(null, "The database do not have this quantity of books available for purchase/rent");
            }

            if (bookTitleStr.equals(question_description) && stockInt<=new_question_description2 && bookTitleStr.equals(question_description3)) { //if book is in issued books
            JOptionPane.showMessageDialog(null, bookTitleStr + " issued successfully to " + personNameStr);
            try {
            con = DriverManager.getConnection(dbURL, "", "");
            pstm = con.prepareStatement(
            "UPDATE Books SET Stock = Stock - ? WHERE Title=?"); //query to update stock
            pstm.setInt(1,stockInt);       //first value to first parameter
            pstm.setString(2,bookTitleStr);         //second value to second parameter
            numUpd = pstm.executeUpdate();   //first update
            pstm.close();                    //Close the PreparedStatement object
            
            pstm = con.prepareStatement(
            "DELETE FROM Books WHERE Title=? AND Stock<1");
            pstm.setString(1,bookTitleStr);       //first value to first parameter
            numUpd = pstm.executeUpdate();   //first update
            pstm.close();                    //Close the PreparedStatement object
            
            pstm = con.prepareStatement(
            "UPDATE BooksIssued SET BookStock = BookStock + ? WHERE BookTitle=? AND UserName = ?");
            pstm.setInt(1,stockInt);       //first value to first parameter
            pstm.setString(2,bookTitleStr);         //second value to second parameter
            pstm.setString(3,personNameStr);         //second value to second parameter
            numUpd = pstm.executeUpdate();   //first update
            pstm.close();                    //Close the PreparedStatement object
            }
         catch (SQLException sqlex) {
            System.err.println("SQL statement issue " + sqlex.getMessage());
        } finally {

           try {
                if (null != stm) {
                    stm.close();
                }
                if (null != con) {
                    con.close();
                }
            } catch (SQLException sqlex) {
                System.err.println(sqlex.getMessage());
            }
        }
    }
    
        if (bookTitleStr.equals(question_description) && stockInt<=new_question_description2 && "".equals(question_description3) ) { //if book is not in issuedbooks
            JOptionPane.showMessageDialog(null, bookTitleStr + " issued successfully to " + personNameStr);
            try {
            con = DriverManager.getConnection(dbURL, "", "");
            
            pstm = con.prepareStatement(
            "UPDATE Books SET Stock = Stock - ? WHERE Title=?"); //query to update stock
            pstm.setInt(1,stockInt);       //first value to first parameter
            pstm.setString(2,bookTitleStr);         //second value to second parameter
            numUpd = pstm.executeUpdate();   //first update
            pstm.close();                    //Close the PreparedStatement object
            
            pstm = con.prepareStatement(
            "DELETE FROM Books WHERE Title=? AND Stock<1"); //delete from books where the stock is below one
            pstm.setString(1,bookTitleStr);       //first value to first parameter
            numUpd = pstm.executeUpdate();   //first update
            pstm.close();                    //Close the PreparedStatement object
            
            pstm = con.prepareStatement(
            "INSERT INTO BooksIssued (BookTitle, BookAuthour, BookPublisher, BookGenre, BookPrice, UserName, UserAddress, UserEmail, UserPhone, BuyOrRent, DateForReturn, BookStock) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, 'Rent', DateAdd ('ww', 1, now()), ?)"); //insert statement
            pstm.setString(1,bookTitleStr);       //first value to first parameter
            pstm.setString(2,bookAuthourStr);         //second value to second parameter
            pstm.setString(3,bookPublisherStr);       //third value to third parameter
            pstm.setString(4,bookGenreStr);         //fourth value to fourth parameter
            pstm.setString(5,bookPriceStr);       //fifth value to fifth parameter
            pstm.setString(6,personNameStr);         //second value to second parameter
            pstm.setString(7,personAddressStr);       //third value to third parameter
            pstm.setString(8,personEmailStr);         //fourth value to fourth parameter
            pstm.setString(9,personPhoneStr);       //fifth value to fifth parameter
            pstm.setInt(10,stockInt);       //fifth value to fifth parameter
            numUpd = pstm.executeUpdate();   //first update
            pstm.close();                    //Close the PreparedStatement object
            
            question_description=""; //reset variable to empty string
            
            //pstm = con.prepareStatement(
            //"DELETE FROM BooksIssued WHERE DatePurchased=(SELECT MAX(DatePurchased) FROM BooksIssued)");
            //numUpd = pstm.executeUpdate();   //first update
            //pstm.close();                    //Close the PreparedStatement object
            }
         catch (SQLException sqlex) {
            System.err.println("SQL statement issue " + sqlex.getMessage());
        } finally {

           try {
                if (null != stm) {
                    stm.close();
                }
                if (null != con) {
                    con.close();
                }
            } catch (SQLException sqlex) {
                System.err.println(sqlex.getMessage());
            }
        }
    }
    question_description=""; //reset variable to empty string
    }
     
    //getRowsForOverdueAccounts, used to display the overdue accounts table onto the GUI
    public static Object[][] getRowsForOverdueAccounts(String table) { //getRows function
        searchOverdueAccounts(table);
        Object[][] content;
        try {
            // determine the number of rows
            rs.last();
            int number = rs.getRow();
            content = new Object[number][columnCount];
            rs.beforeFirst();
            int i = 0;
            while (rs.next()) {
            // each row is an array of objects
                for (int col = 1; col <= columnCount; col++) {
                    content[i][col - 1] = rs.getObject(col);
                }
                i++;
            }
            return content;
        } catch (java.sql.SQLException sqlex) {
            System.err.println(sqlex);
        }
        return null;
    }
     
     
    //searchOverdueAccounts, used to display the overdue accounts table onto the GUI. Notice the sql query, this filters the BooksIssued table to find rows that have not returned their rented books within a week
    public static void searchOverdueAccounts(String table) { //searchRecords function
        String sqlQuery = "SELECT * FROM BooksIssued WHERE DateForReturn<now()";
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            con = DriverManager.getConnection(dbURL, "", "");
            stm = con.createStatement(
                    java.sql.ResultSet.TYPE_SCROLL_INSENSITIVE,
                    java.sql.ResultSet.CONCUR_READ_ONLY);
            rs = stm.executeQuery(sqlQuery);
            rsMeta = rs.getMetaData();
            columnCount = rsMeta.getColumnCount();
        } catch (ClassNotFoundException cnfex) {
            System.err.println("Issue with the JDBC driver.");
            System.exit(1); // terminate program - cannot recover
        } catch (java.sql.SQLException sqlex) {
            System.err.println(sqlex);
        } catch (Exception ex) {
            System.err.println(ex);
            //ex.printStackTrace();
        }
    }
    
    //ReturnBoughtBook, used to return a book bought buy a user to the library
    public static void ReturnBoughtBook(String bookTitleStr, String bookAuthourStr, String bookPublisherStr, String bookGenreStr, String bookPriceStr, Integer stockInt, String personNameStr, String personAddressStr, String personEmailStr, String personPhoneStr) { //JDBCConnect2 function, used for login, to store student name and student id in student table of database
        String question_description3=""; //resetting variables to empty strings
        String question_description4="";
        String question_descriptionr="";
        
        try {
            con = DriverManager.getConnection(dbURL, "", "");
            pstm = con.prepareStatement(
            "SELECT BookTitle from BooksIssued where BookTitle=? and UserName=?;");
            pstm.setString(1,bookTitleStr);       //first value to first parameter
            pstm.setString(2,personNameStr);       //first value to first parameter
            rs2 = pstm.executeQuery();   //first update
            while (rs2.next()) {         //Position the cursor
            question_description = rs2.getString(1);
            }
            rs2.close();                    //Close the PreparedStatement object
            pstm.close();                    //Close the PreparedStatement object
            
            pstm = con.prepareStatement(
            "SELECT UserName from BooksIssued where BookTitle=? and UserName=?;");
            pstm.setString(1,bookTitleStr);       //first value to first parameter
            pstm.setString(2,personNameStr);       //first value to first parameter
            rs2 = pstm.executeQuery();   //first update
            while (rs2.next()) {         //Position the cursor
            question_descriptionextra = rs2.getString(1);
            }
            rs2.close();                    //Close the PreparedStatement object
            pstm.close();                    //Close the PreparedStatement object
            
            pstm = con.prepareStatement(
            "SELECT BookStock from BooksIssued where BookTitle=? and UserName=?;");
            pstm.setString(1,bookTitleStr);       //first value to first parameter
            pstm.setString(2,personNameStr);       //first value to first parameter
            rs2 = pstm.executeQuery();   //first update
            while (rs2.next()) {         //Position the cursor
            question_description22 = rs2.getString(1);
            }
            rs2.close();                    //Close the PreparedStatement object
            pstm.close();                    //Close the PreparedStatement object
            
            pstm = con.prepareStatement(
            "SELECT Title from Books where Title=?;");
            pstm.setString(1,bookTitleStr);       //first value to first parameter
            rs2 = pstm.executeQuery();   //first update
            while (rs2.next()) {         //Position the cursor
            question_description3 = rs2.getString(1);
            }
            rs2.close();                    //Close the PreparedStatement object
            pstm.close();                    //Close the PreparedStatement object
            
            pstm = con.prepareStatement(
            "SELECT Name from Users where Name=? and Address=? and Email=? and Phone=?;");
            pstm.setString(1,personNameStr);       //first value to first parameter
            pstm.setString(2,personAddressStr);       //first value to first parameter
            pstm.setString(3,personEmailStr);       //first value to first parameter
            pstm.setString(4,personPhoneStr);       //first value to first parameter
            rs2 = pstm.executeQuery();   //first update
            while (rs2.next()) {         //Position the cursor
            question_descriptionr = rs2.getString(1);
            }
            rs2.close();                    //Close the PreparedStatement object
            pstm.close();                    //Close the PreparedStatement object
                    } catch (SQLException sqlex) {
            System.err.println("SQL statement issue " + sqlex.getMessage());
        } finally {

           try {
                if (null != stm) {
                    stm.close();
                }
                if (null != con) {
                    con.close();
                }
            } catch (SQLException sqlex) {
                System.err.println(sqlex.getMessage());
            }
        }
        //JOptionPane.showMessageDialog(null, question_description);
        //JOptionPane.showMessageDialog(null, question_descriptionextra);
        //JOptionPane.showMessageDialog(null, question_description22);
        //JOptionPane.showMessageDialog(null, question_description3);
        //question_description="";
        //question_descriptionextra="";
        //question_description2="";
        //question_description3="";
            
            if (!bookTitleStr.equals(question_description) || !personNameStr.equals(question_descriptionextra)){ //if the book has not been issued
                JOptionPane.showMessageDialog(null, "Could not find this issued book. Please make sure the fields are correct");
                return;
            }
            
            if (!personNameStr.equals(question_descriptionr)){ //if the user is not in the users table of the database
                JOptionPane.showMessageDialog(null, "This user does not belong in the database");
                return;
            }
            
             int new_question_description2 = Integer.parseInt(question_description22); //converts string to int
            
            if (stockInt>new_question_description2){ //if the stock inputted is greater than what is available
                JOptionPane.showMessageDialog(null, "The database do not have this quantity of books available for purchase/rent");
            }

            if (bookTitleStr.equals(question_description) && stockInt<=new_question_description2 && bookTitleStr.equals(question_description3)) { //if book is in issued books
            JOptionPane.showMessageDialog(null, personNameStr + " successfully returned " + bookTitleStr);
            System.out.println("first loop");
            try {
            con = DriverManager.getConnection(dbURL, "", "");
            pstm = con.prepareStatement(
            "UPDATE Books SET Stock = Stock + ? WHERE Title=?"); //query to update stock
            pstm.setInt(1,stockInt);       //first value to first parameter
            pstm.setString(2,bookTitleStr);         //second value to second parameter
            numUpd = pstm.executeUpdate();   //first update
            pstm.close();                    //Close the PreparedStatement object
            
            pstm = con.prepareStatement(
            "DELETE FROM Books WHERE Title=? AND Stock<1"); //if the stock is below one
            pstm.setString(1,bookTitleStr);       //first value to first parameter
            numUpd = pstm.executeUpdate();   //first update
            pstm.close();                    //Close the PreparedStatement object
            
            pstm = con.prepareStatement(
            "UPDATE BooksIssued SET BookStock = BookStock - ? WHERE BookTitle=? AND UserName = ?"); //updates book stock
            pstm.setInt(1,stockInt);       //first value to first parameter
            pstm.setString(2,bookTitleStr);         //second value to second parameter
            pstm.setString(3,personNameStr);         //second value to second parameter
            numUpd = pstm.executeUpdate();   //first update
            pstm.close();                    //Close the PreparedStatement object
            }
         catch (SQLException sqlex) {
            System.err.println("SQL statement issue " + sqlex.getMessage());
        } finally {

           try {
                if (null != stm) {
                    stm.close();
                }
                if (null != con) {
                    con.close();
                }
            } catch (SQLException sqlex) {
                System.err.println(sqlex.getMessage());
            }
        }
    }
            
        if (bookTitleStr.equals(question_description) && stockInt<=new_question_description2 && "".equals(question_description3) ) { //if book is not in issuedbooks
            JOptionPane.showMessageDialog(null, bookTitleStr + " issued successfully to " + personNameStr);
            System.out.println("second loop");
            try {
            con = DriverManager.getConnection(dbURL, "", "");
            
            pstm = con.prepareStatement(
            "UPDATE Books SET Stock = Stock + ? WHERE Title=?"); //query to update stock
            pstm.setInt(1,stockInt);       //first value to first parameter
            pstm.setString(2,bookTitleStr);         //second value to second parameter
            numUpd = pstm.executeUpdate();   //first update
            pstm.close();                    //Close the PreparedStatement object
            
            pstm = con.prepareStatement(
            "INSERT INTO Books (Title, Authour, Publisher, Genre, Price, Stock) VALUES (?, ?, ?, ?, ?, ?)");
            pstm.setString(1,bookTitleStr);       //first value to first parameter
            pstm.setString(2,bookAuthourStr);         //second value to second parameter
            pstm.setString(3,bookPublisherStr);       //third value to third parameter
            pstm.setString(4,bookGenreStr);         //fourth value to fourth parameter
            pstm.setString(5,bookPriceStr);       //fifth value to fifth parameter
            pstm.setInt(6,stockInt);       //fifth value to fifth parameter
            numUpd = pstm.executeUpdate();   //first update
            pstm.close();                    //Close the PreparedStatement object
            
            question_description="";
            }
         catch (SQLException sqlex) {
            System.err.println("SQL statement issue " + sqlex.getMessage());
        } finally {

           try {
                if (null != stm) {
                    stm.close();
                }
                if (null != con) {
                    con.close();
                }
            } catch (SQLException sqlex) {
                System.err.println(sqlex.getMessage());
            }
        }
    }
    question_description=""; //resetting variables to empty strings
    question_descriptionextra=""; 
    question_description2=""; 
    question_description3=""; 
    question_description4="";     
    }
    
        //ReturnRentedBook, used to return a book rented by the user back to the library
        public static void ReturnRentedBook(String bookTitleStr, String bookAuthourStr, String bookPublisherStr, String bookGenreStr, String bookPriceStr, Integer stockInt, String personNameStr, String personAddressStr, String personEmailStr, String personPhoneStr) { //JDBCConnect2 function, used for login, to store student name and student id in student table of database     
        String question_description3=""; 
        String question_description4="";
        String question_descriptionr="";
        
        try {
            con = DriverManager.getConnection(dbURL, "", "");
            pstm = con.prepareStatement(
            "SELECT BookTitle from BooksIssued where BookTitle=? and UserName=?;");
            pstm.setString(1,bookTitleStr);       //first value to first parameter
            pstm.setString(2,personNameStr);       //first value to first parameter
            rs2 = pstm.executeQuery();   //first update
            while (rs2.next()) {         //Position the cursor
            question_description = rs2.getString(1);
            }
            rs2.close();                    //Close the PreparedStatement object
            pstm.close();                    //Close the PreparedStatement object
            
            pstm = con.prepareStatement(
            "SELECT UserName from BooksIssued where BookTitle=? and UserName=?;");
            pstm.setString(1,bookTitleStr);       //first value to first parameter
            pstm.setString(2,personNameStr);       //first value to first parameter
            rs2 = pstm.executeQuery();   //first update
            while (rs2.next()) {         //Position the cursor
            question_descriptionextra = rs2.getString(1);
            }
            rs2.close();                    //Close the PreparedStatement object
            pstm.close();                    //Close the PreparedStatement object
            
            pstm = con.prepareStatement(
            "SELECT BookStock from BooksIssued where BookTitle=? and UserName=?;");
            pstm.setString(1,bookTitleStr);       //first value to first parameter
            pstm.setString(2,personNameStr);       //first value to first parameter
            rs2 = pstm.executeQuery();   //first update
            while (rs2.next()) {         //Position the cursor
            question_description22 = rs2.getString(1);
            }
            rs2.close();                    //Close the PreparedStatement object
            pstm.close();                    //Close the PreparedStatement object
            
            pstm = con.prepareStatement(
            "SELECT Title from Books where Title=?;");
            pstm.setString(1,bookTitleStr);       //first value to first parameter
            rs2 = pstm.executeQuery();   //first update
            while (rs2.next()) {         //Position the cursor
            question_description3 = rs2.getString(1);
            }
            rs2.close();                    //Close the PreparedStatement object
            pstm.close();                    //Close the PreparedStatement object
            
            pstm = con.prepareStatement(
            "SELECT Name from Users where Name=? and Address=? and Email=? and Phone=?;");
            pstm.setString(1,personNameStr);       //first value to first parameter
            pstm.setString(2,personAddressStr);       //first value to first parameter
            pstm.setString(3,personEmailStr);       //first value to first parameter
            pstm.setString(4,personPhoneStr);       //first value to first parameter
            rs2 = pstm.executeQuery();   //first update
            while (rs2.next()) {         //Position the cursor
            question_descriptionr = rs2.getString(1);
            }
            rs2.close();                    //Close the PreparedStatement object
            pstm.close();                    //Close the PreparedStatement object
                    } catch (SQLException sqlex) {
            System.err.println("SQL statement issue " + sqlex.getMessage());
        } finally {

           try {
                if (null != stm) {
                    stm.close();
                }
                if (null != con) {
                    con.close();
                }
            } catch (SQLException sqlex) {
                System.err.println(sqlex.getMessage());
            }
        }
        //JOptionPane.showMessageDialog(null, question_description);
        //JOptionPane.showMessageDialog(null, question_descriptionextra);
        //JOptionPane.showMessageDialog(null, question_description22);
        //JOptionPane.showMessageDialog(null, question_description3);
        //question_description="";
        //question_descriptionextra="";
        //question_description2="";
        //question_description3="";
            
            if (!bookTitleStr.equals(question_description) || !personNameStr.equals(question_descriptionextra)){ //if the book has not been issued
                JOptionPane.showMessageDialog(null, "Could not find this issued book. Please make sure the fields are correct");
                return;
            }
            
            if (!personNameStr.equals(question_descriptionr)){ //if the user does not belong in the users table
                JOptionPane.showMessageDialog(null, "This user does not belong in the database");
                return;
            }
            
             int new_question_description2 = Integer.parseInt(question_description22); //converts string to int
            
            if (stockInt>new_question_description2){ //if the stock entered is greater than what is available
                JOptionPane.showMessageDialog(null, "The database do not have this quantity of books available for purchase/rent");
            }

            if (bookTitleStr.equals(question_description) && stockInt<=new_question_description2 && bookTitleStr.equals(question_description3)) { //if book is in issued books
            JOptionPane.showMessageDialog(null, personNameStr + " successfully returned " + bookTitleStr);
            System.out.println("first loop");
            try {
            con = DriverManager.getConnection(dbURL, "", "");
            pstm = con.prepareStatement(
            "UPDATE Books SET Stock = Stock + ? WHERE Title=?");
            pstm.setInt(1,stockInt);       //first value to first parameter
            pstm.setString(2,bookTitleStr);         //second value to second parameter
            numUpd = pstm.executeUpdate();   //first update
            pstm.close();                    //Close the PreparedStatement object
            
            pstm = con.prepareStatement(
            "DELETE FROM Books WHERE Title=? AND Stock<1");
            pstm.setString(1,bookTitleStr);       //first value to first parameter
            numUpd = pstm.executeUpdate();   //first update
            pstm.close();                    //Close the PreparedStatement object
            
            pstm = con.prepareStatement(
            "UPDATE BooksIssued SET BookStock = BookStock - ? WHERE BookTitle=? AND UserName = ?");
            pstm.setInt(1,stockInt);       //first value to first parameter
            pstm.setString(2,bookTitleStr);         //second value to second parameter
            pstm.setString(3,personNameStr);         //second value to second parameter
            numUpd = pstm.executeUpdate();   //first update
            pstm.close();                    //Close the PreparedStatement object
            }
         catch (SQLException sqlex) {
            System.err.println("SQL statement issue " + sqlex.getMessage());
        } finally {

           try {
                if (null != stm) {
                    stm.close();
                }
                if (null != con) {
                    con.close();
                }
            } catch (SQLException sqlex) {
                System.err.println(sqlex.getMessage());
            }
        }
    }
            
        if (bookTitleStr.equals(question_description) && stockInt<=new_question_description2 && "".equals(question_description3) ) { //if book is not in issuedbooks
            JOptionPane.showMessageDialog(null, bookTitleStr + " issued successfully to " + personNameStr);
            System.out.println("second loop");
            try {
            con = DriverManager.getConnection(dbURL, "", "");
            
            pstm = con.prepareStatement(
            "UPDATE Books SET Stock = Stock + ? WHERE Title=?"); //query to update stock
            pstm.setInt(1,stockInt);       //first value to first parameter
            pstm.setString(2,bookTitleStr);         //second value to second parameter
            numUpd = pstm.executeUpdate();   //first update
            pstm.close();                    //Close the PreparedStatement object
            
            pstm = con.prepareStatement(
            "INSERT INTO Books (Title, Authour, Publisher, Genre, Price, Stock) VALUES (?, ?, ?, ?, ?, ?)"); //insert statement
            pstm.setString(1,bookTitleStr);       //first value to first parameter
            pstm.setString(2,bookAuthourStr);         //second value to second parameter
            pstm.setString(3,bookPublisherStr);       //third value to third parameter
            pstm.setString(4,bookGenreStr);         //fourth value to fourth parameter
            pstm.setString(5,bookPriceStr);       //fifth value to fifth parameter
            pstm.setInt(6,stockInt);       //fifth value to fifth parameter
            numUpd = pstm.executeUpdate();   //first update
            pstm.close();                    //Close the PreparedStatement object
            
            question_description=""; //reset variable
            
            //pstm = con.prepareStatement(
            //"DELETE FROM BooksIssued WHERE DatePurchased=(SELECT MAX(DatePurchased) FROM BooksIssued)");
            //numUpd = pstm.executeUpdate();   //first update
            //pstm.close();                    //Close the PreparedStatement object
            }
         catch (SQLException sqlex) {
            System.err.println("SQL statement issue " + sqlex.getMessage());
        } finally {

           try {
                if (null != stm) {
                    stm.close();
                }
                if (null != con) {
                    con.close();
                }
            } catch (SQLException sqlex) {
                System.err.println(sqlex.getMessage());
            }
        }
    }
    question_description=""; //resetting variables
    question_descriptionextra=""; 
    question_description2=""; 
    question_description3=""; 
    question_description4="";
    }
    
    //CorrectUserLoginName, finds the correct login name from the database
    public static String CorrectUserLoginName() { //returns correct q2

        try {
            con = DriverManager.getConnection(dbURL, "", "");
            
            pstm = con.prepareStatement(
            "SELECT username from Admin;");
            rs = pstm.executeQuery();   // Perform first update
            while (rs.next()) {
            the_correct_answer = rs.getString(1);        // Retrieve the first column value;
            }
            rs.close(); 
            pstm.close();                    // Close the PreparedStatement object
            
        } catch (SQLException sqlex) {
            System.err.println("SQL statement issue " + sqlex.getMessage());
        } finally {
            try {
                if (null != stm) {
                    // cleanup resources, once after processing
                    stm.close();
                }
                if (null != con) {
                    // and then finally close connection
                    con.close();
                }
            } catch (SQLException sqlex) {
                System.err.println(sqlex.getMessage());
            }
        }
        return the_correct_answer; //returns a string of the correct username found from the selected prepared statmeent
    }
        
    //CorrectUserLoginPassword, finds the correct login password from the database
    public static String CorrectUserLoginPassword() { //returns correct q2

        try {
            con = DriverManager.getConnection(dbURL, "", "");
            
            pstm = con.prepareStatement(
            "SELECT password from Admin;");
            rs = pstm.executeQuery();   // Perform first update
            while (rs.next()) {
            the_correct_answer = rs.getString(1);        // Retrieve the first column value;
            }
            rs.close(); 
            pstm.close();                    // Close the PreparedStatement object
            
        } catch (SQLException sqlex) {
            System.err.println("SQL statement issue " + sqlex.getMessage());
        } finally {
            try {
                if (null != stm) {
                    // cleanup resources, once after processing
                    stm.close();
                }
                if (null != con) {
                    // and then finally close connection
                    con.close();
                }
            } catch (SQLException sqlex) {
                System.err.println(sqlex.getMessage());
            }
        }
        return the_correct_answer; //returns a string of the correct password found from the selected prepared statmeent
    }
    
    //AddUser, adds a user to the user table in the database
    public static void AddUser(String userNameStr, String userAddressStr, String userEmailStr, String userPhoneStr) { //JDBCConnect2 function, used for login, to store student name and student id in student table of database
                    
        try {
            con = DriverManager.getConnection(dbURL, "", "");

            pstm = con.prepareStatement(
            "INSERT INTO Users (Name, Address, Email, Phone) VALUES (?, ?, ?, ?)"); //insert statement
            pstm.setString(1,userNameStr);       //first value to first parameter
            pstm.setString(2,userAddressStr);         //second value to second parameter
            pstm.setString(3,userEmailStr);       //third value to third parameter
            pstm.setString(4,userPhoneStr);         //fourth value to fourth parameter
            numUpd = pstm.executeUpdate();   //first update
            pstm.close();                    //Close the PreparedStatement object
            
        } catch (SQLException sqlex) {
            //System.err.println("SQL statement issue " + sqlex.getMessage());
        } finally {

            try {
                if (null != stm) {
                    stm.close();
                }
                if (null != con) {
                    con.close();
                }
            } catch (SQLException sqlex) {
                //System.err.println(sqlex.getMessage());
            }
        }
    }
    
    //RemoveUser, removes a user from the user table in the database
    public static void RemoveUser(String userNameStr, String userAddressStr, String userEmailStr, String userPhoneStr) { //JDBCConnect2 function, used for login, to store student name and student id in student table of database
        try {
            con = DriverManager.getConnection(dbURL, "", "");
            pstm = con.prepareStatement(
            "SELECT Name from Users where Name=? and Address=? and Email=? and Phone=?;"); //query to find user
            pstm.setString(1,userNameStr);       //first value to first parameter
            pstm.setString(2,userAddressStr);       //first value to first parameter
            pstm.setString(3,userEmailStr);       //first value to first parameter
            pstm.setString(4,userPhoneStr);       //first value to first parameter
            rs2 = pstm.executeQuery();   //first update
            while (rs2.next()) {         //Position the cursor
            question_descriptionr = rs2.getString(1);
            }
            rs2.close();                    //Close the PreparedStatement object
            pstm.close();                    //Close the PreparedStatement object
                    } catch (SQLException sqlex) {
            System.err.println("SQL statement issue " + sqlex.getMessage());
        } finally {

           try {
                if (null != stm) {
                    stm.close();
                }
                if (null != con) {
                    con.close();
                }
            } catch (SQLException sqlex) {
                System.err.println(sqlex.getMessage());
            }
        }
        //JOptionPane.showMessageDialog(null, question_descriptionr);
            
            if (!userNameStr.equals(question_descriptionr)){ //if the user is not in the users table
                JOptionPane.showMessageDialog(null, "This user is not contained in the database");
                return;
            }

            if (userNameStr.equals(question_descriptionr)) { //if book is in issued books
            JOptionPane.showMessageDialog(null, userNameStr + " successfully removed");   
        try {
            con = DriverManager.getConnection(dbURL, "", "");

            pstm = con.prepareStatement(
            "DELETE FROM Users WHERE Name=? AND Address=? AND Email=? AND Phone=?"); //delete user query
            pstm.setString(1,userNameStr);       //first value to first parameter
            pstm.setString(2,userAddressStr);         //second value to second parameter
            pstm.setString(3,userEmailStr);       //third value to third parameter
            pstm.setString(4,userPhoneStr);         //fourth value to fourth parameter
            numUpd = pstm.executeUpdate();   //first update
            pstm.close();                    //Close the PreparedStatement object
            
        } catch (SQLException sqlex) {
            System.err.println("SQL statement issue " + sqlex.getMessage());
        } finally {

            try {
                if (null != stm) {
                    stm.close();
                }
                if (null != con) {
                    con.close();
                }
            } catch (SQLException sqlex) {
                System.err.println(sqlex.getMessage());
            }
        }
    }question_descriptionr=""; //resets variable to empty string
    }
    
    
    
        
}
