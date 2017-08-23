package org.as.am.dao;

import org.as.am.model.Account;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public interface AccountDAO {
    /**
     * Insert an account
     * @param account
     *     Account
     * @return
     *     true iff the account was inserted
     */
    boolean insertAccount(Account account);

    /**
     * Update an account
     * @param account
     *     Account
     * @return
     *     true iff the account was updated
     */
    boolean updateAccount(Account account);

    /**
     * Delete an account
     * @param account
     *     Account
     * @return
     *     true iff the account was deleted
     */
    boolean deleteAccounts(Account account);

    /**
     * Find an account by account name and email address
     * @param accountName
     *     Account name
     * @param username
     *     Username
     * @return
     *     Account if it is found, null otherwise.
     */
    JSONObject findAccount(String accountName, String username);

    /**
     * Find all accounts by account name
     * @param accountName
     * @return
     *     Accounts if they are found, null otherwise.
     */
    JSONArray findAccounts(String accountName);
}