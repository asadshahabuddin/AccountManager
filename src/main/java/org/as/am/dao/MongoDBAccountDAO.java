package org.as.am.dao;

import org.bson.Document;
import org.as.am.encrypt.AES;
import com.mongodb.MongoClient;
import org.as.am.model.Account;
import org.bson.types.ObjectId;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.as.am.model.QuestionAnswer;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;

import static org.as.am.properties.MongoDBProperties.*;

public class MongoDBAccountDAO
    implements AccountDAO {
    /**
     * Check if the account is valid
     * @param account
     *     Account
     * @return
     *     true iff the account is valid
     */
    private boolean isValidAccount(Account account) {
        return !(account == null ||
                 (account.getName() == null || account.getName().length() == 0) ||
                 ((account.getEmail() == null || account.getEmail().length() == 0) &&
                  (account.getUsername() == null || account.getUsername().length() == 0)) ||
                 (account.getPassword() == null || account.getPassword().length() == 0));
    }

    /**
     * Check if the account is unique
     * @param account
     *     Account
     * @return
     *     true iff the account is unique
     */
    private boolean isUniqueAccount(Account account) {
        return !(account.getEmail() != null &&
                 account.getEmail().length() > 0 &&
                 findAccountDetails(account.getName(), account.getEmail()).size() > 0);
    }

    /**
     * Create a document for the specified account
     * @param account
     *     Account
     * @return
     *     The equivalent document
     */
    private Document createDocument(Account account) {
        Document document   = new Document();
        JSONArray qaJsonArr = new JSONArray();

        // Create a JSON array of secret questions and answers
        if(account.getQAList() != null) {
            for(QuestionAnswer entry : account.getQAList()) {
                JSONObject qaJsonObj = new JSONObject();
                qaJsonObj.put("Q", entry.getQuestion());
                qaJsonObj.put("A", entry.getAnswer());
                qaJsonArr.add(qaJsonObj);
            }
        }

        // Populate the document
        document.put(NAME    , account.getName());
        document.put(EMAIL   , AES.encrypt(account.getEmail()));
        document.put(USERNAME, AES.encrypt(account.getUsername()));
        document.put(PASSWORD, AES.encrypt(account.getPassword()));
        document.put(PIN     , AES.encrypt(account.getPin()));
        document.put(QA      , AES.encrypt(qaJsonArr.toJSONString()));

        return document;
    }

    public boolean insertAccount(Account account) {
        boolean inserted = false;
        if(!isValidAccount(account)) {
            return inserted;
        }

        MongoClient client = new MongoClient(HOSTNAME, PORT);
        MongoCollection collection = null;
        try {
            if(client != null) {
                MongoDatabase database = client.getDatabase(DATABASE);
                if(database != null) {
                    collection = database.getCollection(COLLECTION_ACCOUNT);
                }
            }
            if(collection != null &&
               isUniqueAccount(account)) {
                collection.insertOne(createDocument(account));
                inserted = true;
            }
        } finally {
            if(client != null) {
                client.close();
            }
        }
        return inserted;
    }

    // Coming this Fall
    public boolean updateAccount(String accountId, Account account) {
        boolean updated = false;
        if(accountId == null ||
           !isValidAccount(account)) {
            return updated;
        }

        MongoClient client = new MongoClient(HOSTNAME, PORT);
        MongoCollection collection = null;
        try {
            if(client != null) {
                MongoDatabase database = client.getDatabase(DATABASE);
                if(database != null) {
                    collection = database.getCollection(COLLECTION_ACCOUNT);
                }
            }
            if(collection != null) {
                Document filter      = new Document(ID, new ObjectId(accountId)),
                         document    = createDocument(account),
                         newDocument = new Document("$set", document);
                updated = collection.updateOne(filter, newDocument).getModifiedCount() > 0;
            }
        } finally {
            if(client != null) {
                client.close();
            }
        }
        return updated;
    }

    public boolean deleteAccount(String accountId) {
        boolean deleted = false;
        if(accountId == null ||
           accountId.length() == 0) {
            return deleted;
        }

        MongoClient client = new MongoClient(HOSTNAME, PORT);
        MongoCollection collection = null;
        try {
            if(client != null) {
                MongoDatabase database = client.getDatabase(DATABASE);
                if(database != null) {
                    collection = database.getCollection(COLLECTION_ACCOUNT);
                }
            }
            if(collection != null) {
                Document filter = new Document(ID, new ObjectId(accountId));
                deleted = collection.deleteOne(filter).getDeletedCount() > 0;
            }
        } finally {
            if(client != null) {
                client.close();
            }
        }
        return deleted;
    }

    public JSONArray findAccounts() {
        MongoClient client         = new MongoClient(HOSTNAME, PORT);
        MongoCollection collection = null;
        JSONArray accounts         = new JSONArray();

        try {
            if(client != null) {
                MongoDatabase database = client.getDatabase(DATABASE);
                if(database != null) {
                    collection = database.getCollection(COLLECTION_ACCOUNT);
                }
            }
            if(collection != null) {
                FindIterable<Document> documents = collection.find();
                if(documents != null) {
                    for(Document document : documents) {
                        JSONObject account = new JSONObject();
                        account.put(ID  , document.get(ID).toString());
                        account.put(NAME, document.getString(NAME));
                        accounts.add(account);
                    }
                }
            }
        } finally {
            if(client != null) {
                client.close();
            }
        }

        return accounts;
    }

    public JSONArray findAccountDetails(String accountName, String email) {
        JSONArray accountDetails = new JSONArray();
        if(accountName == null ||
           accountName.length() == 0) {
            return accountDetails;
        }

        MongoClient client = new MongoClient(HOSTNAME, PORT);
        MongoCollection collection = null;
        try {
            if(client != null) {
                MongoDatabase database = client.getDatabase(DATABASE);
                if(database != null) {
                    collection = database.getCollection(COLLECTION_ACCOUNT);
                }
            }
            if(collection != null) {
                Document filter = new Document(NAME, accountName);
                if(email != null &&
                   email.length() > 0) {
                    filter.append(EMAIL, AES.encrypt(email));
                }
                FindIterable<Document> documents = collection.find(filter);

                if(documents != null) {
                    for(Document document : documents) {
                        JSONObject account = new JSONObject();
                        account.put(ID      , document.get(ID).toString());
                        account.put(NAME    , document.getString(NAME));
                        account.put(EMAIL   , AES.decrypt(document.getString(EMAIL)));
                        account.put(USERNAME, AES.decrypt(document.getString(USERNAME)));
                        account.put(PASSWORD, AES.decrypt(document.getString(PASSWORD)));
                        account.put(PIN     , AES.decrypt(document.getString(PIN)));
                        account.put(QA      , AES.decrypt(document.getString(QA)));
                        accountDetails.add(account);
                    }
                }
            }
        } finally {
            if(client != null) {
                client.close();
            }
        }
        return accountDetails;
    }
}