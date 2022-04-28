/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package gui_tutorial;

//Code created by Kevin Flynn C18502203, inspired by DataHandler.java code on Brightspace
//Code orginally submitted on 27th March
//Updated code submitted on 3rd April

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

    private static final String dbURL = "jdbc:ucanaccess://DatabaseTest15.accdb;sysSchema=true";
    private static java.sql.Connection con;
    private static java.sql.Statement stm;
    private static java.sql.PreparedStatement pstm;
    private static int numUpd;
    private static int strUpd;
    public static java.sql.ResultSet rs;
    public static java.sql.ResultSet rs2;
    private static java.sql.ResultSetMetaData rsMeta;
    private static int columnCount;
    private static int counter = 1;
    public static String question_description;
    public static String the_correct_answer;

    public static Vector<String> getTables() { //getTables function
        Vector<String> l = new Vector<>();
        /*l.add("Employee");
        l.add("Dependant");
        l.add("Department");
        l.add("Project");
        l.add("Workson");*/
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
    
        public static void JDBCConnect(String ageStr, String NumberCheck) { //JDBCConnect function, used to put user answers into database
        // Opening database connection
        try {
            con = DriverManager.getConnection(dbURL, "", "");

            // Creating JDBC Statement 
            pstm = con.prepareStatement(
            "update Questions set user_answer = ? where question_id = ?");
            pstm.setString(1,ageStr);        //first value to first parameter
            pstm.setString(2,NumberCheck);   //second value to second parameter
            numUpd = pstm.executeUpdate();   //execute update
            counter = counter + 1;
            pstm.close();                    // Close the PreparedStatement object 
        } catch (SQLException sqlex) {
            System.err.println("SQL statement issue " + sqlex.getMessage());
        } finally {

            // Closing database connection
            try {
                if (null != stm) {
                    // cleanup resources, once after processing
                    stm.close();
                }
                if (null != con) {
                    // close connection
                    con.close();
                }
            } catch (SQLException sqlex) {
                System.err.println(sqlex.getMessage());
            }
        }
    } // End of Constructor
    
        public static void JDBCConnect2(String nameStr, String idStr) { //JDBCConnect2 function, used for login, to store student name and student id in student table of database
                    
        try {
            con = DriverManager.getConnection(dbURL, "", "");

            pstm = con.prepareStatement(
            "INSERT INTO Student (student_name, student_id) VALUES (?, ?)");
            pstm.setString(1,nameStr);       //first value to first parameter
            pstm.setString(2,idStr);         //second value to second parameter
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
    }
        
        public static void JDBCConnect3(String marksStr, String idStr) { //JDBCConnect3, used to put total marks of user and the date the user finished the quiz
                    
        try {
            con = DriverManager.getConnection(dbURL, "", "");

            pstm = con.prepareStatement(
            "UPDATE Student SET marks = ? where student_id = ?");
            pstm.setString(1,marksStr);     //Assign first value to first parameter 
            pstm.setString(2,idStr);        //Assign first value to first parameter
            numUpd = pstm.executeUpdate();  //Perform first update
            pstm.close();                   //Close the PreparedStatement object 
            
            pstm = con.prepareStatement(
            "UPDATE Student SET dates = now() where student_id = ?");
            pstm.setString(1,idStr);        //Assign first value to first parameter
            numUpd = pstm.executeUpdate();  //Perform first update
            pstm.close();                   //Close the PreparedStatement object    
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
    }
        
            public static void JDBCConnect4() { //JDBCConnect4, used to update the date using the now function
                    
            try {
            con = DriverManager.getConnection(dbURL, "", "");

            pstm = con.prepareStatement(
            "update Student set dates = now()");
            numUpd = pstm.executeUpdate();   //Perform first update
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
    }
                  
        public static void JDBCConnect5() { //JDBCConnect5, used to reset the user answers to empty

        try {
            con = DriverManager.getConnection(dbURL, "", "");
            
            pstm = con.prepareStatement(
            "update Questions set user_answer = Null");
            numUpd = pstm.executeUpdate();   //Perform first update
            pstm.close();                    //Close the PreparedStatement object 
            
        } catch (SQLException sqlex) {
            System.err.println("SQL statement issue " + sqlex.getMessage());
        } finally {

            // Closing database connection
            try {
                if (null != stm) {
                    // cleanup resources, once after processing
                    stm.close();
                }
                if (null != con) {
                    // close connection
                    con.close();
                }
            } catch (SQLException sqlex) {
                System.err.println(sqlex.getMessage());
            }
        }
    } // End of Constructor
        
        public static void JDBCConnectGetQuestions(javax.swing.JLabel jLabel4, javax.swing.JLabel jLabel6, javax.swing.JLabel jLabel8, javax.swing.JLabel jLabel10, javax.swing.JLabel jLabel12, javax.swing.JLabel jLabel14, javax.swing.JLabel jLabel16, javax.swing.JLabel jLabel18, javax.swing.JLabel jLabel20, javax.swing.JLabel jLabel22) {
        //JDBCConnectGetQuestions, used to get questions and display them on the jpanels
        try {
            con = DriverManager.getConnection(dbURL, "", "");
            
            pstm = con.prepareStatement(
            "SELECT question_description from Questions WHERE question_id=1");
            rs2 = pstm.executeQuery();   //Perform first update
            while (rs2.next()) {         //Position the cursor
                question_description = rs2.getString(1);        //Retrieve the first column value
                jLabel4.setText(question_description);
            }
            rs2.close(); 
            pstm.close();                    // Close the PreparedStatement object
            
            pstm = con.prepareStatement(
            "SELECT question_description from Questions WHERE question_id=2");
            rs2 = pstm.executeQuery();   //Perform first update
            while (rs2.next()) {         //Position the cursor
                question_description = rs2.getString(1);        //Retrieve the first column value
                jLabel6.setText(question_description);
            }
            rs2.close(); 
            pstm.close();                    //Close the PreparedStatement object
            
            pstm = con.prepareStatement(
            "SELECT question_description from Questions WHERE question_id=3");
            rs2 = pstm.executeQuery();   //Perform first update
            while (rs2.next()) {               //Position the cursor
                question_description = rs2.getString(1);        //Retrieve the first column value
                jLabel8.setText(question_description);
            }
            rs2.close(); 
            pstm.close();                    // Close the PreparedStatement object
            
            pstm = con.prepareStatement(
            "SELECT question_description from Questions WHERE question_id=4");
            rs2 = pstm.executeQuery();   //Perform first update
            while (rs2.next()) {               //Position the cursor
                question_description = rs2.getString(1);        //Retrieve the first column value
                jLabel10.setText(question_description);
            }
            rs2.close(); 
            pstm.close();                    //Close the PreparedStatement object
            
            pstm = con.prepareStatement(
            "SELECT question_description from Questions WHERE question_id=5");
            rs2 = pstm.executeQuery();   // Perform first update
            while (rs2.next()) {               // Position the cursor
                question_description = rs2.getString(1);        //Retrieve the first column value
                jLabel12.setText(question_description);
            }
            rs2.close(); 
            pstm.close();                    //Close the PreparedStatement object
            
            pstm = con.prepareStatement(
            "SELECT question_description from Questions WHERE question_id=6");
            rs2 = pstm.executeQuery();   //Perform first update
            while (rs2.next()) {               //Position the cursor
                question_description = rs2.getString(1);        //Retrieve the first column value
                jLabel14.setText(question_description);
            }
            rs2.close(); 
            pstm.close();                    //Close the PreparedStatement object
            
            pstm = con.prepareStatement(
            "SELECT question_description from Questions WHERE question_id=7");
            rs2 = pstm.executeQuery();   //Perform first update
            while (rs2.next()) {               //Position the cursor
                question_description = rs2.getString(1);        //Retrieve the first column value
                jLabel16.setText(question_description);
            }
            rs2.close(); 
            pstm.close();                    //Close the PreparedStatement object
            
            pstm = con.prepareStatement(
            "SELECT question_description from Questions WHERE question_id=8");
            rs2 = pstm.executeQuery();   //Perform first update
            while (rs2.next()) {               //Position the cursor
                question_description = rs2.getString(1);        //Retrieve the first column value
                jLabel18.setText(question_description);
            }
            rs2.close(); 
            pstm.close();                    // Close the PreparedStatement object
            
            pstm = con.prepareStatement(
            "SELECT question_description from Questions WHERE question_id=9");
            rs2 = pstm.executeQuery();   // Perform first update
            while (rs2.next()) {               // Position the cursor                  4 
                question_description = rs2.getString(1);        // Retrieve the first column value
                jLabel20.setText(question_description);
            }
            rs2.close(); 
            pstm.close();                    //Close the PreparedStatement object
            
            pstm = con.prepareStatement(
            "SELECT question_description from Questions WHERE question_id=10");
            rs2 = pstm.executeQuery();   //Perform first update
            while (rs2.next()) {               // Position the cursor
                question_description = rs2.getString(1);        //Retrieve the first column value
                jLabel22.setText(question_description);
            }
            rs2.close(); 
            pstm.close();                    //Close the PreparedStatement object
            
        } catch (SQLException sqlex) {
            System.err.println("SQL statement issue " + sqlex.getMessage());
        } finally {

            // Closing database connection
            try {
                if (null != stm) {
                    // cleanup resources, once after processing
                    stm.close();
                }
                if (null != con) {
                    // close connection
                    con.close();
                }
            } catch (SQLException sqlex) {
                System.err.println(sqlex.getMessage());
            }
        }
    } // End of Constructor
 
    public static String JDBCConnectGetCorrectQ1() { //returns correct q1

        try {
            con = DriverManager.getConnection(dbURL, "", "");
            
            pstm = con.prepareStatement(
            "SELECT correct_answer from Questions WHERE question_id=1");
            rs = pstm.executeQuery();   // Perform first update
            while (rs.next()) {
            the_correct_answer = rs.getString(1);        // Retrieve the first column value;
            }
            rs.close(); 
            pstm.close();                    // Close the PreparedStatement object
            
        } catch (SQLException sqlex) {
            System.err.println("SQL statement issue " + sqlex.getMessage());
        } finally {

            // Step 3: Closing database connection
            try {
                if (null != stm) {
                    // cleanup resources, once after processing
                    stm.close();
                }
                if (null != con) {
                    // close connection
                    con.close();
                }
            } catch (SQLException sqlex) {
                System.err.println(sqlex.getMessage());
            }
        }
        return the_correct_answer; 
    } // End of Constructor

    public static String JDBCConnectGetCorrectQ2() { //returns correct q2

        try {
            con = DriverManager.getConnection(dbURL, "", "");
            
            pstm = con.prepareStatement(
            "SELECT correct_answer from Questions WHERE question_id=2");
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
        return the_correct_answer; 
    }
    
    public static String JDBCConnectGetCorrectQ3() { //returns correct q3

        try {
            con = DriverManager.getConnection(dbURL, "", "");
            
            pstm = con.prepareStatement(
            "SELECT correct_answer from Questions WHERE question_id=3");
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
        return the_correct_answer; 
    }
    
    public static String JDBCConnectGetCorrectQ4() { //returns correct q4

        try {
            con = DriverManager.getConnection(dbURL, "", "");
            
            pstm = con.prepareStatement(
            "SELECT correct_answer from Questions WHERE question_id=4");
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
        return the_correct_answer; 
    }
        
    public static String JDBCConnectGetCorrectQ5() { //returns correct q5

        try {
            con = DriverManager.getConnection(dbURL, "", "");
            
            pstm = con.prepareStatement(
            "SELECT correct_answer from Questions WHERE question_id=5");
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
        return the_correct_answer; 
    }
    
        public static String JDBCConnectGetCorrectQ6() { //returns correct q6

        try {
            con = DriverManager.getConnection(dbURL, "", "");
            
            pstm = con.prepareStatement(
            "SELECT correct_answer from Questions WHERE question_id=6");
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
        return the_correct_answer; 
    }

    public static String JDBCConnectGetCorrectQ7() { //returns correct q7

        try {
            con = DriverManager.getConnection(dbURL, "", "");
            
            pstm = con.prepareStatement(
            "SELECT correct_answer from Questions WHERE question_id=7");
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
        return the_correct_answer; 
    }
    
    public static String JDBCConnectGetCorrectQ8() { //returns correct q8

        try {
            con = DriverManager.getConnection(dbURL, "", "");
            
            pstm = con.prepareStatement(
            "SELECT correct_answer from Questions WHERE question_id=8");
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
        return the_correct_answer; 
    }
    
    public static String JDBCConnectGetCorrectQ9() { //returns correct q9

        try {
            con = DriverManager.getConnection(dbURL, "", "");
            
            pstm = con.prepareStatement(
            "SELECT correct_answer from Questions WHERE question_id=9");
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
        return the_correct_answer; 
    }
    
    public static String JDBCConnectGetCorrectQ10() { //returns correct q10

        try {
            con = DriverManager.getConnection(dbURL, "", "");
            
            pstm = con.prepareStatement(
            "SELECT correct_answer from Questions WHERE question_id=10");
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
        return the_correct_answer; 
    }
    
    
    public static void AddBook(String bookTitleStr, String bookAuthourStr, String bookPublisherStr, String bookGenreStr, String bookPriceStr) { //JDBCConnect2 function, used for login, to store student name and student id in student table of database
                    
        try {
            con = DriverManager.getConnection(dbURL, "", "");

            pstm = con.prepareStatement(
            "INSERT INTO Books (Title, Authour, Publisher, Genre, Price, Stock) VALUES (?, ?, ?, ?, ?, 1)");
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
    }
    
        public static void RemoveBook(String bookTitleStr, String bookAuthourStr, String bookPublisherStr, String bookGenreStr, String bookPriceStr) { //JDBCConnect2 function, used for login, to store student name and student id in student table of database
                    
        try {
            con = DriverManager.getConnection(dbURL, "", "");

            pstm = con.prepareStatement(
            "DELETE FROM Books WHERE Title=? AND Authour=? AND Publisher=? AND Genre=? AND Price=? AND Stock=1");
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
    }
    
    public static void IssueBookForBuy(String bookTitleStr, String bookAuthourStr, String bookPublisherStr, String bookGenreStr, String bookPriceStr, String personNameStr, String personAddressStr, String personEmailStr, String personPhoneStr) { //JDBCConnect2 function, used for login, to store student name and student id in student table of database
                    
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
            
            //rs2 = pstm.executeQuery();   //Perform first update
            //while (rs2.next()) {         //Position the cursor
                //question_description = rs2.getString(1);        //Retrieve the first column value
                //jLabel4.setText(question_description);
            //}

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
            
        JOptionPane.showMessageDialog(null, question_description);
            
            if (!bookTitleStr.equals(question_description)){
                JOptionPane.showMessageDialog(null, "This book is not available for purchase as it is not contained in our database");
            }
            
            //if (!bookTitleStr.equals(question_description)){
                //JOptionPane.showMessageDialog(null, "This book is not available for purchase as it is not contained in our database");
            //}
            
            //JOptionPane.showMessageDialog(null, "This book is not available for purchase as it is not contained in our database");
            //JOptionPane.showMessageDialog(null, "This book is not available for purchase as it is not contained in our database");
            
            if (bookTitleStr.equals(question_description)) {
            JOptionPane.showMessageDialog(null, bookTitleStr + " issued successfully to " + personNameStr);
            try {
            con = DriverManager.getConnection(dbURL, "", "");
            pstm = con.prepareStatement(
            "DELETE FROM Books WHERE Title=?");
            pstm.setString(1,bookTitleStr);       //first value to first parameter
            numUpd = pstm.executeUpdate();   //first update
            pstm.close();                    //Close the PreparedStatement object
            
            pstm = con.prepareStatement(
            "INSERT INTO BooksIssued (BookTitle, BookAuthour, BookPublisher, BookGenre, BookPrice, UserName, UserAddress, UserEmail, UserPhone, BuyOrRent, DatePurchased) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, 'Buy', now())");
            pstm.setString(1,bookTitleStr);       //first value to first parameter
            pstm.setString(2,bookAuthourStr);         //second value to second parameter
            pstm.setString(3,bookPublisherStr);       //third value to third parameter
            pstm.setString(4,bookGenreStr);         //fourth value to fourth parameter
            pstm.setString(5,bookPriceStr);       //fifth value to fifth parameter
            pstm.setString(6,personNameStr);         //second value to second parameter
            pstm.setString(7,personAddressStr);       //third value to third parameter
            pstm.setString(8,personEmailStr);         //fourth value to fourth parameter
            pstm.setString(9,personPhoneStr);       //fifth value to fifth parameter
            numUpd = pstm.executeUpdate();   //first update
            pstm.close();                    //Close the PreparedStatement object
            }
            //}
            
            //if (bookTitleStr.equals(bookTitleStr)){
                //JOptionPane.showMessageDialog(null, "This book is not available for purchase as it is not contained in our database");
            //}
            
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
            //if (!bookTitleStr.equals(question_description)){
                //JOptionPane.showMessageDialog(null, "This book is not available for purchase as it is not contained in our database");
            //}
    }}
       
    
     public static void IssueBookForRent(String bookTitleStr, String bookAuthourStr, String bookPublisherStr, String bookGenreStr, String bookPriceStr, String personNameStr, String personAddressStr, String personEmailStr, String personPhoneStr) { //JDBCConnect2 function, used for login, to store student name and student id in student table of database
                    
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
            
            //rs2 = pstm.executeQuery();   //Perform first update
            //while (rs2.next()) {         //Position the cursor
                //question_description = rs2.getString(1);        //Retrieve the first column value
                //jLabel4.setText(question_description);
            //}

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
            
        JOptionPane.showMessageDialog(null, question_description);
            
            if (!bookTitleStr.equals(question_description)){
                JOptionPane.showMessageDialog(null, "This book is not available for rent as it is not contained in our database");
            }
            
            //if (!bookTitleStr.equals(question_description)){
                //JOptionPane.showMessageDialog(null, "This book is not available for purchase as it is not contained in our database");
            //}
            
            //JOptionPane.showMessageDialog(null, "This book is not available for purchase as it is not contained in our database");
            //JOptionPane.showMessageDialog(null, "This book is not available for purchase as it is not contained in our database");
            
            if (bookTitleStr.equals(question_description)) {
            JOptionPane.showMessageDialog(null, bookTitleStr + " issued successfully to " + personNameStr);
            try {
            con = DriverManager.getConnection(dbURL, "", "");
            pstm = con.prepareStatement(
            "INSERT INTO BooksIssued (BookTitle, BookAuthour, BookPublisher, BookGenre, BookPrice, UserName, UserAddress, UserEmail, UserPhone, BuyOrRent, DateForReturn ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, 'Rent',  DateAdd ('ww', 1, now()))");
            pstm.setString(1,bookTitleStr);       //first value to first parameter
            pstm.setString(2,bookAuthourStr);         //second value to second parameter
            pstm.setString(3,bookPublisherStr);       //third value to third parameter
            pstm.setString(4,bookGenreStr);         //fourth value to fourth parameter
            pstm.setString(5,bookPriceStr);       //fifth value to fifth parameter
            pstm.setString(6,personNameStr);         //second value to second parameter
            pstm.setString(7,personAddressStr);       //third value to third parameter
            pstm.setString(8,personEmailStr);         //fourth value to fourth parameter
            pstm.setString(9,personPhoneStr);       //fifth value to fifth parameter
            numUpd = pstm.executeUpdate();   //first update
            pstm.close();                    //Close the PreparedStatement object
            }
            //}
            
            //if (bookTitleStr.equals(bookTitleStr)){
                //JOptionPane.showMessageDialog(null, "This book is not available for purchase as it is not contained in our database");
            //}
            
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
            //if (!bookTitleStr.equals(question_description)){
                //JOptionPane.showMessageDialog(null, "This book is not available for purchase as it is not contained in our database");
            //}
    }}
     
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
    
    public static void ReturnBoughtBook(String bookTitleStr, String bookAuthourStr, String bookPublisherStr, String bookGenreStr, String bookPriceStr, String personNameStr, String personAddressStr, String personEmailStr, String personPhoneStr) { //JDBCConnect2 function, used for login, to store student name and student id in student table of database
                    
        try {
            con = DriverManager.getConnection(dbURL, "", "");

            pstm = con.prepareStatement(
            "SELECT BookTitle from BooksIssued where BookTitle=?;");
            pstm.setString(1,bookTitleStr);       //first value to first parameter
            rs2 = pstm.executeQuery();   //first update
            while (rs2.next()) {         //Position the cursor
            question_description = rs2.getString(1);
            }
            rs2.close();                    //Close the PreparedStatement object
            pstm.close();                    //Close the PreparedStatement object
            
            //rs2 = pstm.executeQuery();   //Perform first update
            //while (rs2.next()) {         //Position the cursor
                //question_description = rs2.getString(1);        //Retrieve the first column value
                //jLabel4.setText(question_description);
            //}

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
            
        JOptionPane.showMessageDialog(null, question_description);
            
            if (!bookTitleStr.equals(question_description)){
                JOptionPane.showMessageDialog(null, "Could not find this book in our 'BooksIssued' table");
            }
            
            //if (!bookTitleStr.equals(question_description)){
                //JOptionPane.showMessageDialog(null, "This book is not available for purchase as it is not contained in our database");
            //}
            
            //JOptionPane.showMessageDialog(null, "This book is not available for purchase as it is not contained in our database");
            //JOptionPane.showMessageDialog(null, "This book is not available for purchase as it is not contained in our database");
            
            if (bookTitleStr.equals(question_description)) {
            JOptionPane.showMessageDialog(null, personNameStr + " has returned " + bookTitleStr + " to the library");
            try {
            con = DriverManager.getConnection(dbURL, "", "");
            pstm = con.prepareStatement(
            "INSERT INTO Books (Title, Authour, Publisher, Genre, Price, Stock) VALUES (?, ?, ?, ?, ?, 1)");
            pstm.setString(1,bookTitleStr);       //first value to first parameter
            pstm.setString(2,bookAuthourStr);         //second value to second parameter
            pstm.setString(3,bookPublisherStr);       //third value to third parameter
            pstm.setString(4,bookGenreStr);         //fourth value to fourth parameter
            pstm.setString(5,bookPriceStr);       //fifth value to fifth parameter
            numUpd = pstm.executeUpdate();   //first update
            pstm.close();                    //Close the PreparedStatement object
            
            pstm = con.prepareStatement(
            "DELETE FROM BooksIssued WHERE BookTitle=? AND UserName=?");
            pstm.setString(1,bookTitleStr);       //first value to first parameter
            pstm.setString(2,personNameStr);         //second value to second parameter
            numUpd = pstm.executeUpdate();   //first update
            pstm.close();                    //Close the PreparedStatement object
            }
            //}
            
            //if (bookTitleStr.equals(bookTitleStr)){
                //JOptionPane.showMessageDialog(null, "This book is not available for purchase as it is not contained in our database");
            //}
            
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
            //if (!bookTitleStr.equals(question_description)){
                //JOptionPane.showMessageDialog(null, "This book is not available for purchase as it is not contained in our database");
            //}
    }}
    
        public static void ReturnRentedBook(String bookTitleStr, String bookAuthourStr, String bookPublisherStr, String bookGenreStr, String bookPriceStr, String personNameStr, String personAddressStr, String personEmailStr, String personPhoneStr) { //JDBCConnect2 function, used for login, to store student name and student id in student table of database
                    
        try {
            con = DriverManager.getConnection(dbURL, "", "");

            pstm = con.prepareStatement(
            "SELECT BookTitle from BooksIssued where BookTitle=?;");
            pstm.setString(1,bookTitleStr);       //first value to first parameter
            rs2 = pstm.executeQuery();   //first update
            while (rs2.next()) {         //Position the cursor
            question_description = rs2.getString(1);
            }
            rs2.close();                    //Close the PreparedStatement object
            pstm.close();                    //Close the PreparedStatement object
            
            //rs2 = pstm.executeQuery();   //Perform first update
            //while (rs2.next()) {         //Position the cursor
                //question_description = rs2.getString(1);        //Retrieve the first column value
                //jLabel4.setText(question_description);
            //}

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
            
        JOptionPane.showMessageDialog(null, question_description);
            
            if (!bookTitleStr.equals(question_description)){
                JOptionPane.showMessageDialog(null, "Could not find this book in our 'BooksIssued' table");
            }
            
            //if (!bookTitleStr.equals(question_description)){
                //JOptionPane.showMessageDialog(null, "This book is not available for purchase as it is not contained in our database");
            //}
            
            //JOptionPane.showMessageDialog(null, "This book is not available for purchase as it is not contained in our database");
            //JOptionPane.showMessageDialog(null, "This book is not available for purchase as it is not contained in our database");
            
            if (bookTitleStr.equals(question_description)) {
            JOptionPane.showMessageDialog(null, personNameStr + " has returned " + bookTitleStr + " to the library");
            try {
            con = DriverManager.getConnection(dbURL, "", "");
            pstm = con.prepareStatement(
            "DELETE FROM BooksIssued WHERE BookTitle=? AND UserName=?");
            pstm.setString(1,bookTitleStr);       //first value to first parameter
            pstm.setString(2,personNameStr);         //second value to second parameter
            numUpd = pstm.executeUpdate();   //first update
            pstm.close();                    //Close the PreparedStatement object
            }
            //}
            
            //if (bookTitleStr.equals(bookTitleStr)){
                //JOptionPane.showMessageDialog(null, "This book is not available for purchase as it is not contained in our database");
            //}
            
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
            //if (!bookTitleStr.equals(question_description)){
                //JOptionPane.showMessageDialog(null, "This book is not available for purchase as it is not contained in our database");
            //}
    }}
    
}
