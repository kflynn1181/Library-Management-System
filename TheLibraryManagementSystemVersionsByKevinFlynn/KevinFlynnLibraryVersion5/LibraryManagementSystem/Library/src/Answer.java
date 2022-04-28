/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author kevin
 */
public class Answer {
    private String user_answer;
    private String correct_answer;

    public Answer(String user_answer, String correct_answer) {
        this.user_answer = user_answer;
        this.correct_answer = correct_answer;
    }

    public void setUser_answer(String user_answer) {
        this.user_answer = user_answer;
    }

    public void setCorrect_answer(String correct_answer) {
        this.correct_answer = correct_answer;
    }

    public String getUser_answer() {
        return user_answer;
    }

    public String getCorrect_answer() {
        return correct_answer;
    }

    @Override
    public String toString() {
        return "Answer{" + "user_answer=" + user_answer + '}';
    }
    
}
