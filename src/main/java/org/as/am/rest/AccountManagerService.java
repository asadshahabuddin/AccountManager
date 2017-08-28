package org.as.am.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import org.json.simple.JSONArray;
import org.as.am.dao.MongoDBAccountDAO;

@Path("/accountmanager")
public class AccountManagerService {
    @GET
    @Path("/find")
    public Response findAccounts() {
        MongoDBAccountDAO dao = new MongoDBAccountDAO();
        JSONArray accounts = dao.findAccounts();
        return Response.status(Response.Status.OK).entity(accounts).build();
    }
}