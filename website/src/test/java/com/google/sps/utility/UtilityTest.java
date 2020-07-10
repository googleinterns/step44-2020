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

package com.google.sps.utility;

import static org.junit.Assert.assertTrue;
import com.google.sps.TestUtils;

import org.junit.Test;


public final class UtilityTest {

  private final String placesApiPizzaInNewYork;
  private final String servletPlacesApiPizzaInNewYork;
  private final String placeDetailResponseBody;
  private final String servletPlaceDetailResponseBody;
  
  
  public UtilityTest(){

    placesApiPizzaInNewYork = TestUtils.retrieveBody("/PlacesApiPizzaInNewYorkResponse.json");

    placeDetailResponseBody = TestUtils.retrieveBody("/PlaceDetailsResponse.json");

    servletPlaceDetailResponseBody = TestUtils.retrieveBody("/ServletPlaceDetailsResponse.txt");

    servletPlacesApiPizzaInNewYork = TestUtils.retrieveBody("/ServletPlacesApiPizzaInNewYorkResponse.txt");
  }

  
}
