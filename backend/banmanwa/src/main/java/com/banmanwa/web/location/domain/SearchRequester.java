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

public class SearchRequester {
    private static final String BASIC_URL = "/v2/local/search/keyword.json";
    private final WebClient webClient;

    public SearchRequester(WebClient webClient) {
        this.webClient = webClient;
    }

    public List<UtilityDocument> requestSearch(String keyword) {
        try {
            UtilityResponse utilityResponse = receivedSearchResponse(keyword);
            return Objects.requireNonNull(utilityResponse).getDocuments();
        } catch (WebClientRequestException e) {
            throw new BanManWaException(LocationExceptionSet.INVALID_LOCATION);
        }
    }

    private UtilityResponse receivedSearchResponse(String keyword) {
        return webClient.get()
            .uri(uriBuilder ->
                uriBuilder.path(BASIC_URL)
                    .queryParam("query", keyword)
                    .build()
                )
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .bodyToMono(UtilityResponse.class)
            .block();
    }
}
