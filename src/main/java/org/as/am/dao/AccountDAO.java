package org.as.am.dao;

import org.as.am.model.Account;
import org.json.simple.JSONArray;

public interface AccountDAO {
    /**
     * Insert an account
     * There can be multiple accounts for an account name. However,
     * there can be only one account for a combination of account
     * name and email address.
     * @param account
     *     Account
     * @return
     *     true iff the account is inserted
     */
    boolean insertAccount(Account account);

    /**
     * Update an account
     * @param account
     *     Account
     * @return
     *     true iff the account is updated
     */
    boolean updateAccount(Account account);

    /**
     * Delete accounts by account name and email address (if provided)
     * @param account
     *     Account
     * @return
     *     true iff any account is deleted
     */
    boolean deleteAccounts(Account account);

    /**
     * Find all accounts by account name and email address (if provided)
     * @param accountName
     *     Account name
     * @param email
     *     Email address
     * @return
     *     Accounts, if any are found.
     */
    JSONArray findAccounts(String accountName, String email);
}