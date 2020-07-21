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

import com.google.appengine.api.datastore.Entity;
import java.util.ArrayList;
import java. util. Collections;
import java.util.List;

import static org.junit.Assert.assertTrue;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;
import static org.mockito.ArgumentMatchers.*;

import com.google.appengine.api.datastore.Query;
import com.google.sps.LocalTestServerContext;
import com.google.sps.TestUtils;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.MockitoAnnotations;
import org.mockito.ArgumentMatchers;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.api.datastore.PreparedQuery.TooManyResultsException;
import com.google.appengine.api.datastore.PreparedQuery;

import java.util.Scanner;
import java.util.Random;

@RunWith(MockitoJUnitRunner.class)
public final class RefreshDataServletTest {
  protected final long[] volumes= {18, 10, 11, 19, 4, 10, 18, 13, 9, 9, 7, 15, 17, 6, 10, 20, 3, 7, 15, 0};

  private final ArrayList<Entity> MockDatastoreList = new ArrayList<Entity>();
  private final String refreshString;
  private final LocalServiceTestHelper helper =
      new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
 @Before
  public void setUp() {
    helper.setUp();
  }

  @After
  public void tearDown() {
    helper.tearDown();
  }
  public RefreshDataServletTest(){

    refreshString = TestUtils.retrieveBody("/Refresh.json");
    
  }

  class RefreshDataServletTester extends RefreshDataServlet {
      
    public RefreshDataServletTester(){

    DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
    for (int i = 0; i < 20; i++){
    Entity restaurantEntity = new Entity("Restaurant");
    long timestamp = System.currentTimeMillis();
    restaurantEntity.setProperty("idNum", i);
    restaurantEntity.setProperty("timestamp", timestamp);
    restaurantEntity.setProperty("openOrderVolume", volumes[i]);
    ds.put(restaurantEntity);
    MockDatastoreList.add(restaurantEntity);
    }
    
  }
  }

  @Test
  public void testRefreshDataServlet() throws Exception {
      Random r = mock(Random.class);
      HttpServletRequest request = mock(HttpServletRequest.class);
      HttpServletResponse response = mock(HttpServletResponse.class);    
      PreparedQuery results = mock(PreparedQuery.class);
    int cntr =0;
     
      cntr++;
      StringWriter stringWriter = new StringWriter();
      PrintWriter writer = new PrintWriter(stringWriter);
      
      when(response.getWriter()).thenReturn(writer);

      new RefreshDataServletTester().doGet(request, response);
      
      
      writer.flush();
      System.out.println(stringWriter.toString() );
      System.out.println(refreshString);
      assertTrue(stringWriter.toString().contains(refreshString));
    }
  
}
