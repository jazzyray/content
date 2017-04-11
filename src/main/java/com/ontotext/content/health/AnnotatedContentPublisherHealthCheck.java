package com.ontotext.content.health;

import com.codahale.metrics.health.HealthCheck;

public class AnnotatedContentPublisherHealthCheck extends HealthCheck {

    @Override
    protected Result check() throws Exception {

        // @TODO add some proper health checks
        return HealthCheck.Result.healthy();
    }

}