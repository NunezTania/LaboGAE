package org.example;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@WebServlet(name = "HelloAppEngine", value = "/datastorewrite")
public class DatastoreWrite extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("text/plain");
        PrintWriter responseWriter = response.getWriter();
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Map<String, String[]> parameterNames = request.getParameterMap();

        if(parameterNames.containsKey("_kind")){
            responseWriter.println("found Kind: " + parameterNames.get("_kind")[0]);
            // Create entity for the first book. We assume there is only one book in the URI.
            Entity entity = new Entity(parameterNames.get("_kind")[0]);
            // Add the properties to the entity.
            parameterNames.forEach((key, value) -> {
                if(!key.equals("_kind")){
                    entity.setProperty(key, value[0]);
                    responseWriter.println("setProperty: " + key + ": " + value[0]);
                }
            });
            // Save the entity.
            datastore.put(entity);
            responseWriter.println("Entity written to datastore.");
        } else {
            responseWriter.println("No kind specified.");
        }
    }
}
