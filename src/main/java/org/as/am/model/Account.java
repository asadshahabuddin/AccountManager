package org.as.am.model;

import java.util.List;
import java.util.ArrayList;

public class Account {
    private String name;
    private String email;
    private String username;
    private String password;
    private String pin;
    private List<QuestionAnswer> qAList;

    /**
     * Constructor
     * @param name
     *     Account name
     * @param email
     *     Email address
     * @param username
     *     Username
     * @param password
     *     Password
     * @param pin
     *     PIN
     * @param qAList
     *     List of secret questions and answers
     */
    public Account(String name,
                   String email,
                   String username,
                   String password,
                   String pin,
                   List<QuestionAnswer> qAList) {
        this.name     = name;
        this.email    = email;
        this.username = username;
        this.password = password;
        this.pin      = pin;
        this.qAList   = qAList;
    }

    /**
     * Set account name
     * @param name
     *     Account name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get account name
     * @return
     *     Account name
     */
    public String getName() {
        return name;
    }

    /**
     * Set email address
     * @param email
     *     Email address
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Get email address
     * @return
     *     Email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Set username
     * @param username
     *     Username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Get username
     * @return
     *    Username
     */
    public String getUsername() {
        return username;
    }


    /**
     * Set password
     * @param password
     *     Password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Get password
     * @return
     *     Password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Set PIN
     * @param pin
     *     PIN
     */
    public void setPin(String pin) {
        this.pin = pin;
    }

    /**
     * Get PIN
     * @return
     *     PIN
     */
    public String getPin() {
        return pin;
    }

    /**
     * Add an entry to the list of secret questions and answers
     * @param qa
     *     Secret question and answer entry
     */
    public void addQA(QuestionAnswer qa) {
        if(qAList == null) {
            qAList = new ArrayList<QuestionAnswer>();
        }
        qAList.add(qa);
    }

    /**
     * Get the list of secret questions and answers
     * @return
     *     List of secret questions and answers
     */
    public List<QuestionAnswer> getQAList() {
        return qAList;
    }
}