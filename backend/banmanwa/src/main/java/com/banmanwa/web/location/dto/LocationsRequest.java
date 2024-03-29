package com.banmanwa.web.location.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class LocationsRequest {

    @JsonProperty("locations")
    private List<LocationRequest> locationRequests;

    public LocationsRequest() {
    }

    public List<LocationRequest> getLocationsRequests() {
        return locationRequests;
    }
}
