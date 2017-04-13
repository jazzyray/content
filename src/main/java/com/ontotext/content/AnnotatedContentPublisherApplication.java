package com.ontotext.content;


import com.ontotext.content.configuration.AnnotatedContentPublisherConfiguration;
import com.ontotext.content.health.AnnotatedContentPublisherHealthCheck;
import com.ontotext.content.jms.ActiveMQConsumerBundle;
import com.ontotext.content.resource.AnnotatedContentPublisherResource;
import com.ontotext.content.service.AnnotatedContentPublisherService;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;

public class AnnotatedContentPublisherApplication extends Application<AnnotatedContentPublisherConfiguration> {

    ActiveMQConsumerBundle activeMQConsumerBundle;
    AnnotatedContentPublisherService annotatedContentPublisherService;

    public static void main(String[] args) throws Exception {
        new AnnotatedContentPublisherApplication().run(args);
    }

    @Override
    public void run(AnnotatedContentPublisherConfiguration annotatedContentPublisherConfiguration, Environment environment) throws Exception {


        final AnnotatedContentPublisherResource resource = new AnnotatedContentPublisherResource(this.annotatedContentPublisherService);

        final AnnotatedContentPublisherHealthCheck healthCheck = new AnnotatedContentPublisherHealthCheck();
        environment.healthChecks().register("annotatedcontent", healthCheck);

        environment.jersey().register(resource);

        this.activeMQConsumerBundle.start();

    }

    @Override
    public void initialize(Bootstrap<AnnotatedContentPublisherConfiguration> bootstrap) {
        // @TODO Add health check
        // bootstrap.addBundle(new HealthCheckBundle());

        bootstrap.addBundle(new SwaggerBundle<AnnotatedContentPublisherConfiguration>() {
            @Override
            protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(AnnotatedContentPublisherConfiguration configuration) {
                return configuration.swaggerBundleConfiguration;
            }
        });

        this.activeMQConsumerBundle = new ActiveMQConsumerBundle();
        bootstrap.addBundle(this.activeMQConsumerBundle);
    }

}
