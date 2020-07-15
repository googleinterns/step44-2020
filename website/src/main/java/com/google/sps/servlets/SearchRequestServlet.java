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
import com.google.maps.model.PlacesSearchResponse;
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

  protected GeoApiContext context = new GeoApiContext.Builder(new GaeRequestHandler.Builder())
    .apiKey("<insertAPIKeyHere>")
    .build();

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    
    try {
      String query = request.getParameter("query");
      PlacesSearchResponse apiResponse = PlacesApi.textSearchQuery(context,query).await();


      String json = convertToJsonUsingGson(apiResponse);

      response.setContentType("application/json;");
      response.setCharacterEncoding("UTF-8");
      response.getWriter().println(json);

    } catch (Exception e) {
        e.printStackTrace();
    }
  }
  
}