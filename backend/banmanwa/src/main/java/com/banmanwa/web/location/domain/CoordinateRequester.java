package com.banmanwa.web.location.domain;

import com.banmanwa.web.exception.BanManWaException;
import com.banmanwa.web.location.dto.AxisDocument;
import com.banmanwa.web.location.dto.AxisResponse;
import com.banmanwa.web.location.excpetion.LocationExceptionSet;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;

import java.util.List;
import java.util.Objects;

public class CoordinateRequester {

    private static final String BASIC_URL = "/v2/local/search/address.json";
    private final WebClient webClient;

    public CoordinateRequester(WebClient webClient) {
        this.webClient = webClient;
    }

    public List<AxisDocument> requestCoordinate(String address) {
        try {
            AxisResponse axisResponse = receivedAxisResponse(address);
            return Objects.requireNonNull(axisResponse).getDocuments();
        } catch (WebClientRequestException e) {
            throw new BanManWaException(LocationExceptionSet.INVALID_LOCATION);
        }
    }

    private AxisResponse receivedAxisResponse(String address) {
        return webClient.get().uri(uriBuilder ->
                uriBuilder.path(BASIC_URL)
                        .queryParam("query", address)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(AxisResponse.class)
                .block();
    }
}
