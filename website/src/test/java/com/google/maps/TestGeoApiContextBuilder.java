package com.google.maps;

public class TestGeoApiContextBuilder extends GeoApiContext.Builder {
    public TestGeoApiContextBuilder(String urlOverride) {
        super();
        baseUrlOverride(urlOverride);
    }
  }