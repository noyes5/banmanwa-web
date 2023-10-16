package com.banmanwa.web.location.domain;

import com.banmanwa.web.exception.BanManWaException;
import com.banmanwa.web.location.dto.UtilityDocument;
import com.banmanwa.web.location.dto.UtilityResponse;
import com.banmanwa.web.location.excpetion.LocationExceptionSet;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;

import java.util.List;
import java.util.Objects;

public class UtilityRequester {

    private static final String BASIC_URL = "/v2/local/search/category.json";
    private static final int BASIC_RADIUS = 1000;
    private final WebClient webClient;

    public UtilityRequester(WebClient webClient) {
        this.webClient = webClient;
    }

    public List<UtilityDocument> requestUtility(String categoryCode, double x, double y) {
        try {
            UtilityResponse utilityResponse = receivedUtilityResponse(categoryCode, x, y);
            return Objects.requireNonNull(utilityResponse).getDocuments();
        } catch (WebClientRequestException e) {
            throw new BanManWaException(LocationExceptionSet.INVALID_LOCATION);
        }
    }

    private UtilityResponse receivedUtilityResponse(String categoryCode, double x, double y) {
        return webClient.get()
            .uri(uriBuilder ->
                uriBuilder.path(BASIC_URL)
                    .queryParam("x", x)
                    .queryParam("y", y)
                    .queryParam("category_group_code", categoryCode)
                    .queryParam("radius", BASIC_RADIUS)
                    .queryParam("sort", "distance")
                    .build()
            )
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .bodyToMono(UtilityResponse.class)
            .block();
    }
}
