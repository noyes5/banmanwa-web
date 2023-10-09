package com.banmanwa.web.location.service;

import com.banmanwa.web.location.domain.LocationRequester;
import com.banmanwa.web.location.dto.Document;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class LocationService {

    private final WebClient webClient;

    public LocationService(WebClient webClient) {
        this.webClient = webClient;
    }

    public List<Document> findAddress(double x, double y) {

        LocationRequester locationRequester = new LocationRequester(webClient);
        return locationRequester.requestAddress(x, y);
    }
}
