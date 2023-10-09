package com.banmanwa.web.location.domain;

import com.banmanwa.web.exception.BanManWaException;
import com.banmanwa.web.location.dto.Document;
import com.banmanwa.web.location.dto.LocationResponse;
import com.banmanwa.web.location.excpetion.LocationExceptionSet;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;

import java.util.List;
import java.util.Objects;

public class LocationRequester {

    private static final String BASIC_URL = "/v2/local/geo/coord2regioncode.json?x=%s&y=%s";
    private final WebClient webClient;

    public LocationRequester(WebClient webClient) {
        this.webClient = webClient;
    }

    public List<Document> requestAddress(double x, double y) {
        try {
            String url = String.format(BASIC_URL, x, y);
            LocationResponse locationResponse = webClient.get()
                    .uri(url)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(LocationResponse.class)
                    .block();

            return Objects.requireNonNull(locationResponse).getDocuments();
        } catch (WebClientRequestException e) {
            throw new BanManWaException(LocationExceptionSet.INVALID_LOCATION);
        }
    }
}
