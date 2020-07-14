/*
 * Copyright 2014 Google Inc. All rights reserved.
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under
 * the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF
 * ANY KIND, either express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package com.google.sps.servlets;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.atLeast;

import com.google.maps.GeoApiContext;
import com.google.sps.LocalTestServerContext;
import com.google.sps.TestUtils;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public final class DetailedRequestSevletTest {


  private final String placeDetailResponseBody;
  private final ArrayList<String> multiplePlaceDetailsResponseBody = new ArrayList<String>();
  private final String servletPlaceDetailResponseBody;
  private final String servletMultiplePlaceDetailsResponseBody;
  
  

  public DetailedRequestSevletTest(){

    placeDetailResponseBody = TestUtils.retrieveBody("/PlaceDetailsResponse.json");

    servletPlaceDetailResponseBody = TestUtils.retrieveBody("/ServletPlaceDetailsResponse.txt");

    servletMultiplePlaceDetailsResponseBody = TestUtils.retrieveBody("/ServletMultiplePlaceDetailsResponseBody.txt");

    for(int i = 0; i < 3; i++){
      multiplePlaceDetailsResponseBody.add(placeDetailResponseBody);
    }

  }

  class DetailedRequestSevletTester extends DetailedRequestServlet {
    
    public DetailedRequestSevletTester(GeoApiContext mockContext){
      context = mockContext;
    }
 
  }

  @Test
  public void testPlaceDetailsRequest() throws Exception {
    try (LocalTestServerContext sc = new LocalTestServerContext(placeDetailResponseBody)) {
      HttpServletRequest request = mock(HttpServletRequest.class);       
      HttpServletResponse response = mock(HttpServletResponse.class);    

      when(request.getParameter("placeID")).thenReturn("AILJFakePlaceID");

      StringWriter stringWriter = new StringWriter();
      PrintWriter writer = new PrintWriter(stringWriter);
      when(response.getWriter()).thenReturn(writer);

      new DetailedRequestSevletTester(sc.context).doGet(request, response);

      verify(request, atLeast(1)).getParameter("placeID");
      
      writer.flush();

      assertTrue(stringWriter.toString().contains(servletPlaceDetailResponseBody));
    }
  }
  
  @Test
  public void testMultiplePlaceDetailsRequests() throws Exception {
    try (LocalTestServerContext sc = new LocalTestServerContext(multiplePlaceDetailsResponseBody)) {
      HttpServletRequest request = mock(HttpServletRequest.class);       
      HttpServletResponse response = mock(HttpServletResponse.class);    

      when(request.getParameter("placeID")).thenReturn("AILJFakePlaceID0-AILJFakePlaceID1-AILJFakePlaceID2");

      StringWriter stringWriter = new StringWriter();
      PrintWriter writer = new PrintWriter(stringWriter);
      when(response.getWriter()).thenReturn(writer);

      new DetailedRequestSevletTester(sc.context).doGet(request, response);

      verify(request, atLeast(1)).getParameter("placeID");

      writer.flush();

      System.out.println(servletMultiplePlaceDetailsResponseBody);

      System.out.println(stringWriter.toString());

      assertTrue(stringWriter.toString().contains(servletMultiplePlaceDetailsResponseBody));
    }
  }
}