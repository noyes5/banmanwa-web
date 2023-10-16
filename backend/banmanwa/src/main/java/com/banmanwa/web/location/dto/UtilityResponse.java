package com.banmanwa.web.location.dto;

import java.util.List;

public class UtilityResponse {

    private Meta meta;
    private List<UtilityDocument> documents;

    public UtilityResponse() {
    }

    public List<UtilityDocument> getDocuments() {
        return documents;
    }

    @Override
    public String toString() {
        return "UtilityResponse{" +
                "meta=" + meta +
                ", documents=" + documents +
                '}';
    }
}
