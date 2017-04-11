import com.ontotext.content.configuration.AnnotatedContentPublisherConfiguration;
import com.ontotext.content.health.AnnotatedContentPublisherHealthCheck;
import com.ontotext.content.resource.AnnotatedContentPublisherResource;
import com.ontotext.content.service.AnnotatedContentPublisherService;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;

public class AnnotatedContentPublisherApplication extends Application<AnnotatedContentPublisherConfiguration> {

    AnnotatedContentPublisherService annotatedContentPublisherService;

    public static void main(String[] args) throws Exception {
        new AnnotatedContentPublisherApplication().run(args);
    }

    @Override
    public void run(AnnotatedContentPublisherConfiguration annotatedContentPublisherConfiguration, Environment environment) throws Exception {

        this.annotatedContentPublisherService = new AnnotatedContentPublisherService();
        final AnnotatedContentPublisherResource resource = new AnnotatedContentPublisherResource(this.annotatedContentPublisherService);

        final AnnotatedContentPublisherHealthCheck healthCheck = new AnnotatedContentPublisherHealthCheck();
        environment.healthChecks().register("annotation", healthCheck);

        environment.jersey().register(resource);

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
        });;
    }

}
