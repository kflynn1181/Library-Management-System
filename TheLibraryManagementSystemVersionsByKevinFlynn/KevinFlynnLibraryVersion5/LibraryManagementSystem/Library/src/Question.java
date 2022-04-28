/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author kevin
 */
public class Question {
    private String question;
    private int question_id;
    private Answer[] answers = new Answer[4];

    public Question(String question, int question_id) {
        this.question = question;
        this.question_id = question_id;
        //String SQL = "SELECT Answer FROM "
    }
    public static int getRandom(int iMin, int iMax){
        int iRand=0;
        iRand=(int)Math.round(Math.random()*(iMax-iMin))+iMin;
        return iRand;
    }
    
}
