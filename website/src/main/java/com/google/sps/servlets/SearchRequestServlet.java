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

import com.google.gson.Gson;
import com.google.maps.model.LatLng;
//import com.google.maps.model.PlacesSearchResponse;
import com.google.maps.model.PlaceDetails;
import com.google.maps.PlacesApi;
import com.google.maps.GeoApiContext;
import com.google.maps.GaeRequestHandler;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that returns some example content.*/
@WebServlet("/searchRequest")
public class SearchRequestServlet extends HttpServlet {

  private GeoApiContext context = new GeoApiContext.Builder(new GaeRequestHandler.Builder())
    .apiKey("<PasteAPIKEYHere>")
    .build();
  private static final String GOOGLE_SYDNEY = "ChIJN1t_tDeuEmsRUsoyG83frY4";

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    try {
      PlaceDetails placeDetails = PlacesApi.placeDetails(context, GOOGLE_SYDNEY).await();

      String json = convertToJsonUsingGson(placeDetails);

      response.setContentType("application/json;");
      response.setCharacterEncoding("UTF-8");
      response.getWriter().println(json);

    } catch (Exception e) {
        // Handle error
    }
  }

  private String convertToJsonUsingGson(PlaceDetails response) {
    Gson gson = new Gson();

    String json = gson.toJson(response, PlaceDetails.class);
    return json;
  }
}