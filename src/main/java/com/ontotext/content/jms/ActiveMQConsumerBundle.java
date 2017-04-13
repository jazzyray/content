package com.ontotext.content.jms;

import com.mockrunner.jms.DestinationManager;
import com.ontotext.content.configuration.ActiveMQConfig;
import com.ontotext.content.service.AnnotatedContentPublisherService;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.lifecycle.Managed;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.springframework.jms.core.JmsTemplate;

public class ActiveMQConsumerBundle implements ConfiguredBundle<ActiveMQConfigHolder>, Managed {

    ActiveMQQueueConsumer activeMQQueueConsumer;
    QueueMessageConsumer queueMessageConsumer;
    JmsTemplate jmsTemplate;


    public void run(ActiveMQConfigHolder configuration, Environment environment) {
        DestinationManager destinationManager = new DestinationManager();
        this.jmsTemplate = new JmsTemplate();
        this.queueMessageConsumer = new QueueMessageConsumer(new AnnotatedContentConsumer(this.jmsTemplate, new AnnotatedContentPublisherService()), configuration.getActiveMQ().queueName, 30);
        this.activeMQQueueConsumer = new ActiveMQQueueConsumer(this.jmsTemplate, configuration.getActiveMQ().brokerUrl, configuration.getActiveMQ().queueName, this.queueMessageConsumer);
    }

    public void start() {
        this.activeMQQueueConsumer.start();
    }

    public void stop() {
        try {
            this.activeMQQueueConsumer.stop();
        } catch (Exception e) {
            new RuntimeException(e);
        }
    }

    public void initialize(Bootstrap<?> bootstrap) {

    }
}
