package com.ontotext.content.representation;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.net.URI;

public class AnnotatedContentResult {

    private URI location;
    private String status;

    public AnnotatedContentResult() {
        // Jackson deserialization
    }

    public AnnotatedContentResult(URI location, String status) {
        this.location = location;
        this.status = status;
    }

    @JsonProperty
    public URI getLocation() {
        return location;
    }

    @JsonProperty
    public String getStatus() {
        return status;
    }

    public void setLocation(URI location) {
        this.location = location;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
