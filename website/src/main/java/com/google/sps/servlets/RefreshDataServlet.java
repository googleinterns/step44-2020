// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import java.util.Random;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.PreparedQuery.TooManyResultsException;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import java.util.ArrayList;
import java.util.Arrays;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.Date;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/RefreshData")
public final class RefreshDataServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
      DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Query query = new Query("Restaurant").addSort("idNum", SortDirection.ASCENDING);
    PreparedQuery results = datastore.prepare(query);
    for (Entity entity : results.asIterable()) {
        long timestamp = System.currentTimeMillis();
        entity.setProperty("timestamp", timestamp);
        int newOrderVolume = (int) entity.getProperty("openOrderVolume");
        newOrderVolume+=getRandomChange();
        entity.setProperty("openOrderVolume", newOrderVolume);
        datastore.put(entity);
    }
    

    Gson gson = new Gson();
    ArrayList<String> restaurants = new ArrayList<>();
    for (Entity entity : results.asIterable()) {
         int id = (int) entity.getProperty("idNUm");
       int orderVolume = (int) entity.getProperty("openOrderVolume");
       String message = id + " : " + orderVolume;      
      restaurants.add(message); 
      }

    // Send the JSON as the response
    response.setContentType("application/json;");
    response.getWriter().println(gson.toJson(restaurants));
  }

  /**
   * gets a random number between 0 and 20 for openOrderVolume
   */
  private int getRandomChange() {
    
    Random r = new Random();
     r.setSeed(5); 
     int rand = r.nextInt(3);
  
    if(rand==0){
        return 0;
    }
    if(rand ==1){
        return -1;
    }
    return 1;
  }

  

}