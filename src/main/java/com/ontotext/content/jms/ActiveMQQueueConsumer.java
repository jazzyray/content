package com.ontotext.content.jms;

import com.mockrunner.jms.ConfigurationManager;
import com.mockrunner.jms.DestinationManager;
import com.mockrunner.mock.jms.MockQueueConnectionFactory;
import com.ontotext.content.exception.MessageConsumerStartException;
import com.ontotext.content.service.AnnotatedContentPublisherService;
import io.dropwizard.lifecycle.Managed;
import org.apache.activemq.spring.ActiveMQConnectionFactory;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.Destination;
import java.util.Properties;

public class ActiveMQQueueConsumer implements Managed {

    QueueMessageConsumer queueMessageConsumer;
    AnnotatedContentConsumer annotatedContentConsumer;
    JmsTemplate jmsTemplate;
    Destination destination;
    String brokerURL;
    String queueName;

    public ActiveMQQueueConsumer(JmsTemplate jmsTemplate, String brokerURL, String queueName, QueueMessageConsumer queueMessageConsumer) {
        DestinationManager destinationManager = new DestinationManager();
        this.jmsTemplate = jmsTemplate;
        this.queueMessageConsumer = queueMessageConsumer;

        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
        Properties activeMQProperties = new Properties();
        activeMQProperties.setProperty("brokerURL", brokerURL);
        activeMQConnectionFactory.setProperties(activeMQProperties);

        this.destination = destinationManager.createQueue(queueName);
        this.jmsTemplate.setConnectionFactory(activeMQConnectionFactory);

        this.jmsTemplate.setDefaultDestination(this.destination);
        this.annotatedContentConsumer = new AnnotatedContentConsumer(jmsTemplate, new AnnotatedContentPublisherService());

        this.queueMessageConsumer = new QueueMessageConsumer(this.annotatedContentConsumer, queueName, 30);

    }

    public void start() {
        try {
            this.queueMessageConsumer.start();
        } catch (Exception e) {
            throw new MessageConsumerStartException();
        }
    }

    public void stop() throws Exception {
        this.queueMessageConsumer.stop();
    }


    public AnnotatedContentConsumer getAnnotatedContentConsumer() {
        return annotatedContentConsumer;
    }

    public void setAnnotatedContentConsumer(AnnotatedContentConsumer annotatedContentConsumer) {
        this.annotatedContentConsumer = annotatedContentConsumer;
    }

    public JmsTemplate getJmsTemplate() {
        return jmsTemplate;
    }

    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public Destination getDestination() {
        return destination;
    }

    public void setDestination(Destination destination) {
        this.destination = destination;
    }

    public String getBrokerURL() {
        return brokerURL;
    }

    public void setBrokerURL(String brokerURL) {
        this.brokerURL = brokerURL;
    }

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }
}
