package org.as.am.crucible;

import org.json.simple.JSONArray;
import org.as.am.dao.MongoDBAccountDAO;

public class Test {
    /**
     * Main
     * @param args
     *     Program arguments
     */
    public static void main(String[] args) {
        MongoDBAccountDAO dao = new MongoDBAccountDAO();
        JSONArray accounts = dao.findAccounts("test", null);
        System.out.println("\n" + accounts.toJSONString());
    }
}