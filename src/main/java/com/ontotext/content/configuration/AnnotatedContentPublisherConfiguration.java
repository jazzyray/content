package com.ontotext.content.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ontotext.content.jms.ActiveMQConfigHolder;
import io.dropwizard.Configuration;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class AnnotatedContentPublisherConfiguration extends Configuration implements ActiveMQConfigHolder {

    @JsonProperty("swagger")
    public SwaggerBundleConfiguration swaggerBundleConfiguration;

    @JsonProperty
    @NotNull
    @Valid
    private ActiveMQConfig activeMQ;


    public ActiveMQConfig getActiveMQ() {
        return this.activeMQ;
    }
}
