package com.ontotext.content.jms;

import com.mockrunner.jms.ConfigurationManager;
import com.mockrunner.jms.DestinationManager;
import com.mockrunner.mock.jms.MockQueueConnectionFactory;
import com.ontotext.content.exception.MessageConsumerStartException;
import com.ontotext.content.service.AnnotatedContentPublisherService;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.Destination;

public class MockRunnerQueueConsumer {

    QueueMessageConsumer queueMessageConsumer;
    AnnotatedContentConsumer annotatedContentConsumer;
    JmsTemplate jmsTemplate;
    Destination destination;
    String queueName;

    public MockRunnerQueueConsumer(JmsTemplate jmsTemplate, String queueName, QueueMessageConsumer queueMessageConsumer) {
        DestinationManager destinationManager = new DestinationManager();
        this.jmsTemplate = jmsTemplate;
        this.queueMessageConsumer = queueMessageConsumer;

        MockQueueConnectionFactory mockRunnerConnectionFactory = new MockQueueConnectionFactory(destinationManager, new ConfigurationManager());

        this.destination = destinationManager.createQueue(queueName);
        this.jmsTemplate.setConnectionFactory(mockRunnerConnectionFactory);

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

    public QueueMessageConsumer getQueueMessageConsumer() {
        return queueMessageConsumer;
    }

    public void setQueueMessageConsumer(QueueMessageConsumer queueMessageConsumer) {
        this.queueMessageConsumer = queueMessageConsumer;
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

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }
}
