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

import com.google.maps.model.PlaceDetails;
import com.google.maps.PlacesApi;
import com.google.maps.GeoApiContext;
import com.google.maps.GaeRequestHandler;
import java.io.IOException;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask; 
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

  private ArrayList<PlaceDetails> places = new ArrayList<PlaceDetails>();
  private ArrayList<String> placeIDs = new ArrayList<String>();

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String rawPlaceIDs = request.getParameter("placeID");

    getPlaceIDFromString(rawPlaceIDs);
    System.out.println("PlaceIDs");
    System.out.println(placeIDs);

    populatePlaceDetails();
    System.out.println("Places");
    System.out.println(places);
    System.out.println(places.size());

    String json = convertToJsonUsingGson(places);

    response.setContentType("application/json;");
    response.setCharacterEncoding("UTF-8");
    response.getWriter().println(json);
  }

  private void getPlaceIDFromString(String rawPlaceIDs){

    placeIDs.addAll(Arrays.asList(rawPlaceIDs.split("-")));
  }

  class Task implements Callable<PlaceDetails> {

    GeoApiContext context;
    String placeID;

    Task(GeoApiContext givenContext, String givenPlaceID) {
        context = givenContext;
        placeID = givenPlaceID;
    }
    public PlaceDetails call() throws Exception {
      PlaceDetails apiResponse = new PlaceDetails();
      try{
        apiResponse = PlacesApi.placeDetails(context, placeID).await();
      }catch (Exception e) {
        e.printStackTrace();
      }

      return apiResponse;
    }
  }
  
  private void populatePlaceDetails () {
    ExecutorService threadPool = Executors.newFixedThreadPool(20);
    CompletionService<PlaceDetails> taskCompletionService = new ExecutorCompletionService<PlaceDetails>(threadPool);

    for (int i = 0; i < placeIDs.size(); i++) {
      System.out.println("new Tread submited");
      taskCompletionService.submit(new Task (context, placeIDs.get(i)));
    }

    for (int i = 0; i < placeIDs.size(); i++) {
      try{
        Future<PlaceDetails> result = taskCompletionService.take();
        places.add(result.get());
      }catch (InterruptedException e) {
          System.out.println("Error Interrupted exception");
          e.printStackTrace();
      } catch (ExecutionException e) {
          System.out.println("Error get() threw exception");
          e.printStackTrace();
      }
    }
  }
}