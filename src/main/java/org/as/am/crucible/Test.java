package org.as.am.crucible;

import org.as.am.model.Account;
import org.as.am.dao.MongoDBAccountDAO;

public class Test {
    /**
     * Main
     * @param args
     *     Program arguments
     */
    public static void main(String[] args) {
        MongoDBAccountDAO dao = new MongoDBAccountDAO();
        Account account = new Account();

        // Populate account details
        account.setName("Test");
        account.setEmail("john.doe@gmail.com");
        account.setUsername("johndoe");
        account.setPassword("password");
        account.setPin("123456");

        // Insert account
        System.out.println("\n>Insert account\nSuccess - " +
                           dao.insertAccount(account));
        System.out.println("\n>Find accounts details\nAccount name - Test\n" +
                           dao.findAccountDetails("Test", null).toJSONString());

        // List accounts
        System.out.println("\n>Find accounts\n" + dao.findAccounts());

        // Update account details
        account.setEmail("jane.doe@gmail.com");
        account.setUsername("janedoe");

        // Update account
        System.out.println("\n>Update account\nSuccess - " +
                           dao.updateAccount("59a3826fc53ffc5f6512fd71", account));
        System.out.println("\n>Find accounts details\nAccount name - Test\n" +
                           dao.findAccountDetails("Test", null).toJSONString());

        // Delete account
        System.out.println("\n>Delete account\nSuccess - " +
                           dao.deleteAccount("59a3826fc53ffc5f6512fd71"));
        System.out.println("\n>Find accounts details\nAccount name - Test\n" +
                           dao.findAccountDetails("Test", null).toJSONString());
    }
}