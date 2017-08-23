package org.as.am.dao;

import org.bson.Document;
import org.as.am.encrypt.AES;
import com.mongodb.MongoClient;
import org.as.am.model.Account;
import com.mongodb.BasicDBObject;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.as.am.model.QuestionAnswer;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;

import static org.as.am.properties.MongoDBProperties.*;

public class MongoDBAccountDAO
    implements AccountDAO {
    private final MongoClient client;
    private final MongoDatabase database;
    private final MongoCollection collection;

    public MongoDBAccountDAO() {
        client     = new MongoClient(HOSTNAME, PORT);
        database   = client.getDatabase(DATABASE);
        collection = database.getCollection(COLLECTION_ACCOUNT);
    }

    public boolean insertAccount(Account account) {
        if(collection == null ||
           (account.getName() == null || account.getName().length() == 0) ||
           ((account.getEmail() == null || account.getEmail().length() == 0) &&
            (account.getUsername() == null || account.getUsername().length() == 0)) ||
           (account.getPassword() == null || account.getPassword().length() == 0)) {
            return false;
        }

        BasicDBObject document = new BasicDBObject();
        JSONArray qaJsonArr    = new JSONArray();

        // Create a JSON array of secret questions and answers
        if(account.getQAList() != null &&
           account.getQAList().size() > 0) {
            for(QuestionAnswer entry : account.getQAList()) {
                JSONObject qaJsonObj = new JSONObject();
                qaJsonObj.put("Q", entry.getQuestion());
                qaJsonObj.put("A", entry.getAnswer());
                qaJsonArr.add(qaJsonObj);
            }
        }

        // Create a document and insert it into the collection
        document.put(NAME    , account.getName());
        document.put(EMAIL   , account.getEmail() != null ?
                               AES.encrypt(account.getEmail()) : "");
        document.put(USERNAME, account.getUsername() != null ?
                               AES.encrypt(account.getUsername()) : "");
        document.put(PASSWORD, AES.encrypt(account.getPassword()));
        document.put(PIN     , AES.encrypt(account.getPin()));
        document.put(QA      , AES.encrypt(qaJsonArr.toJSONString()));
        collection.insertOne(document);

        return true;
    }

    public boolean updateAccount(Account account) {
        return false;
    }

    public boolean deleteAccounts(Account account) {
        return false;
    }

    public JSONObject findAccount(String accountName, String username) {
        JSONObject account = null;

        if(collection == null ||
           accountName == null ||
           accountName.length() == 0) {
            return null;
        }

        BasicDBObject query = new BasicDBObject(NAME, accountName);
        FindIterable<Document> documents;

        if(username != null &&
           username.length() > 0) {
            query.append(USERNAME, username);
        }
        documents = collection.find(query);
        if(documents != null) {
            Document document = documents.first();
            account = new JSONObject();
            account.put(NAME    , document.getString(NAME));
            account.put(EMAIL   , document.get(EMAIL) != null ?
                                  AES.decrypt(document.getString(EMAIL)) : "");
            account.put(USERNAME, document.get(USERNAME) != null ?
                                  AES.decrypt(document.getString(USERNAME)) : "");
            account.put(PASSWORD, AES.decrypt(document.getString(PASSWORD)));
            account.put(PIN     , document.get(PIN) != null ?
                                  AES.decrypt(document.getString(PIN)) : "");
            account.put(QA      , document.get(QA) != null ?
                                  AES.decrypt(document.getString(QA)) : new JSONArray());
        }

        return account;
    }

    public JSONArray findAccounts(String accountName) {
        JSONArray accounts = null;

        if(collection == null ||
           accountName == null ||
           accountName.length() == 0) {
            return null;
        }

        BasicDBObject query = new BasicDBObject(NAME, accountName);
        FindIterable<Document> documents = collection.find(query);

        if(documents != null) {
            accounts = new JSONArray();
            for(Document document : documents) {
                JSONObject account = new JSONObject();
                account.put(NAME    , document.getString(NAME));
                account.put(EMAIL   , document.get(EMAIL) != null ?
                        AES.decrypt(document.getString(EMAIL)) : "");
                account.put(USERNAME, document.get(USERNAME) != null ?
                        AES.decrypt(document.getString(USERNAME)) : "");
                account.put(PASSWORD, AES.decrypt(document.getString(PASSWORD)));
                account.put(PIN     , document.get(PIN) != null ?
                        AES.decrypt(document.getString(PIN)) : "");
                account.put(QA      , document.get(QA) != null ?
                        AES.decrypt(document.getString(QA)) : new JSONArray());
                accounts.add(account);
            }
        }

        return accounts;
    }
}