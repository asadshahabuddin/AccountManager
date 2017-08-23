package org.as.am.crucible;

import org.json.simple.JSONObject;
import org.as.am.dao.MongoDBAccountDAO;

public class Test {
    /**
     * Main
     * @param args
     *     Program arguments
     */
    public static void main(String[] args) {
        MongoDBAccountDAO dao = new MongoDBAccountDAO();
        JSONObject account = dao.findAccount("test", null);
        System.out.println("\n" + account);
    }
}