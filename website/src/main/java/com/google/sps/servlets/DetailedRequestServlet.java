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

import static com.google.sps.utility.Utility.convertToJsonUsingGson;

import com.google.appengine.api.ThreadManager;
import com.google.maps.model.PlaceDetails;
import com.google.maps.PlacesApi;
import com.google.maps.GeoApiContext;
import com.google.maps.GaeRequestHandler;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit ;
import java.util.concurrent.ThreadFactory;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that returns some example content.*/
@WebServlet("/detailedRequest")
public class DetailedRequestServlet extends HttpServlet {

  protected GeoApiContext context = new GeoApiContext.Builder(new GaeRequestHandler.Builder())
    .apiKey("<insertAPIKeyHere>")
    .build();
  private HashMap<String,PlaceDetails> places =new HashMap<String,PlaceDetails>();

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

    places.clear();

    String rawPlaceIDs = request.getParameter("placeID"); 

    getPlaceIDFromString(rawPlaceIDs);

    populatePlaceDetails();

    String json = convertToJsonUsingGson(places);

    response.setContentType("application/json;");
    response.setCharacterEncoding("UTF-8");
    response.getWriter().println(json);

  }

  private void getPlaceIDFromString(String rawPlaceIDs){

    List<String> tempIDList = Arrays.asList(rawPlaceIDs.split("\\."));
    PlaceDetails TempDetails = new PlaceDetails();

    for(String ID : tempIDList){
      places.put(ID,TempDetails);
    }
  }

  class Task implements Runnable {
    String placeID;

    Task( String givenPlaceID) {
        placeID = givenPlaceID;
    }
    @Override
    public void run(){
      PlaceDetails apiResponse = new PlaceDetails();
      try{
        apiResponse = PlacesApi.placeDetails(context, placeID).await();
      }catch (Exception e) {
        e.printStackTrace();
      }

      places.replace(placeID,apiResponse);
    }
  }

  private void populatePlaceDetails () {
    ThreadFactory factory = ThreadManager.currentRequestThreadFactory();
    ExecutorService threadPool = Executors.newCachedThreadPool(factory);

    for (String ID : places.keySet()) {
      threadPool.submit(new Task(ID));
    }

    threadPool.shutdown();

    try {
      threadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
