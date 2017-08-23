package org.as.am.model;

public class QuestionAnswer {
    private String question;
    private String answer;

    /**
     * Constructor
     * @param question
     *     Secret question
     * @param answer
     *     Secret answer
     */
    QuestionAnswer(String question, String answer) {
        this.question = question;
        this.answer   = answer;
    }

    /**
     * Get secret question
     * @return
     *     Secret question
     */
    public String getQuestion() {
        return question;
    }

    /**
     * Get secret answer
     * @return
     *     Secret answer
     */
    public String getAnswer() {
        return answer;
    }
}